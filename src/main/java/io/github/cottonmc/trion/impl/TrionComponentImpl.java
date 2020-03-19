package io.github.cottonmc.trion.impl;

import com.google.common.collect.ImmutableList;
import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.item.TrionArmorItem;
import io.github.cottonmc.trion.registry.TrionParticles;
import io.github.cottonmc.trion.registry.TrionSounds;
import io.github.cottonmc.trion.registry.TrionStatusEffects;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TrionComponentImpl implements TrionComponent {
	//core information
	private PlayerEntity player;
	private boolean triggerActive = false;
	private int trion = 200;
	private int maxTrion = 200;
	private int virtualTrion = 200;
	private int lastVirtualTrion = 200;
	private List<Trigger> equippedTriggers = new ArrayList<>();

	//set-up
	private boolean activating = false;
	private int activationTime = 0;
	private final int maxActivationTime = 30;

	//cooldowns
	private int virtualTrionCooldown = 0;
	private final int maxVirtualTrionCooldown = 400;
	private boolean burst = false;
	private int burstCooldown = 0;
	private final int maxBurstCooldown = 200;

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
				((ServerWorld) player.world).spawnParticles(TrionParticles.TRANSFORMATION, player.getX(), player.getY(), player.getZ(), 25, 0.0F, 0.0F, 0.0F, 0.25F);
				activationTime++;
			} else {
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					player.equipStack(slot, TrionArmorItem.getTrionStack(slot, player.getEquippedStack(slot), 0x5FEC94));
				}
				player.world.playSound(null, player.getBlockPos(), TrionSounds.TRANSFORMATION_ON, SoundCategory.PLAYERS, .8f, 1f);
				activating = false;
				triggerActive = true;
				activationTime = 0;
			}
			sync();
		} else {
			if (!player.hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT)) {
				if (!isTriggerActive()) {
					if (burst) {
						if (getTrion() < getMaxTrion()) {
							if (burstCooldown < maxBurstCooldown) {
								burstCooldown++;
								sync();
							} else {
								if (getEntity().world.getTime() % 3 == 0) {
									setTrion(getTrion() + 1);
								}
							}
						} else {
							burst = false;
							sync();
						}
					} else if (getTrion() < getMaxTrion() && getEntity().world.getTime() % 2 == 0) {
						setTrion(getTrion() + 1);
					}
				}
				if (isTriggerActive()) {
					if (getEntity().world.getTime() % 50 == 0) {
						setTrion(getTrion() - 1, true);
					}
					for (Trigger trigger : getEquippedTriggers()) {
						trigger.tick(this);
					}
				}
			} else {
				if (virtualTrion != lastVirtualTrion) {
					virtualTrionCooldown = 0;
					lastVirtualTrion = virtualTrion;
				} else {
					virtualTrionCooldown++;
					if (virtualTrionCooldown >= maxVirtualTrionCooldown) {
						virtualTrion = maxTrion;
						virtualTrionCooldown = 0;
					}
				}
				sync();
			}
		}
	}

	@Override
	public void activateTrigger(List<Trigger> equippedTriggers) {
		if (burst) return; //TODO: sound effect
		activating = true;
		this.equippedTriggers = equippedTriggers;
		if (!player.world.isClient) {
			((ServerWorld)player.world).spawnParticles(TrionParticles.TRANSFORMATION, player.getX(), player.getY(), player.getZ(), 100, 0.0F, 0.0F, 0.0F, 0.25F);
			player.world.playSound(null, player.getBlockPos(), TrionSounds.TRANSFORMATION_CHARGE, SoundCategory.PLAYERS, 1f, 1f);
		}
		sync();
	}

	@Override
	public void deactivateTrigger() {
		if (player.hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT)) return; //TODO: keep this?
		triggerActive = false;
		equippedTriggers.clear();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			player.equipStack(slot, TrionArmorItem.getOriginalStack(player.getEquippedStack(slot)));
		}
		player.world.playSound(null, player.getBlockPos(), TrionSounds.TRANSFORMATION_OFF, SoundCategory.PLAYERS, .8f, 1f);
		sync();
	}

	@Override
	public ImmutableList<Trigger> getEquippedTriggers() {
		return ImmutableList.copyOf(equippedTriggers);
	}

	@Override
	public boolean isBurst() {
		return burst;
	}

	@Override
	public int getTrion() {
		if (!player.hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT)) {
			return trion;
		} else {
			return virtualTrion;
		}
	}

	@Override
	public void setTrion(int trion, boolean realOnly) {
		if (!player.hasStatusEffect(TrionStatusEffects.VIRTUAL_COMBAT)) {
			this.trion = Math.max(0, trion);
			if (this.trion == 0) {
				deactivateTrigger();
				burst = true;
			}
		} else if (!realOnly) {
			this.virtualTrion = Math.max(0, virtualTrion);
			if (this.virtualTrion == 0) {
				//TODO: proper effect for defeat in virtual combat
				if (!player.world.isClient) {
					((ServerWorld) player.world).spawnParticles(TrionParticles.TRANSFORMATION, player.getX(), player.getY(), player.getZ(), 25, 0.0F, 0.0F, 0.0F, 0.25F);
				}
			}
		}
		sync();
	}

	@Override
	public int getMaxTrion() {
		return maxTrion;
	}

	@Override
	public void setMaxTrion(int maxTrion) {
		this.maxTrion = maxTrion;
		this.trion = Math.min(trion, maxTrion);
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
		burst = tag.getBoolean("Burst");
		trion = tag.getInt("Trion");
		maxTrion = tag.getInt("MaxTrion");
		activating = tag.getBoolean("Activating");
		activationTime = tag.getInt("ActivationTime");
		virtualTrion = tag.getInt("VirtualTrion");
		virtualTrionCooldown = tag.getInt("VirtualTrionCooldown");
		List<Trigger> triggerList = new ArrayList<>();
		ListTag triggerTag = tag.getList("EquippedTriggers", NbtType.STRING);
		for (Tag trigger : triggerTag) {
			triggerList.add(Trion.TRIGGERS.get(new Identifier(trigger.asString())));
		}
		this.equippedTriggers = triggerList;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putBoolean("TriggerActive", triggerActive);
		tag.putBoolean("Burst", burst);
		tag.putInt("Trion", trion);
		tag.putInt("MaxTrion", maxTrion);
		tag.putBoolean("Activating", activating);
		tag.putInt("ActivationTime", activationTime);
		tag.putInt("VirtualTrion", virtualTrion);
		tag.putInt("VirtualTrionCooldown", virtualTrionCooldown);
		ListTag triggerList = new ListTag();
		for (Trigger trigger : equippedTriggers) {
			triggerList.add(StringTag.of(Trion.TRIGGERS.getId(trigger).toString()));
		}
		tag.put("EquippedTriggers", triggerList);
		return tag;
	}
}
