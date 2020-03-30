package io.github.cottonmc.trion.trigger;

import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class BailOutTrigger implements Trigger {

	@Override
	public void tick(TrionComponent component) {
		float percent = (float) component.getTrion() / (float) component.getMaxTrion();
		if (percent < .05) {
			PlayerEntity player = (PlayerEntity)component.getEntity();
			BlockPos spawnPos = player.getSpawnPosition();
			if (spawnPos == null) spawnPos = player.getEntityWorld().getSpawnPos();
			if (player.getBlockPos().isWithinDistance(spawnPos, 2000)) {
				//TODO: process for bailing out instead? That might take a rework on how trion components work though..
				player.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
				component.deactivateTrigger();
			}
		}
	}

	@Override
	public ItemStack getStack(TrionComponent component) {
		return ItemStack.EMPTY; //TODO: manual bail out
	}

}