package io.github.cottonmc.trion.trigger;

import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TriggerItem;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.registry.TrionItems;

public class RaygustTrigger implements Trigger {
	@Override
	public void tick(TrionComponent component) {

	}

	@Override
	public TriggerItem getItem() {
		return (TriggerItem) TrionItems.RAYGUST;
	}
}
