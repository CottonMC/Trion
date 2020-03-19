package io.github.cottonmc.trion.api;

import com.google.common.collect.ImmutableList;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;

import java.util.List;

//TODO: documentation
public interface TrionComponent extends EntitySyncedComponent {
	boolean isTriggerActive();
	void tick();
	void activateTrigger(List<Trigger> equippedTriggers);
	void deactivateTrigger();
	ImmutableList<Trigger> getEquippedTriggers();
	boolean isBurst(); //TODO: better name?
	int getTrion();
	default void setTrion(int trion) {
		setTrion(trion, false);
	}
	void setTrion(int trion, boolean realOnly);
	int getMaxTrion();
	void setMaxTrion(int maxTrion);
}
