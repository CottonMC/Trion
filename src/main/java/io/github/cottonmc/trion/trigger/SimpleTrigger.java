package io.github.cottonmc.trion.trigger;

import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TriggerItem;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.item.Item;

public class SimpleTrigger implements Trigger {
	private final Item item;

	public SimpleTrigger(Item item) {
		this.item = item;
	}

	@Override
	public void tick(TrionComponent component) {

	}

	@Override
	public TriggerItem getItem() {
		return (TriggerItem)item;
	}
}
