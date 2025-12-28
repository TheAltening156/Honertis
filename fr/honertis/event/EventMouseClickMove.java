package fr.honertis.event;

public class EventMouseClickMove extends Event<EventMouseClickMove> {
	public int mouseX;
	public int mouseY;
	public int mouseButton;
	
	public EventMouseClickMove(int mouseX, int mouseY, int mouseButton) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.mouseButton = mouseButton;
	}
	
	public int getMouseButton() {
		return mouseButton;
	}
	
	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}
	public int getMouseY() {
		return mouseY;
	}
	
	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
}
