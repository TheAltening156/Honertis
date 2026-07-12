package fr.honertis.event;

import net.minecraft.entity.Entity;

public class EventAttack extends Event<EventAttack>{
	public Entity entity;
	
	public EventAttack(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
