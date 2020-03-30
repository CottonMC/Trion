package io.github.cottonmc.trion.api;

import io.github.cottonmc.trion.Trion;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public interface Trigger {

	Trigger EMPTY = Registry.register(Trion.TRIGGERS, new Identifier(Trion.MODID, "empty"), new Trigger() {
		@Override
		public void tick(TrionComponent component) { }

		@Override
		public ItemStack getStack(TrionComponent component) {
			return ItemStack.EMPTY;
		}
	});

	/**
	 * Called every tick that a trion component has this trigger equipped and is active.
	 * @param component The component this trigger is active in.
	 */
	void tick(TrionComponent component);

	/**
	 * Called on trigger activation to equip a player with their weapons.
	 * @param component The component this trigger is active in.
	 * @return The stack this trigger gives to the player.
	 */
	ItemStack getStack(TrionComponent component);

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