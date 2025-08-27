package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;

public class FullBright extends ModuleBase{
	public float currentGamma;
	
	public FullBright() {
		super("FullBight", "module.bright", Category.UTILITIES);
	}
	
	@Override
	public void onEnable() {
		currentGamma = mc.gameSettings.gammaSetting;
		mc.gameSettings.gammaSetting = 100;
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = currentGamma;
	}
	
}
