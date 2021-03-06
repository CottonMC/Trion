package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.Trion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TrionSounds {

	public static final SoundEvent TRANSFORMATION_ON = register("transformation.on");
	public static final SoundEvent TRANSFORMATION_OFF = register("transformation.off");
	public static final SoundEvent TRANSFORMATION_CHARGE = register("transformation.charge");

	public static void init() { }

	private static SoundEvent register(String name) {
		return Registry.register(Registry.SOUND_EVENT, new Identifier(Trion.MODID, name), new SoundEvent(new Identifier(Trion.MODID, name)));
	}
}
