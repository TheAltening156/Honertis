package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.NumberSettings;

public class Zoom extends ModuleBase{
	public BooleanSettings scroll = new BooleanSettings("Scroll", true);
	public NumberSettings zoomSpeed = new NumberSettings("module.zoom.zoomspeed", 0.50, 0, 1, 0.01);
	public boolean isZooming;
	
	public Zoom() {
		super("Zoom", "module.zoom", Category.UTILITIES);
		this.addSettings(scroll, zoomSpeed);
	}
	
	
	
}
