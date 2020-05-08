package io.github.cottonmc.trion.item;

import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.github.cottonmc.trion.api.TriggerConfig;
import io.github.cottonmc.trion.api.TriggerItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

/**
 * Trion sword item which has an optional range boost.
 */
public class TrionSwordItem extends SwordItem implements TriggerItem {
	private float rangeBoost;
	private static final UUID REACH_UUID = UUID.fromString("ad6b3b12-6647-49a2-bff4-b234ea3ea532");
	private static final UUID ATTACK_RANGE_UUID = UUID.fromString("43e6b5f0-2a29-4d9f-95ae-bacf99ee9320");

	public TrionSwordItem(ToolMaterial material, int attackDamage, float attackSpeed, float rangeBoost, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
		this.rangeBoost = rangeBoost;
	}

	@Override
	public ItemStack equip(ItemStack previous, TriggerConfig config) {
		ItemStack equipped = TriggerItem.super.equip(previous, config);
		CompoundTag tag = equipped.getOrCreateTag();
		tag.putBoolean("Unbreakable", true); //so trion swords don't get damaged, since ToolItem overrides that setting
		tag.putInt("HideFlags", 0b00000001); // hide unbreakable
		return equipped;
	}

	@Override
	public Multimap<String, EntityAttributeModifier> getModifiers(EquipmentSlot slot) {
		Multimap<String, EntityAttributeModifier> ret = super.getModifiers(slot);
		if (slot == EquipmentSlot.MAINHAND && rangeBoost != 0) {
			ret.put(ReachEntityAttributes.REACH.getId(), new EntityAttributeModifier(REACH_UUID, "Reach modifier", rangeBoost, EntityAttributeModifier.Operation.ADDITION));
			ret.put(ReachEntityAttributes.ATTACK_RANGE.getId(), new EntityAttributeModifier(ATTACK_RANGE_UUID, "Attack range modifier", rangeBoost, EntityAttributeModifier.Operation.ADDITION));
		}
		return ret;
	}
}
