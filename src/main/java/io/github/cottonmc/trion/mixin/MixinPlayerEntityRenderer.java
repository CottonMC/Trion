package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.registry.TrionTriggers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer {
	public MixinPlayerEntityRenderer(EntityRenderDispatcher dispatcher, EntityModel model, float shadowSize) {
		super(dispatcher, model, shadowSize);
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void hideWithChameleon(AbstractClientPlayerEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {

		TrionComponent comp = Trion.TRION_COMPONENT.get(entity);
		//only hide if we're not holding anything and the Chameleon trigger is in use!
		//TODO: hide shadow too
		if (comp.isTriggerActive() //if trigger is inactive, we're visible
				&& entity.getStackInHand(Hand.MAIN_HAND).isEmpty() //if main hand has a stack, we're visible
				&& entity.getStackInHand(Hand.OFF_HAND).isEmpty() //if off hand has a tack, we're visible
				&& comp.getConfig().getEquippedTriggers().contains(TrionTriggers.CHAMELEON) //if we don't hae Chameleon, we're visible
				&& (MinecraftClient.getInstance().player != null && entity.isInvisibleTo(MinecraftClient.getInstance().player))) //if the viewer should be allowed to see us we're visible
			info.cancel();
	}

	@Override
	protected boolean isFullyVisible(LivingEntity entity) {
		TrionComponent comp = Trion.TRION_COMPONENT.get(entity);
		return (!comp.isTriggerActive() //if trigger is inactive, we're visible
				|| !entity.getStackInHand(Hand.MAIN_HAND).isEmpty() //if main hand has a stack, we're visible
				|| !entity.getStackInHand(Hand.OFF_HAND).isEmpty() //if off hand has a stack, we're visible
				|| !comp.getConfig().getEquippedTriggers().contains(TrionTriggers.CHAMELEON)) //if we don't have Chameleon, we're bisible
				&& super.isFullyVisible(entity);
	}
}
