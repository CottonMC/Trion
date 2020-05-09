package io.github.cottonmc.trion.client;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.TrionConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public String getModId() {
		return Trion.MODID;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(TrionConfig.class, parent).get();
	}
}
