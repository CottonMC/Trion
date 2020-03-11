package io.github.cottonmc.trion.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.EntityDamageSource;

import javax.annotation.Nullable;

public class TrionDamageSource extends EntityDamageSource {
	//TODO: more info?
	public TrionDamageSource(String name, @Nullable Entity entity) {
		super(name, entity);
		this.setBypassesArmor();
	}
}
