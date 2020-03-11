package io.github.cottonmc.trion.hooks;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class CustomStatusEffect extends StatusEffect {
	public CustomStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
}
