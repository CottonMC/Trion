package io.github.cottonmc.trion;

import io.github.cottonmc.trion.registry.TrionItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Trion implements ModInitializer {
	public static final String MODID = "trion";
	public static final ItemGroup TRION_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "trion_group"), () -> new ItemStack(TrionItems.TRIGGER));

	//trion color is 0x5fec94

	public static final Logger logger = LogManager.getLogger(MODID);

	@Override
	public void onInitialize() {
		TrionItems.init();
	}
}
