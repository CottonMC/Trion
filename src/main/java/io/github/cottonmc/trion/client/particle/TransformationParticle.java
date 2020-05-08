package io.github.cottonmc.trion.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;

public class TransformationParticle extends AnimatedParticle {
	public TransformationParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider, 0.02f);
		this.velocityX = velocityX / 2;
		this.velocityY = velocityY / 2;
		this.velocityZ = velocityZ / 2;
		this.scale *= 0.75f;
		this.maxAge = 30 + this.random.nextInt(12);
		this.collidesWithWorld = false;
		this.setSpriteForAge(spriteProvider);
		int colorIndex = this.random.nextInt(36);
		if (colorIndex == 0) {
			//green - 0x51a655
			this.setColor(Math.min(1f, 0.32f - this.random.nextFloat() * (0.1f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.65f + this.random.nextFloat() * (0.15f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.33f + this.random.nextFloat() * (0.1f * (random.nextBoolean()? 1f : -1f))));
		} else if (colorIndex < 4) {
			//yellow - 0xd9b266
			this.setColor(Math.min(1f, 0.85f - this.random.nextFloat() * (0.15f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.7f + this.random.nextFloat() * (0.15f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.2f + this.random.nextFloat() * (0.1f * (random.nextBoolean()? 1f : -1f))));
		} else if (colorIndex < 20) {
			//blue - 0x5ba8cf
			this.setColor(Math.min(1f, 0.36f + this.random.nextFloat() * (0.1f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.66f + this.random.nextFloat() * (0.1f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.81f + this.random.nextFloat() * (0.15f * (random.nextBoolean()? 1f : -1f))));
		} else {
			//purple - 0x7557bd
			this.setColor(Math.min(1f, 0.46f + this.random.nextFloat() * (0.15f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.34f + this.random.nextFloat() * (0.1f * (random.nextBoolean()? 1f : -1f))), Math.min(1f, 0.74f + this.random.nextFloat() * (0.15f * (random.nextBoolean()? 1f : -1f))));
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
