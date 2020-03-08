package io.github.cottonmc.trion;

import io.github.cottonmc.trion.client.hud.TrionBarHud;
import io.github.cottonmc.trion.client.particle.TransformationParticle;
import io.github.cottonmc.trion.registry.TrionParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class TrionClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(TrionParticles.TRANSFORMATION, TransformationParticle.Factory::new);
		HudRenderCallback.EVENT.register(TrionBarHud::render);
	}

}
