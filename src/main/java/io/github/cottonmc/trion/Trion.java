package io.github.cottonmc.trion;

import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.api.TrionComponent;
import io.github.cottonmc.trion.impl.TrionComponentImpl;
import io.github.cottonmc.trion.registry.*;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Trion implements ModInitializer {
	public static final String MODID = "trion";
	public static final ItemGroup TRION_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "trion_group"), () -> new ItemStack(TrionItems.TRIGGER));

	public static final ComponentType<TrionComponent> TRION_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "trion"), TrionComponent.class);
	public static final Registry<Trigger> TRIGGERS = new DefaultedRegistry<>("trion:empty");

	//trion color is 0x5fec94, takamoma-2 color is 0x388e9a

	public static final Logger logger = LogManager.getLogger(MODID);

	@Override
	public void onInitialize() {
		TrionItems.init();
		TrionParticles.init();
		TrionSounds.init();
		TrionStatusEffects.init();
		TrionTriggers.init();
		EntityComponentCallback.event(PlayerEntity.class).register((player, container) -> container.put(TRION_COMPONENT, new TrionComponentImpl(player)));
	}
}
