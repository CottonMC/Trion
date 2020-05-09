package io.github.cottonmc.trion;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "trion")
public class TrionConfig implements ConfigData {
	@ConfigEntry.Category("hud")
	public int meterX = 4;

	@ConfigEntry.Category("hud")
	public int meterY = 16;

	@ConfigEntry.Category("hud")
	@ConfigEntry.Gui.Tooltip(count = 2)
	public boolean bigBars = false;
}
