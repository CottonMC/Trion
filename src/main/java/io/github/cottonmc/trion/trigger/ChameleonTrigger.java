package io.github.cottonmc.trion.trigger;

import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ChameleonTrigger implements Trigger {
	@Override
	public void tick(TrionComponent component) {
		PlayerEntity player = (PlayerEntity)component.getEntity();
//		if (player.world.getTime() % 5 == 0) {
			component.setTrion(component.getTrion() - 1, true);
//		}
	}

	@Override
	public ItemStack getStack(TrionComponent component) {
		return ItemStack.EMPTY;
	}
}
