package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.registry.TrionTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Entity.class)
public class MixinClientEntity {
	@Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
	private void injectInvisibility(CallbackInfoReturnable<Boolean> info) {
		Optional<TrionComponent> compOpt = Trion.TRION_COMPONENT.maybeGet(this);
		if (compOpt.isPresent() && (Entity)(Object)this instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity)(Object)this;
			TrionComponent comp = compOpt.get();
			if (comp.isTriggerActive()
					&& entity.getStackInHand(Hand.MAIN_HAND).isEmpty()
					&& entity.getStackInHand(Hand.OFF_HAND).isEmpty()
					&& comp.getConfig().getEquippedTriggers().contains(TrionTriggers.CHAMELEON)) info.setReturnValue(true);
		}
	}
}
