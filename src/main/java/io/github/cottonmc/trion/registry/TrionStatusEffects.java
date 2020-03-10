package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.status.CustomStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TrionStatusEffects {
	public static final StatusEffect VIRTUAL_COMBAT = register(new CustomStatusEffect(StatusEffectType.NEUTRAL, 0x5FD3EC), "virtual_combat");
	public static final StatusEffect CHAMELEON = register(new CustomStatusEffect(StatusEffectType.BENEFICIAL, 0xFFFFFF), "chameleon"); //TODO: color, should this be something other than a status effect?

	public static void init() { }

	private static StatusEffect register(StatusEffect effect, String name) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(Trion.MODID, name), effect);
	}
}
