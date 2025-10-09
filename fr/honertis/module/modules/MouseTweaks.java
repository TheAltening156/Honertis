package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;

public class MouseTweaks extends ModuleBase{

	public MouseTweaks() {
		super("Yalter's MouseTweaks", "module.mouseTweaks.name", Category.UTILITIES);
	}
	
	@Override
	public void update() {
		if (this.isEnabled())
		yalter.mousetweaks.Main.onUpdateInGame();

	}
	
}
