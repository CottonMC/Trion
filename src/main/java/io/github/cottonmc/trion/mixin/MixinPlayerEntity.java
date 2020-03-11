package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.combat.TrionDamageSource;
import io.github.cottonmc.trion.registry.TrionStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	@Shadow public abstract boolean isCreative();

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickTrion(CallbackInfo info) {
		TrionComponent comp = Trion.TRION_COMPONENT.get(this);
		if (!world.isClient) comp.tick();
	}

	//TODO: actually make an entity damage event
	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropShoulderEntities()V"), cancellable = true)
	private void doTrionDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		//trion won't protect you against the void, unblockable+piercing damage, or magic
		if ((source.isUnblockable() && source.bypassesArmor()) || source.isOutOfWorld() || source.getMagic()) return;
		TrionComponent comp = Trion.TRION_COMPONENT.get(this);
		//TODO: better way to fix so virtual combat can't be used to cheese PvE?
		if (hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT) && !(source instanceof TrionDamageSource)) return;
		if (comp.isTriggerActive()) {
			int trionCost = source.bypassesArmor()? (int)Math.ceil(amount * 5) : (int)Math.ceil(amount * 2.5); //TODO: rebalance?
			comp.setTrion(comp.getTrion() - trionCost);
			super.damage(source, 0f);
			info.cancel();
		}
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void doTrionAttack(Entity target, CallbackInfo info) {
		if (isCreative()) return;
		TrionComponent comp = Trion.TRION_COMPONENT.get(this);
		if (comp.isTriggerActive()
				&& hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT)
				&& target instanceof LivingEntity
				&& !(target instanceof PlayerEntity)) {
			//TODO: check if using trion weapon?
			info.cancel();
		}
	}

}
