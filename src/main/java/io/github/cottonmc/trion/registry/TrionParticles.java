package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.hooks.CustomParticleType;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TrionParticles {

	public static final DefaultParticleType TRANSFORMATION = register(true, "transformation");

	public static void init() { }

	public static DefaultParticleType register(boolean alwaysShow, String name) {
		return Registry.register(Registry.PARTICLE_TYPE, new Identifier(Trion.MODID, name), new CustomParticleType(alwaysShow));
	}
}
