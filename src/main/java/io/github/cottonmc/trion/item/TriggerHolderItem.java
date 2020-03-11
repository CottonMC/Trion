package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TriggerHolderItem extends Item {
	public TriggerHolderItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (world.isClient) return TypedActionResult.success(stack);
		TrionComponent component = Trion.TRION_COMPONENT.get(user);
		if (component.isTriggerActive()) {
			component.deactivateTrigger();
			user.getItemCooldownManager().set(this, 60);
		} else {
			component.activateTrigger();
			user.getItemCooldownManager().set(this, 30);
		}
		return TypedActionResult.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (entity instanceof PlayerEntity && !world.isClient) {
			PlayerEntity player = (PlayerEntity)entity;
			TrionComponent component = Trion.TRION_COMPONENT.get(player);
			if (component.isTriggerActive() && player.getItemCooldownManager().isCoolingDown(this)) {
			}
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}
