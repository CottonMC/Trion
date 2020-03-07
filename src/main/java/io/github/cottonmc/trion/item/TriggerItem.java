package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.registry.TrionParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TriggerItem extends Item {
	public TriggerItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (world.isClient) return TypedActionResult.success(stack);
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Active")) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				user.equipStack(slot, TrionArmorItem.getOriginalStack(user.getEquippedStack(slot)));
			}
			tag.remove("Active");
			user.getItemCooldownManager().set(this, 100);
		} else {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				user.equipStack(slot, TrionArmorItem.getTrionStack(slot, user.getEquippedStack(slot), 0x5FEC94));
			}
			tag.putBoolean("Active", true);
			user.getItemCooldownManager().set(this, 20);
			((ServerWorld)world).spawnParticles(TrionParticles.TRANSFORMATION, user.getX(), user.getY(), user.getZ(), 100, 0.0F, 0.0F, 0.0F, 0.25F);
		}
		return TypedActionResult.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (stack.getOrCreateTag().contains("Active") && !world.isClient()) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)entity;
				if (player.getItemCooldownManager().isCoolingDown(this)) {
					((ServerWorld)world).spawnParticles(TrionParticles.TRANSFORMATION, player.getX(), player.getY(), player.getZ(), 25, 0.0F, 0.0F, 0.0F, 0.25F);
				}
			}
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}
