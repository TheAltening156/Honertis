package fr.honertis.settings;

public class BooleanSettings extends Settings{
	public boolean toggled;
	
	public BooleanSettings(String name, boolean toggled) {
		this.name = name;
		this.toggled = toggled;
	}

	public BooleanSettings(String name, boolean toggled, boolean show) {
		this.name = name;
		this.toggled = toggled;
		this.show = show;
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
	
	@Override
	public Boolean getValue() {
		return isToggled();
	}
	public void setValue(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			if(s.equalsIgnoreCase("true")) {
				this.toggled = true;
			} else if (s.equalsIgnoreCase("false")) {
				this.toggled = false;
			}
		}
		if (o instanceof Boolean) {
			this.toggled = (boolean) o;
		}
	}
}
