package io.github.cottonmc.trion.hooks;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class CustomToolMaterial implements ToolMaterial {
	private int itemDurability;
	private float miningSpeed;
	private float attackDamage;
	private int miningLevel;
	private int enchantability;
	private Lazy<Ingredient> repairIngredient;

	public CustomToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantibility, Supplier<Ingredient> repairIngredient) {
		this.miningLevel = miningLevel;
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantibility;
		this.repairIngredient = new Lazy(repairIngredient);
	}

	@Override
	public int getMiningLevel() {
		return miningLevel;
	}

	@Override
	public int getDurability() {
		return itemDurability;
	}

	@Override
	public float getMiningSpeed() {
		return miningSpeed;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}
}
