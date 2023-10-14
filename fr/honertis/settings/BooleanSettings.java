package fr.honertis.settings;

public class BooleanSettings extends Settings{
	public boolean toggled;
	
	public BooleanSettings(String name, boolean toggled) {
		this.name = name;
		this.toggled = toggled;
	}

	public void setToggled(boolean t) {
		this.toggled = t;
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public void toggle() {
		this.toggled = !toggled;
	}
}
