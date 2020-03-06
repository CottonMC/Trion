package io.github.cottonmc.trion.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
		} else {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				user.equipStack(slot, TrionArmorItem.getTrionStack(slot, user.getEquippedStack(slot), 0xFFFFFF));
			}
			tag.putBoolean("Active", true);
		}
		return TypedActionResult.success(stack);
	}
}
