package fr.honertis.module.addons;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;

public class MiniPlayer extends ModuleBase{
	
	public MiniPlayer() {
		super("MiniPlayer", "MiniPlayer position", Category.UTILITIES, false);
		this.addSettings(posX,posY);
		posX.setSize(225);
		posY.setSize(40);
	}
	
}
