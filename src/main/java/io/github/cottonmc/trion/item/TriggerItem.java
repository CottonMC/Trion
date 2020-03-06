package io.github.cottonmc.trion.item;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TriggerItem extends Item {
	public TriggerItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (world.isClient) return TypedActionResult.success(stack);
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Active")) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				user.equipStack(slot, TrionArmorItem.getOriginalStack(user.getEquippedStack(slot)));
			}
			tag.remove("Active");
		} else {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				user.equipStack(slot, TrionArmorItem.getTrionStack(slot, user.getEquippedStack(slot), 0x5FEC94));
			}
			tag.putBoolean("Active", true);
			((ServerWorld)world).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, user.getX(), user.getY() + 1.5D, user.getZ(), 100, 0.0F, 0.0F, 0.0F, 0.5F);
		}
		return TypedActionResult.success(stack);
	}
}
