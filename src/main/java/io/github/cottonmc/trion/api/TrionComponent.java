package io.github.cottonmc.trion.api;

import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;

//TODO: documentation
public interface TrionComponent extends EntitySyncedComponent {
	boolean isTriggerActive();
	void tick();
	void activateTrigger();
	void deactivateTrigger();
	int getTrion();
	default void setTrion(int trion) {
		setTrion(trion, false);
	}
	void setTrion(int trion, boolean realOnly);
	int getMaxTrion();
	void setMaxTrion(int maxTrion);
}
