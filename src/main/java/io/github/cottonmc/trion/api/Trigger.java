package io.github.cottonmc.trion.api;

import io.github.cottonmc.trion.Trion;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public interface Trigger {

	Trigger EMPTY = Registry.register(Trion.TRIGGERS, new Identifier(Trion.MODID, "empty"), new Trigger() {
		@Override
		public void tick(TrionComponent component) { }

		@Override
		public TriggerItem getItem() {
			return TriggerItem.NONE;
		}
	});

	/**
	 * Called every tick that a trion component has this trigger equipped and is active.
	 * @param component The component this trigger is active in.
	 */
	void tick(TrionComponent component);


	/**
	 * Used during trigger activation to equip a player.
	 * @return The item form of this Trigger, or {@link TriggerItem#NONE} if it doesn't have an item and is purely passive.
	 */
	TriggerItem getItem();

	/**
	 * @return The translation key for this trigger.
	 */
	default String getTranslationKey() {
		if (getItem() instanceof Item) {
			return ((Item)getItem()).getTranslationKey();
		}
		Identifier id = Trion.TRIGGERS.getId(this);
		return "trigger." + id.getNamespace() + "." + id.getPath();
	}

	/**
	 * @return The text component for this trigger's name, used in GUIs.
	 */
	default Text getName() {
		return new TranslatableText(getTranslationKey());
	}

	/**
	 * @return The text component for this trigger's description, used in GUIs.
	 */
	default Text getDescription() {
		return new TranslatableText("desc." + getTranslationKey());
	}
}
