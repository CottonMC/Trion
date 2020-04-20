package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.api.TriggerItem;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class MixinSlot {
	@Shadow public abstract ItemStack getStack();

	//TODO: will this totally work? Spinnery's definitely gonna fuck with this...
	@Inject(method = "canTakeItems", at = @At("HEAD"), cancellable = true)
	private void blockTriggerRemoval(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		if (getStack().getItem() instanceof TriggerItem && !player.isCreative()) info.setReturnValue(false);
	}
}
