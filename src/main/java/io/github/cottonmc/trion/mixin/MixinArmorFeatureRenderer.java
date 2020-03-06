package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.item.CustomArmorMaterial;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ArmorFeatureRenderer.class)
public class MixinArmorFeatureRenderer {
	@Shadow
	@Final
	private static Map<String, Identifier> ARMOR_TEXTURE_CACHE;

	@Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
	private void injectArmorId(ArmorItem armorItem, boolean lowerParts, String suffix, CallbackInfoReturnable<Identifier> info) {
		if (armorItem.getMaterial() instanceof CustomArmorMaterial) {
			Identifier id = ((CustomArmorMaterial)armorItem.getMaterial()).getId();
			Identifier texId = new Identifier(id.getNamespace(), "textures/models/armor/" + id.getPath() + "_layer_" + (lowerParts ? 2 : 1) + (suffix == null ? "" : "_" + suffix) + ".png");
			info.setReturnValue(ARMOR_TEXTURE_CACHE.computeIfAbsent(texId.toString(), string -> texId));
		}
	}
}
