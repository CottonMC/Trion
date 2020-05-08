package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.trigger.BailOutTrigger;
import io.github.cottonmc.trion.trigger.ChameleonTrigger;
import io.github.cottonmc.trion.trigger.SimpleTrigger;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TrionTriggers {
	public static final Trigger BAIL_OUT = register(new BailOutTrigger(), "bail_out");
	public static final Trigger CHAMELEON = register(new ChameleonTrigger(), "chameleon");
	public static final Trigger RAYGUST = register(new SimpleTrigger(TrionItems.RAYGUST), "raygust");
	public static final Trigger KOGETSU = register(new SimpleTrigger(TrionItems.KOGETSU), "kogetsu");
	public static final Trigger SCORPION = register(new SimpleTrigger(TrionItems.SCORPION), "scorpion");
	public static final Trigger SHIELD = register(new SimpleTrigger(TrionItems.TRION_SHIELD), "shield");

	public static void init() { }

	private static Trigger register(Trigger trigger, String name) {
		return Registry.register(Trion.TRIGGERS, new Identifier(Trion.MODID, name), trigger);
	}
}
