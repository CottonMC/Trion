package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.combat.TrionDamageSource;
import io.github.cottonmc.trion.registry.TrionEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

	@Inject(method = "arrow", at = @At("HEAD"), cancellable = true)
	private static void injectTrionDamageSource(ProjectileEntity projectile, Entity attacker, CallbackInfoReturnable<DamageSource> info) {
		if (projectile.getType() == TrionEntities.TRION_PROJECTILE) {
			info.setReturnValue(new TrionDamageSource("trion", attacker)); //TODO: trion projectile damage source?
		}
	}
}
