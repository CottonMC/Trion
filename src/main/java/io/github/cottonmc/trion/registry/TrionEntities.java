package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.entity.TrionProjectileEntity;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;

public class TrionEntities {
	public static final EntityType<TrionProjectileEntity> TRION_PROJECTILE = FabricEntityTypeBuilder
			.create(EntityCategory.MISC, TrionProjectileEntity::new)
			.size(EntityDimensions.fixed(0.5f, 0.5f))
			.setImmuneToFire()
			.build();
}
