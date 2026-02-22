package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.utils.LangManager;

public class NorCamHurt extends ModuleBase{
	public BooleanSettings hand = new BooleanSettings(LangManager.format("module.noCamHurt.hand"), false);
	
	public NorCamHurt() {
		super("NoCamHurt", "module.noCamHurt", Category.UTILITIES);
		this.addSettings(hand);
	}
	
}
