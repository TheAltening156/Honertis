package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;

public class ToggleSprint extends ModuleBase{

	public ToggleSprint() {
		super("ToggleSprint", "Sprint automatiquement", Category.UTILITIES);
	}
		
	@Override
	public void onUpdate(EventUpdate e) {
		mc.thePlayer.setSprinting(mc.gameSettings.keyBindForward.isKeyDown() && !mc.thePlayer.isCollidedHorizontally && !(mc.thePlayer.isEating() || mc.thePlayer.isBlocking()));
	}
	
}
