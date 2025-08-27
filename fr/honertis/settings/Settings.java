package fr.honertis.settings;

import fr.honertis.utils.LangManager;

public class Settings {
	public String name;
	public boolean show = true;
	
	public Object getValue() {
		return null;
	}

	public void setValue(Object o) {
		
	}
	public String getName() {
		return LangManager.format(name);
	}
}
