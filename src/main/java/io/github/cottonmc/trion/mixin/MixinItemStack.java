package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TriggerShifter;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.item.TrionArmorItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
	@Shadow public abstract Item getItem();

	//TODO: something I can do to make this happen in non-player inventories too?
	@Inject(method = "inventoryTick", at = @At("HEAD"))
	private void injectInvTick(World world, Entity entity, int slot, boolean selected, CallbackInfo info) {
		if (getItem() instanceof TriggerShifter && !(getItem() instanceof TrionArmorItem)) {
			if (Trion.TRION_COMPONENT.maybeGet(entity).isPresent()) {
				TrionComponent comp = Trion.TRION_COMPONENT.get(entity);
				if (!comp.isTriggerActive()) {
					((PlayerEntity) entity).inventory.setInvStack(slot, ((TriggerShifter) getItem()).unequip((ItemStack)(Object)this));
				}
			}
		}
	}
}
