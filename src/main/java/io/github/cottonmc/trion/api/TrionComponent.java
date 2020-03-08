package io.github.cottonmc.trion.api;

import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;

public interface TrionComponent extends EntitySyncedComponent {
	boolean isTriggerActive();
	void tick();
	void activateTrigger();
	void deactivateTrigger();
	int getTrion();
	void setTrion(int trion);
	int getMaxTrion();
	void setMaxTrion(int maxTrion);
}
