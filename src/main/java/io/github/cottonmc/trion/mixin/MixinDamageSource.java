package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.combat.TrionDamageSource;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class MixinDamageSource {
	@Inject(method = "player", at = @At("HEAD"), cancellable = true)
	private static void injectTrionDamageSource(PlayerEntity attacker, CallbackInfoReturnable<DamageSource> info) {
		TrionComponent comp = Trion.TRION_COMPONENT.get(attacker);
		if (comp.isTriggerActive()) {
			info.setReturnValue(new TrionDamageSource("trion", attacker));
		}
	}
}
