package fr.honertis.event;

import fr.honertis.Honertis;
import fr.honertis.module.ModuleBase;

public class Event<T> {
	public boolean cancelled;
	public EventType type;
	
	public void onEvent(EventUpdate e) {
	    for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority())
	        if (m.isEnabled()) m.onUpdate(e);
	}
	
	public void onEvent(EventAttack e) {
	    for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority())
	        if (m.isEnabled()) m.onAttacking(e);
	}

	public void onEvent(EventRenderGui e) {
	    for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority())
	        if (m.isEnabled()) m.onGuiRender(e);
	}

	public void onEvent(EventRender2D e) {
	    for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority())
	        if (m.isEnabled()) m.onRender2D(e);
	}
	
	public void onEvent(EventReceivePacket e) {
		for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority()) 
			if (m.isEnabled()) m.onReceivePacket(e);
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
