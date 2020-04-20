package io.github.cottonmc.trion.trigger;

import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TriggerItem;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class ChameleonTrigger implements Trigger {
	@Override
	public void tick(TrionComponent component) {
		PlayerEntity player = (PlayerEntity)component.getEntity();
		if (player.world.getTime() % 5 == 0
			&& player.getStackInHand(Hand.MAIN_HAND).isEmpty()
			&& player.getStackInHand(Hand.OFF_HAND).isEmpty()) {
			component.setTrion(component.getTrion() - 1, true);
		}
	}

	@Override
	public TriggerItem getItem() {
		return TriggerItem.NONE;
	}
}
