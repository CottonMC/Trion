package io.github.cottonmc.trion.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.trion.api.TrionShield;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

	@Shadow protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);

	@Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
	private void renderCustomDurabilityBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String amount, CallbackInfo info) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (stack.getItem() instanceof TrionShield) {
			TrionShield durab = (TrionShield) stack.getItem();
			RenderSystem.disableDepthTest();
			RenderSystem.disableTexture();
			RenderSystem.disableAlphaTest();
			RenderSystem.disableBlend();

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder builder = tessellator.getBuffer();

			if (durab.getShieldDamage(player, stack) != 0) {
				float progress = ((float) (durab.getMaxShieldDamage(player, stack) - durab.getShieldDamage(player, stack))) / ((float) durab.getMaxShieldDamage(player, stack));
				int durability = (int) (13 * progress);
				int color = durab.getColor(player, stack);

				this.renderGuiQuad(builder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
				this.renderGuiQuad(builder, x + 2, y + 13, durability, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
			}

			RenderSystem.enableBlend();
			RenderSystem.enableAlphaTest();
			RenderSystem.enableTexture();
			RenderSystem.enableDepthTest();
		}
	}
}