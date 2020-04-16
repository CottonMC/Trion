package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.api.TriggerShifter;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {
	@Shadow public abstract ItemStack getStack();

	@Shadow public abstract void setStack(ItemStack stack);

	@Inject(method = "tick", at = @At("HEAD"))
	private void injectRevert(CallbackInfo info) {
		if (getStack().getItem() instanceof TriggerShifter) {
			TriggerShifter shifter = (TriggerShifter) getStack().getItem();
			setStack(shifter.unequip(getStack()));
		}
	}
}
