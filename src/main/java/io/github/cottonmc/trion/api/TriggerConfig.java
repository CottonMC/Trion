package io.github.cottonmc.trion.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

//TODO: documentation
public interface TriggerConfig {
	Identifier getTextureId(EquipmentSlot slot);
	int getColor(EquipmentSlot slot);
	ImmutableList<Trigger> getEquippedTriggers();
	void fromTag(CompoundTag tag);
	CompoundTag toTag(CompoundTag tag);
}
