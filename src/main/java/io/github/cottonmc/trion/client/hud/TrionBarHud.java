package io.github.cottonmc.trion.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.registry.TrionStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class TrionBarHud {
	private static MinecraftClient client = MinecraftClient.getInstance();

	private static final Identifier BAR_TEX = new Identifier(Trion.MODID, "textures/gui/bars.png");
	private static final Identifier ICON_TEX = new Identifier(Trion.MODID, "textures/icons/trion.png");

	private static final float MAX_FADE_TIME = 1000F;
	private static long currentFadeWait = 0; //TODO: implement?
	private static long currentFadeDelta = 0;
	private static long lastFadeTime = 0;
	private static boolean needDraw = true;

	//TODO: config?
	private static final int x = 4;
	private static final int y = 16;
	private static final int normalColor = 0x5FEC94;
	private static final int virtualColor = 0x5FD3EC;
	private static final int cooldownColor = 0xEC5F6B;
	private static final boolean bigBars = false;
	private static final int unitsPerBar = 50; //TODO: ever changes?

	//TODO: any better way to do color and fadeout?
	public static void render(float tickDelta) {
		int color = client.player.hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT)? virtualColor : normalColor;
		long now = System.nanoTime() / 1_000_000L;
		TrionComponent component = Trion.TRION_COMPONENT.get(client.player);
		if (component.isTriggerActive()) {
			drawBar(component, color, 1f);
		} else {
			if (component.getTrion() >= component.getMaxTrion()) {
				if (needDraw) {
					long elapsed = now - lastFadeTime;
					currentFadeDelta += elapsed;
					float progress = currentFadeDelta / MAX_FADE_TIME;
					if (progress < 1f) {
						drawBar(component, color,1 - progress);
					} else {
						currentFadeDelta = 0;
						needDraw = false;
					}
				}
			} else {
				needDraw = true;
				drawBar(component, color, 1f);
			}
		}
		lastFadeTime = now;
	}

	private static void drawBar(TrionComponent component, int color, float alpha) {
		//draw icon
		client.getTextureManager().bindTexture(ICON_TEX);
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		int left = x;
		int top = y;
		RenderSystem.color4f(1f, 1f, 1f, alpha);
		blit(left, top, 9, 9);

		left += 10;

		//draw bar
		float r = (color >> 16 & 255) / 255f;
		float g = (color >> 8 & 255) / 255f;
		float b = (color & 255) / 255f;
		client.getTextureManager().bindTexture(BAR_TEX);

		int boxes = (component.getMaxTrion() / unitsPerBar) - 1;
		boolean needsPlus = boxes > 36;
		int rows = (Math.min(boxes, 35) / 12) + 1;
		int fullBoxes = (component.getTrion() / unitsPerBar);
		if (fullBoxes > boxes) fullBoxes = boxes;
		boolean plusOn = fullBoxes > 36;

		if (!bigBars) {
			long aboveLastBox = component.getMaxTrion() % unitsPerBar;
			if (aboveLastBox == 0) aboveLastBox = unitsPerBar;
			long remainder = component.getTrion() % unitsPerBar;
			if (remainder == 0) remainder = unitsPerBar;
			float bgPercent = (float) aboveLastBox / (float) unitsPerBar;
			int bgLength = (int) (bgPercent * 62F);
			float fgPercent = (float)remainder / (float)aboveLastBox;
			if (fgPercent == 1 && component.getTrion() != component.getMaxTrion()) fgPercent = 0; //prevent situations where remainder == aboveLastBox but current != max
			int fgLength = (int)(fgPercent * bgLength);
			if (fgLength == 0) fgLength = component.getTrion() == 0? 0 : 1;

			//bar BG: left edge, middle, right edge
			blit(left, top, 1, 5, texUV(0), texUV(0), texUV(1), texUV(5));
			blit(left + 1, top, bgLength, 5, texUV(1), texUV(0), texUV(bgLength + 1), texUV(5));
			blit(left + bgLength + 1, top, 1, 5, texUV(63), texUV(0), texUV(64), texUV(5));
			if (boxes > 0) {
				int boxesLeft = boxes;
				int newTop = top + 4;
				for (int i = 0; i < rows; i++) {
					int toDraw = 12;
					if (boxesLeft > 12) {
						boxesLeft -= 12;
					} else {
						toDraw = boxesLeft;
					}
					//first box
					blit(left, newTop, 6, 5, texUV(0), texUV(5), texUV(6), texUV(10));
					int newLeft = left + 5;
					//the rest of the boxes
					for (int j = 1; j < toDraw; j++) {
						blit(newLeft, newTop, 6, 5, texUV(6), texUV(5), texUV(12), texUV(10));
						newLeft += 5;
					}
					if (needsPlus) {
						if (i < 2) {
							blit(newLeft, newTop, 3, 5, texUV(19), texUV(5), texUV(22), texUV(10));
						} else {
							blit(newLeft, newTop, 5, 5, texUV(22), texUV(5), texUV(27), texUV(10));
						}
					}
					newTop += 4;
				}
			}

			RenderSystem.color4f(r, g, b, alpha);
			//bar FG: left edge, middle, right edge
			blit(left, top, 1, 5, texUV(0), texUV(10), texUV(1), texUV(15));
			blit(left + 1, top, fgLength, 5, texUV(1), texUV(10), texUV(fgLength + 1), texUV(15));
			blit(left + fgLength + 1, top, 1, 5, texUV(63), texUV(10), texUV(64), texUV(15));
			if (fullBoxes > 0) {
				int boxesLeft = fullBoxes;
				int newTop = top + 4;
				for (int i = 0; i < rows; i++) {
					int toDraw = 12;
					if (boxesLeft > 12) {
						boxesLeft -= 12;
					} else {
						toDraw = boxesLeft;
						boxesLeft = 0;
					}
					//first box
					blit(left, newTop, 6, 5, texUV(0), texUV(15), texUV(6), texUV(20));
					int newLeft = left + 5;
					//the rest of the boxes
					for (int j = 1; j < toDraw; j++) {
						blit(newLeft, newTop, 6, 5, texUV(6), texUV(15), texUV(12), texUV(20));
						newLeft += 5;
					}
					if (plusOn) {
						if (i < 2) {
							blit(newLeft, newTop, 3, 5, texUV(19), texUV(15), texUV(22), texUV(20));
						} else {
							blit(newLeft, newTop, 5, 5, texUV(22), texUV(15), texUV(27), texUV(20));
						}
					}
					newTop += 4;
					if (boxesLeft == 0) break;
				}
			}
		} else {
			//bar BG: left edge, middle, right edge
			blit(left, top, 1, 9, texUV(0), texUV(20), texUV(1), texUV(29));
			blit(left + 1, top, 62, 9, texUV(1), texUV(20), texUV(63), texUV(29));
			blit(left + 63, top, 1, 9, texUV(63), texUV(20), texUV(64), texUV(29));

			RenderSystem.color4f(r, g, b, alpha);
			int fgLength = (int)(((float)component.getTrion() / (float)component.getMaxTrion()) * 62f);
			//bar FG: left edge, middle, right edge
			blit(left, top, 1, 9, texUV(0), texUV(29), texUV(1), texUV(38));
			blit(left + 1, top, fgLength, 9, texUV(1), texUV(29), texUV(fgLength + 1), texUV(38));
			blit(left + fgLength + 1, top, 1, 9, texUV(63), texUV(29), texUV(64), texUV(38));
		}

		RenderSystem.color4f(1f, 1f, 1f, 1f);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	private static void blit(int x, int y, int width, int height) {
		blit(x, y, width, height, 0f, 0f, 1f, 1f);
	}

	private static void blit(int x, int y, int width, int height, float u1, float v1, float u2, float v2) {
		innerBlit(x, y, x+width, y+height, 0d, u1, v1, u2, v2);
	}

	private static void innerBlit(double x1, double y1, double x2, double y2, double z, float u1, float v1, float u2, float v2) {
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
		buffer.vertex(x1, y2, z).texture(u1, v2).next();
		buffer.vertex(x2, y2, z).texture(u2, v2).next();
		buffer.vertex(x2, y1, z).texture(u2, v1).next();
		buffer.vertex(x1, y1, z).texture(u1, v1).next();
		tess.draw();
	}

	private static float texUV(int orig) {
		return ((float)orig) / 256f;
	}
}
