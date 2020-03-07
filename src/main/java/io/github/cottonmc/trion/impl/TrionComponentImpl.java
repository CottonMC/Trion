package io.github.cottonmc.trion.impl;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class TrionComponentImpl implements TrionComponent {
	private PlayerEntity player;
	private boolean triggerActive = false;
	private int trion = 0;

	public TrionComponentImpl(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public boolean isTriggerActive() {
		return triggerActive;
	}

	@Override
	public void activateTrigger() {
		triggerActive = true;
		sync();
	}

	@Override
	public void deactivateTrigger() {
		triggerActive = false;
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
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putBoolean("TriggerActive", triggerActive);
		tag.putInt("Trion", trion);
		return tag;
	}
}
