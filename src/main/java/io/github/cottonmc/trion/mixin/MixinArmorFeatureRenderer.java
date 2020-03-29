package io.github.cottonmc.trion.mixin;

import io.github.cottonmc.trion.hooks.DynamicArmorMaterial;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(ArmorFeatureRenderer.class)
public abstract class MixinArmorFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
	@Shadow
	@Final
	private static Map<String, Identifier> ARMOR_TEXTURE_CACHE;

	public MixinArmorFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Inject(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void injectCustomRenderer(MatrixStack matrices, VertexConsumerProvider vertexProvider, T wearer, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch, EquipmentSlot slot, int light, CallbackInfo info,
									  ItemStack stack, ArmorItem armor, A model, boolean isLegs, boolean hasGlint, int colorHex, float r, float g, float b) {
		if (armor.getMaterial() instanceof DynamicArmorMaterial) {
			DynamicArmorMaterial material = (DynamicArmorMaterial)armor.getMaterial();
			renderDynamicArmorParts(matrices, vertexProvider, light, stack, material, hasGlint, model, isLegs, r, g, b, null);
			renderDynamicArmorParts(matrices, vertexProvider, light, stack, material, hasGlint, model, isLegs, 1f, 1f, 1f, "overlay");
			info.cancel();
		}
	}

	private void renderDynamicArmorParts(MatrixStack matrices, VertexConsumerProvider vertexProvider, int i, ItemStack armorStack, DynamicArmorMaterial material, boolean renderGlint, A bipedEntityModel, boolean lowerParts, float r, float g, float b, @Nullable String textureSuffix) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexProvider, RenderLayer.getEntityCutoutNoCull(getDynamicArmorTexture(armorStack, material, lowerParts, textureSuffix)), false, renderGlint);
		bipedEntityModel.render(matrices, vertexConsumer, i, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
	}

	private Identifier getDynamicArmorTexture(ItemStack stack, DynamicArmorMaterial material, boolean lowerParts, @Nullable String suffix) {
		Identifier matId = material.getId(stack);
		Identifier texId = new Identifier(matId.getNamespace(), "textures/models/armor/" + matId.getPath() + "_layer_"+ (lowerParts ? 2 : 1) + (suffix == null ? "" : "_" + suffix) + ".png");
		return ARMOR_TEXTURE_CACHE.computeIfAbsent(texId.toString(), string -> texId);
	}
}
