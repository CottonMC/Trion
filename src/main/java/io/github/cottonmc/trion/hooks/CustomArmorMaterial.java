package io.github.cottonmc.trion.hooks;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomArmorMaterial implements ArmorMaterial {
	private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

	private int durabilityMultiplier;
	private int[] protectionAmounts;
	private int enchantability;
	private SoundEvent equipSound;
	private Ingredient repairIngredient;
	private float toughness;
	private Identifier id;

	public CustomArmorMaterial(int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, Ingredient repairIngredient, float toughness, Identifier id) {
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.repairIngredient = repairIngredient;
		this.toughness = toughness;
		this.id = id;
	}

	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return this.protectionAmounts[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient;
	}

	@Override
	public String getName() {
		return id.getPath();
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	public Identifier getId() {
		return id;
	}
}
