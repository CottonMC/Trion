package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.hooks.DynamicArmorMaterial;
import io.github.cottonmc.trion.item.BailOutItem;
import io.github.cottonmc.trion.item.TriggerHolderItem;
import io.github.cottonmc.trion.item.TrionArmorItem;
import io.github.cottonmc.trion.item.TrionDebugItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TrionItems {
	public static final ArmorMaterial TRION_ARMOR = new DynamicArmorMaterial(1, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, Ingredient.EMPTY, 0f, new Identifier(Trion.MODID, "strategist_shaded"));

	public static final Item TRIGGER_HOLDER = register(new TriggerHolderItem(defaultSettings()), "trigger_holder");
	public static final Item TRION_HELMET = register(new TrionArmorItem(TRION_ARMOR, EquipmentSlot.HEAD, defaultSettings()), "trion_helmet");
	public static final Item TRION_CHESTPLATE = register(new TrionArmorItem(TRION_ARMOR, EquipmentSlot.CHEST, defaultSettings()), "trion_chestplate");
	public static final Item TRION_LEGGINGS = register(new TrionArmorItem(TRION_ARMOR, EquipmentSlot.LEGS, defaultSettings()), "trion_leggings");
	public static final Item TRION_BOOTS = register(new TrionArmorItem(TRION_ARMOR, EquipmentSlot.FEET, defaultSettings()), "trion_boots");
	public static final Item DEBUG_50 = register(new TrionDebugItem(50, defaultSettings()), "debug_50");
	public static final Item DEBUG_200 = register(new TrionDebugItem(200, defaultSettings()), "debug_200");
	public static final Item DEBUG_1900 = register(new TrionDebugItem(1900, defaultSettings()), "debug_1900");
	public static final Item BAIL_OUT = register(new BailOutItem(defaultSettings()), "bail_out");

	public static void init() { }

	private static Item register(Item item, String name) {
		return Registry.register(Registry.ITEM, new Identifier(Trion.MODID, name), item);
	}

	private static Item.Settings defaultSettings() {
		return new Item.Settings().group(Trion.TRION_GROUP);
	}
}
