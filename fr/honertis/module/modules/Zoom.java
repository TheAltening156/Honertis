package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;

public class Zoom extends ModuleBase{
	public BooleanSettings scroll = new BooleanSettings("Scroll", true);
	public boolean isZooming;
	
	public Zoom() {
		super("Zoom", "module.zoom", Category.UTILITIES);
		this.addSettings(scroll);
	}
	
	
	
}
