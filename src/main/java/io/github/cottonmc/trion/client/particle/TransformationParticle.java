package io.github.cottonmc.trion.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;

public class TransformationParticle extends AnimatedParticle {
	public TransformationParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider, 0.05F);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.scale *= 0.75F;
		this.maxAge = 60 + this.random.nextInt(12);
		this.setSpriteForAge(spriteProvider);
		if (this.random.nextInt(4) == 0) {
			this.setColor(0.37F + this.random.nextFloat() * 0.2F, 0.93F + this.random.nextFloat() * 0.3F, 0.58F + this.random.nextFloat() * 0.2F);
		} else {
			this.setColor(0.33F + this.random.nextFloat() * 0.2F, 0.87F + this.random.nextFloat() * 0.3F, 0.6F + this.random.nextFloat() * 0.2F);
		}

		this.setResistance(0.6F);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new TransformationParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
