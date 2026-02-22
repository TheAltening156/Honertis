package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;
import fr.honertis.utils.LangManager;

public class ItemPhysics extends ModuleBase{

	public NumberSettings rotSpeed = new NumberSettings(LangManager.format("module.itemPhysics.rotSpeed"), 50, 1, 100, 1); 
	
	public ItemPhysics() {
		super("ItemPhysics", "module.itemPhysics", Category.UTILITIES);
		this.addSettings(rotSpeed);
	}
	
}
