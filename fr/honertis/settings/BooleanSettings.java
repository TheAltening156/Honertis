package fr.honertis.settings;

public class BooleanSettings extends Settings{
	public boolean enabled;
	
	public BooleanSettings(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public BooleanSettings(String name, boolean enabled, boolean show) {
		this.name = name;
		this.enabled = enabled;
		this.show = show;
	}
	
	public void setEnabled(boolean t) {
		this.enabled = t;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void toggle() {
		this.enabled = !enabled;
	}
	
	@Override
	public Boolean getValue() {
		return isEnabled();
	}
	public void setValue(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			if(s.equalsIgnoreCase("true")) {
				this.enabled = true;
			} else if (s.equalsIgnoreCase("false")) {
				this.enabled = false;
			}
		}
		if (o instanceof Boolean) {
			this.enabled = (boolean) o;
		}
	}
}
