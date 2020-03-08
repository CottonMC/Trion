package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickTrion(CallbackInfo info) {
		TrionComponent comp = Trion.TRION_COMPONENT.get(this);
		if (!world.isClient) comp.tick();
	}
}
