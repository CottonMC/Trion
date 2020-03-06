package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.registry.TrionItems;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class TrionArmorItem extends DyeableArmorItem {
	public TrionArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
	}

	public static ItemStack getTrionStack(EquipmentSlot slot, ItemStack previous, int color) {
		Item item;
		switch(slot) {
			case HEAD:
				item = TrionItems.TRION_HELMET;
				break;
			case CHEST:
				item = TrionItems.TRION_CHESTPLATE;
				break;
			case LEGS:
				item = TrionItems.TRION_LEGGINGS;
				break;
			case FEET:
				item = TrionItems.TRION_BOOTS;
				break;
			default:
				return previous;
		}
		ItemStack ret = new ItemStack(item);
		CompoundTag tag = ret.getOrCreateTag();
		tag.put("Previous", previous.toTag(new CompoundTag()));
		tag.putBoolean("Unbreakable", true);
		ret.addEnchantment(Enchantments.BINDING_CURSE, 1);
		((TrionArmorItem)item).setColor(ret, color);
		return ret;
	}

	public static ItemStack getOriginalStack(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Previous")) {
			return ItemStack.fromTag(tag.getCompound("Previous"));
		}
		return stack;
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return false;
	}
}
