package io.github.cottonmc.trion.api;

import io.github.cottonmc.trion.Trion;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public interface Trigger {

	Trigger EMPTY = Registry.register(Trion.TRIGGERS, new Identifier(Trion.MODID, "empty"), new Trigger() {
		@Override
		public void tick(TrionComponent component) { }

		@Override
		public TriggerItem getShifter() {
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
	 * @return The shifter used to convert previous equipment to Trion equipment.
	 */
	TriggerItem getShifter();

	/**
	 * @return The translation key for this trigger.
	 */
	default String getTranslationKey() {
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
