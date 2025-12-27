package fr.honertis.event;

public class EventMouseClick extends Event<EventMouseClick> {
	public int mouseButton;
	
	public EventMouseClick(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
	public int getMouseButton() {
		return mouseButton;
	}
	
	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
}
