package io.github.cottonmc.trion.item;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface TrionShield {
	default int getShieldDamage(PlayerEntity wielder, ItemStack stack) {
		if (stack.getOrCreateTag().contains("ShieldDamage", NbtType.INT)) {
			return stack.getOrCreateTag().getInt("ShieldDamage");
		}
		return 0;
	}

	int getMaxShieldDamage(PlayerEntity wielder, ItemStack stack);

	default void setShieldDamage(PlayerEntity wielder, ItemStack stack, int value) {
		stack.getOrCreateTag().putInt("ShieldDamage", Math.min(value, getMaxShieldDamage(wielder, stack)));
	}

	default void damageShield(PlayerEntity wielder, ItemStack stack, int amount) {
		setShieldDamage(wielder, stack, getShieldDamage(wielder, stack) + amount);
	}

	void tickShield(PlayerEntity wielder, ItemStack stack);

	int getColor(PlayerEntity wielder, ItemStack stack);
}
