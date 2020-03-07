package io.github.cottonmc.trion.api;

import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;

public interface TrionComponent extends EntitySyncedComponent {
	boolean isTriggerActive();
	//TODO: any good way to also have this do the 30 ticks of additional particles, or should that be left to triggers themselves?
	void activateTrigger();
	void deactivateTrigger();
	int getTrion();
	void setTrion(int trion);
}
