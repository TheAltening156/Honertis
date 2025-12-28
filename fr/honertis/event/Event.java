package fr.honertis.event;

import fr.honertis.Honertis;
import fr.honertis.module.ModuleBase;

public class Event<T> {
	public boolean cancelled;
	public EventType type;
	
	public void onEvent(T e) {
		for (ModuleBase m : Honertis.INSTANCE.modulesManager.modules) {
			if (m.isEnabled()) {
				if (e instanceof EventUpdate) {
					m.onUpdate((EventUpdate) e);
				}	
				if (e instanceof EventRenderGui) {
					m.onGuiRender((EventRenderGui) e);
				}
				if (e instanceof EventRender2D) {
					m.onRender2D((EventRender2D) e);
				}
				if (e instanceof EventMouseClick) {
					m.onMouseClicked((EventMouseClick) e);
				}
				if (e instanceof EventMouseClickMove) {
					m.onMouseClickedMove((EventMouseClickMove)e);
				}
			}
		}
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	
	public boolean isPre() {
		if (type == null) return false;
		return type == EventType.PRE;
	}
	public boolean isPost() {
		if (type == null) return false;
		return type == EventType.POST;
	}
	
}
