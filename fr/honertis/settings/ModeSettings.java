package fr.honertis.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSettings extends Settings{
	public List<String> modes;
	public int index;
	
	public ModeSettings(String name, String defMod, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		this.index = this.modes.indexOf(defMod);
	}
	
	
	public List<String> getModes() {
		return modes;
	}
	
	public String currentModName() {
		return modes.get(index);
	}
	
	public void setModes(List<String> modes) {
		this.modes = modes;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public boolean is(String name) {
		return index == modes.indexOf(name);
	}
	
	@Override
	public String getValue() {
		return currentModName();
	}
	
	public void setValue(Object value) {
		if (value instanceof String) {
			for (String v : this.modes) {
				if (v.equalsIgnoreCase((String) value)) {
					this.index = modes.indexOf(v);
					return;
				}
			}
		}
	}
	
}
