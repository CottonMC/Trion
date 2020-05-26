package io.github.cottonmc.trion.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ProjectileEntity.class)
public interface ProjectileEntityAccessor {
	@Accessor
	static TrackedData<Byte> getPROJECTILE_FLAGS() {
		throw new IllegalStateException("Mixin failed to apply!");
	}

	@Invoker
	void invokeSetProjectileFlag(int index, boolean flag);

}
