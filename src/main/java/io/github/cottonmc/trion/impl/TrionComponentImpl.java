package io.github.cottonmc.trion.impl;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.item.TrionArmorItem;
import io.github.cottonmc.trion.registry.TrionParticles;
import io.github.cottonmc.trion.registry.TrionSounds;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

public class TrionComponentImpl implements TrionComponent {
	private PlayerEntity player;
	private boolean triggerActive = false;
	private int trion = 200;
	private int maxTrion = 200;

	private boolean activating = false;
	private int activationTime = 0;
	private final int maxActivationTime = 30;

	public TrionComponentImpl(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public boolean isTriggerActive() {
		return triggerActive;
	}

	@Override
	public void tick() {
		if (activating) {
			if (activationTime < maxActivationTime) {
				if (!player.world.isClient) {
					((ServerWorld) player.world).spawnParticles(TrionParticles.TRANSFORMATION, player.getX(), player.getY(), player.getZ(), 25, 0.0F, 0.0F, 0.0F, 0.25F);
				}
				activationTime++;
			} else {
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					player.equipStack(slot, TrionArmorItem.getTrionStack(slot, player.getEquippedStack(slot), 0x5FEC94));
				}
				player.world.playSound(player, player.getBlockPos(), TrionSounds.TRANSFORM_ON, SoundCategory.PLAYERS, 1f, 1f);
				activating = false;
				triggerActive = true;
				activationTime = 0;
			}
			sync();
		} else {
			if (!isTriggerActive() && getTrion() < getMaxTrion() && getEntity().world.getTime() % 2 == 0) {
				setTrion(getTrion() + 1);
				sync();
			}
			if (isTriggerActive() && getEntity().world.getTime() % 5 == 0) {
				setTrion(getTrion() - 1);
				if (getTrion() == 0) {
					deactivateTrigger();
				}
				sync();
			}
		}
	}

	@Override
	public void activateTrigger() {
		activating = true;
		if (!player.world.isClient) {
			((ServerWorld)player.world).spawnParticles(TrionParticles.TRANSFORMATION, player.getX(), player.getY(), player.getZ(), 100, 0.0F, 0.0F, 0.0F, 0.25F);
		}
		sync();
	}

	@Override
	public void deactivateTrigger() {
		triggerActive = false;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			player.equipStack(slot, TrionArmorItem.getOriginalStack(player.getEquippedStack(slot)));
		}
		player.world.playSound(player, player.getBlockPos(), TrionSounds.TRANSFORM_OFF, SoundCategory.PLAYERS, 1f, 1f);
		sync();
	}

	@Override
	public int getTrion() {
		return trion;
	}

	@Override
	public void setTrion(int trion) {
		this.trion = trion;
		sync();
	}

	@Override
	public int getMaxTrion() {
		return maxTrion;
	}

	@Override
	public void setMaxTrion(int maxTrion) {
		this.maxTrion = maxTrion;
		sync();
	}

	@Override
	public Entity getEntity() {
		return player;
	}

	@Override
	public ComponentType<?> getComponentType() {
		return Trion.TRION_COMPONENT;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		triggerActive = tag.getBoolean("TriggerActive");
		trion = tag.getInt("Trion");
		maxTrion = tag.getInt("MaxTrion");
		activating = tag.getBoolean("Activating");
		activationTime = tag.getInt("ActivationTime");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putBoolean("TriggerActive", triggerActive);
		tag.putInt("Trion", trion);
		tag.putInt("MaxTrion", maxTrion);
		tag.putBoolean("Activating", activating);
		tag.putInt("ActivationTime", activationTime);
		return tag;
	}
}
