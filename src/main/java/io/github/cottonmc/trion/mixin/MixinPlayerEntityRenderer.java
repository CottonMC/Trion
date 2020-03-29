package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.registry.TrionTriggers;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void hideWithChameleon(AbstractClientPlayerEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		TrionComponent comp = Trion.TRION_COMPONENT.get(entity);
		//only hide if we're not holding anything and the Chameleon trigger is in use!
		//TODO: hide shadow too
		if (comp.isTriggerActive()
				&& entity.getStackInHand(Hand.MAIN_HAND).isEmpty()
				&& entity.getStackInHand(Hand.OFF_HAND).isEmpty()
				&& comp.getConfig().getEquippedTriggers().contains(TrionTriggers.CHAMELEON)) info.cancel();
	}
}
