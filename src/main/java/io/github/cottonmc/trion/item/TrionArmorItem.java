package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.api.TriggerConfig;
import io.github.cottonmc.trion.api.TriggerItem;
import io.github.cottonmc.trion.registry.TrionItems;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class TrionArmorItem extends DyeableArmorItem implements TriggerItem {
	public TrionArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
	}

	@Override
	public ItemStack equip(ItemStack previous, TriggerConfig config) {
		int color = config.getColor(this.slot);
		ItemStack equipped = TriggerItem.super.equip(previous, config);
		CompoundTag tag = equipped.getOrCreateTag();
		tag.putBoolean("Unbreakable", true); //so armor doesn't get damaged, since ArmorItem overrides that
		equipped.addEnchantment(Enchantments.BINDING_CURSE, 1); //since equipment slots override canRemove
		tag.putInt("HideFlags", 0b00000101); //hide curse of binding, hide unbreakable
		CompoundTag displayTag = equipped.getOrCreateSubTag("display");
		displayTag.putString("Texture", config.getTextureId(slot).toString());
		this.setColor(equipped, color);
		return equipped;
	}

	public static ItemStack getTrionStack(EquipmentSlot slot, ItemStack previous, TriggerConfig config) {
		TriggerItem item;
		switch(slot) {
			case HEAD:
				item = (TriggerItem) TrionItems.TRION_HELMET;
				break;
			case CHEST:
				item = (TriggerItem) TrionItems.TRION_CHESTPLATE;
				break;
			case LEGS:
				item = (TriggerItem) TrionItems.TRION_LEGGINGS;
				break;
			case FEET:
				item = (TriggerItem) TrionItems.TRION_BOOTS;
				break;
			default:
				return previous;
		}
		return item.equip(previous, config);
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return false;
	}
}
