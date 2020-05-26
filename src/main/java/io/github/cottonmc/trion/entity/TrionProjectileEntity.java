package io.github.cottonmc.trion.entity;

import io.github.cottonmc.trion.combat.TrionDamageSource;
import io.github.cottonmc.trion.mixin.ProjectileEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class TrionProjectileEntity extends ProjectileEntity {

	public TrionProjectileEntity(EntityType<TrionProjectileEntity> type, World world) {
		super(type, world);
		this.pickupType = PickupPermission.DISALLOWED;
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	public void setExplosive(boolean explosive) {
		((ProjectileEntityAccessor)this).invokeSetProjectileFlag(8, explosive);
	}

	public boolean isExplosive() {
		return (this.dataTracker.get(ProjectileEntityAccessor.getPROJECTILE_FLAGS()) & 8) != 0;
	}

	//TODO: explosive trion damage source?
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (isExplosive()) {
			world.createExplosion(this, new TrionDamageSource("trion", getOwner()), this.getX(), this.getY(), this.getZ(), 4, false, Explosion.DestructionType.BREAK);
		}
	}

	@Override
	protected void onBlockCollision(BlockState state) {
		super.onBlockCollision(state);
		if (isExplosive()) {
			world.createExplosion(this, new TrionDamageSource("trion", getOwner()), this.getX(), this.getY(), this.getZ(), 4, false, Explosion.DestructionType.BREAK);
		}
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		this.setExplosive(tag.getBoolean("Explosive"));
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("Explosive", this.isExplosive());
	}
}
