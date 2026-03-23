package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;

public class LowFire extends ModuleBase{

	public NumberSettings height = new NumberSettings("module.lowfire.height", 1.2, 1, 2, 0.01);
	
	public LowFire() {
		super("LowFire", "module.lowfire", Category.UTILITIES);
		this.addSettings(height);
	}
	
	
	
}
