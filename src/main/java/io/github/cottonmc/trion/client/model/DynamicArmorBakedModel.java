package io.github.cottonmc.trion.client.model;

import io.github.cottonmc.trion.TrionClient;
import io.github.cottonmc.trion.hooks.DynamicArmorMaterial;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class DynamicArmorBakedModel implements BakedModel, FabricBakedModel {
	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> supplier, RenderContext context) {
		BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
		if (stack.getItem() instanceof ArmorItem) {
			EquipmentSlot slot = ((ArmorItem)stack.getItem()).getSlotType();
			ArmorMaterial armor = ((ArmorItem)stack.getItem()).getMaterial();
			if (armor instanceof DynamicArmorMaterial) {
				DynamicArmorMaterial dynamic = (DynamicArmorMaterial)armor;
				Identifier id = dynamic.getId(stack);
				ModelIdentifier modelId = new ModelIdentifier(new Identifier(id.getNamespace(), "armor/" + id.getPath() + "_" + TrionClient.ARMOR_TYPES.get(slot)), "inventory");
				BakedModel model = modelManager.getModel(modelId);
				context.fallbackConsumer().accept(model);
			}
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.NONE;
	}

	@Override
	public ModelItemPropertyOverrideList getItemPropertyOverrides() {
		return ModelItemPropertyOverrideList.EMPTY;
	}
}
