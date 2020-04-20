package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TriggerItem;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BailOutItem extends Item implements TriggerItem {
	public BailOutItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (world.isClient) return TypedActionResult.success(stack);
		if (Trion.TRION_COMPONENT.get(user).isTriggerActive()) {
			user.setCurrentHand(hand);
			return TypedActionResult.success(stack);
		}
		return TypedActionResult.pass(stack);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 60;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (world.isClient) return stack;
		if (user instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) user;
			TrionComponent component = Trion.TRION_COMPONENT.get(player);
			BlockPos spawnPos = player.getSpawnPosition();
			if (spawnPos == null) spawnPos = player.getEntityWorld().getSpawnPos();
			if (player.getBlockPos().isWithinDistance(spawnPos, 2000)) {
				//TODO: process for bailing out instead? That might take a rework on how trion components work though..
				player.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
				component.deactivateTrigger();
			}
		}
		return stack;
	}
}
