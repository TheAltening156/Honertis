package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;

public class BlockHit extends ModuleBase{
	public BooleanSettings oldSword = new BooleanSettings("module.blockhit.sword", true);
	
	public BlockHit() {
		super("BlockHit", "module.blockhit", Category.OLD1_7);
		this.addSettings(oldSword);
	}
	
	
	
}
