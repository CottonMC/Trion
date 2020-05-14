package io.github.cottonmc.trion;

import com.mojang.datafixers.util.Pair;
import io.github.cottonmc.trion.client.hud.TrionBarHud;
import io.github.cottonmc.trion.client.model.DynamicArmorBakedModel;
import io.github.cottonmc.trion.client.particle.TransformationParticle;
import io.github.cottonmc.trion.client.particle.TrionDamageParticle;
import io.github.cottonmc.trion.registry.TrionItems;
import io.github.cottonmc.trion.registry.TrionParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class TrionClient implements ClientModInitializer {

	public static final String[] ARMOR_SETS = new String[]{"strategist", "ranger", "commander"}; //TODO: more configurable
	public static final Map<EquipmentSlot, String> ARMOR_TYPES = new HashMap<>();

	@Override
	public void onInitializeClient() {
		List<Identifier> ids = Stream.of(
				TrionItems.TRION_HELMET,
				TrionItems.TRION_CHESTPLATE,
				TrionItems.TRION_LEGGINGS,
				TrionItems.TRION_BOOTS
		).map(Registry.ITEM::getId).collect(Collectors.toList());
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex == 0 && stack.getItem() instanceof DyeableArmorItem) {
				return ((DyeableArmorItem)stack.getItem()).getColor(stack);
			}
			return 0xFFFFFF;
		}, TrionItems.TRION_HELMET, TrionItems.TRION_CHESTPLATE, TrionItems.TRION_LEGGINGS, TrionItems.TRION_BOOTS);

		ParticleFactoryRegistry.getInstance().register(TrionParticles.TRANSFORMATION, TransformationParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TrionParticles.TRION_DAMAGE, TrionDamageParticle.Factory::new);
		HudRenderCallback.EVENT.register(TrionBarHud::render);
		ModelLoadingRegistry.INSTANCE.registerAppender((manager, consumer) -> { //TODO: any way to make this easy with the current variant system?
			for (String set : ARMOR_SETS) {
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					if (slot.getType() == EquipmentSlot.Type.ARMOR) {
						consumer.accept(new ModelIdentifier(new Identifier(Trion.MODID, "armor/" + set + "_" + ARMOR_TYPES.get(slot)), "inventory"));
					}
				}
			}
			for (Identifier id : ids) {
				consumer.accept(new ModelIdentifier(id, "inventory"));
			}
		});

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> (modelId, context) -> {
			for (Identifier id : ids) {
				if (modelId.getNamespace().equals(id.getNamespace()) && modelId.getPath().equals(id.getPath())) {
					return new UnbakedModel() {
						@Override
						public Collection<Identifier> getModelDependencies() {
							return Collections.emptyList();
						}

						@Override
						public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
							return Collections.emptyList();
						}

						@Nullable
						@Override
						public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
							return new DynamicArmorBakedModel();
						}
					};
				}
			}
			return null;
		});
	}

	static {
		ARMOR_TYPES.put(EquipmentSlot.HEAD, "helmet");
		ARMOR_TYPES.put(EquipmentSlot.CHEST, "chestplate");
		ARMOR_TYPES.put(EquipmentSlot.LEGS, "leggings");
		ARMOR_TYPES.put(EquipmentSlot.FEET, "boots");
	}

}
