package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TriggerConfig;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.impl.TriggerConfigImpl;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TriggerHolderItem extends Item {
	public TriggerHolderItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (world.isClient) return TypedActionResult.success(stack);
		TrionComponent component = Trion.TRION_COMPONENT.get(user);
		if (component.isTriggerActive()) {
			component.deactivateTrigger();
			user.getItemCooldownManager().set(this, 60);
		} else {
			component.activateTrigger(getConfig(stack));
			user.getItemCooldownManager().set(this, 30);
		}
		return TypedActionResult.success(stack);
	}

	private TriggerConfig getConfig(ItemStack stack) {
		TriggerConfig ret = new TriggerConfigImpl();
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("TriggerConfig", NbtType.COMPOUND)) {
			ret.fromTag(tag.getCompound("TriggerConfig"));
		}
		return ret;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		TriggerConfig config = getConfig(stack);
		for (Trigger trigger : config.getEquippedTriggers()) {
			tooltip.add(new LiteralText("").append(new TranslatableText(trigger.getTranslationKey())).formatted(Formatting.GRAY));
		}
	}
}
