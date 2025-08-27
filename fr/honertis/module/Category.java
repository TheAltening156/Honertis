package fr.honertis.module;

import fr.honertis.utils.LangManager;

public enum Category {
	OLD1_7("gui.1_7"),
	UTILITIES("gui.utilities"),
	ANCIEN_MODULES("gui.old");
	
	private String name;
	Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return LangManager.format(name);
	}
}
