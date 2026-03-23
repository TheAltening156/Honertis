package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.TimeUtils;

public class ToggleSneak extends ModuleBase {
    
    public ToggleSneak() {
		super("ToggleSneak", "module.sneak", Category.UTILITIES);
	}

    public boolean holdActive = false; 
    private boolean isSneakHeld = false; 
    private TimeUtils timer = new TimeUtils();

	@Override
	public void onUpdate(EventUpdate e) {
		if (mc.currentScreen == null) {
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			    if (!isSneakHeld) { 
			        isSneakHeld = true;
			        timer.reset();
			    }
			} else {
				if (isSneakHeld) {
			        long duration = System.currentTimeMillis() - timer.lastMs;

			        if (duration < 300) {
			            holdActive = !holdActive;
			        } else {
			            holdActive = false;
			        }

			        isSneakHeld = false;
			    }
			}
			
	    }
	}

	@Override
	public void onDisable() {
		holdActive = false;
	    isSneakHeld = false;
	}
}
