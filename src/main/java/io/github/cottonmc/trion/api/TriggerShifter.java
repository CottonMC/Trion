package io.github.cottonmc.trion.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;

public interface TriggerShifter extends ItemConvertible {
	TriggerShifter NONE = new TriggerShifter() {
		@Override
		public Item asItem() {
			return Items.AIR;
		}

		@Override
		public ItemStack equip(ItemStack previous, TriggerConfig config) {
			return previous;
		}

		@Override
		public ItemStack unequip(ItemStack equipped) {
			return equipped;
		}
	};

	default ItemStack equip(ItemStack previous, TriggerConfig config) {
		ItemStack newStack = new ItemStack(asItem(), 1);
		CompoundTag tag = newStack.getOrCreateTag();
		tag.put("Previous", previous.toTag(new CompoundTag()));
		return newStack;
	}

	default ItemStack unequip(ItemStack equipped) {
		CompoundTag tag = equipped.getOrCreateTag();
		if (tag.contains("Previous")) {
			return ItemStack.fromTag(tag.getCompound("Previous"));
		}
		return equipped;
	}
}
