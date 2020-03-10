package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TrionDebugItem extends Item {
	private final int maxToSet;

	public TrionDebugItem(int maxToSet, Settings settings) {
		super(settings);
		this.maxToSet = maxToSet;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand));
		TrionComponent comp = Trion.TRION_COMPONENT.get(user);
		comp.setMaxTrion(maxToSet);
		return TypedActionResult.success(user.getStackInHand(hand));
	}
}
