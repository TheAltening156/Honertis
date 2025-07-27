package fr.honertis.module;

public enum Category {
	OLD1_7("Acien 1.7"),
	UTILITIES("Utilitaires"),
	ANCIEN_MODULES("Ancien modules");
	
	public String name;
	Category(String name) {
		this.name = name;
	}
}
