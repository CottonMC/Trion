package io.github.cottonmc.trion.impl;

import com.google.common.collect.ImmutableList;
import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TriggerConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerConfigImpl implements TriggerConfig {
	private Map<EquipmentSlot, Identifier> textures = new HashMap<>();
	private Object2IntMap<EquipmentSlot> colors = new Object2IntOpenHashMap<>();
	private List<Trigger> equippedTriggers = new ArrayList<>();

	public TriggerConfigImpl() { }

	@Override
	public Identifier getTextureId(EquipmentSlot slot) {
		if (textures.containsKey(slot)) return textures.get(slot);
		return new Identifier(Trion.MODID, "strategist_shaded"); //Tamakoma-2 texture
	}

	public void setTextureId(EquipmentSlot slot, Identifier id) {
		textures.put(slot, id);
	}

	@Override
	public int getColor(EquipmentSlot slot) {
		if (colors.containsKey(slot)) return colors.getInt(slot);
		return 0x388e9a; //Tamakoma-2 color
	}

	public void setColor(EquipmentSlot slot, int color) {
		colors.put(slot, color);
	}

	@Override
	public ImmutableList<Trigger> getEquippedTriggers() {
		return ImmutableList.copyOf(equippedTriggers);
	}

	public void setEquippedTriggers(List<Trigger> triggerList) {
		this.equippedTriggers = triggerList;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		textures.clear();
		colors.clear();
		equippedTriggers.clear();
		CompoundTag texTag = tag.getCompound("Textures");
		for (String key : texTag.getKeys()) {
			EquipmentSlot slot = EquipmentSlot.byName(key);
			textures.put(slot, new Identifier(texTag.getString(key)));
		}
		CompoundTag colorTag = tag.getCompound("Colors");
		for (String key : colorTag.getKeys()) {
			EquipmentSlot slot = EquipmentSlot.byName(key);
			colors.put(slot, colorTag.getInt(key));
		}
		List<Trigger> triggerList = new ArrayList<>();
		ListTag triggerTag = tag.getList("EquippedTriggers", NbtType.STRING);
		for (Tag trigger : triggerTag) {
			triggerList.add(Trion.TRIGGERS.get(new Identifier(trigger.asString())));
		}
		this.equippedTriggers = triggerList;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag texTag = new CompoundTag();
		for (EquipmentSlot slot : textures.keySet()) {
			texTag.putString(slot.getName(), textures.get(slot).toString());
		}
		tag.put("Textures", texTag);
		CompoundTag colorTag = new CompoundTag();
		for (EquipmentSlot slot : colors.keySet()) {
			texTag.putInt(slot.getName(), colors.getInt(slot));
		}
		tag.put("Colors", colorTag);
		ListTag triggerTag = new ListTag();
		for (Trigger trigger : equippedTriggers) {
			triggerTag.add(StringTag.of(Trion.TRIGGERS.getId(trigger).toString()));
		}
		tag.put("EquippedTriggers", triggerTag);
		return tag;
	}
}
