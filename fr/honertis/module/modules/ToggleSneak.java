package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.TimeUtils;

public class ToggleSneak extends ModuleBase {
	public TimeUtils time = new TimeUtils();
	public boolean toggledSneak;
    
    public ToggleSneak() {
		super("ToggleSneak", "module.sneak", Category.UTILITIES);
	}

	@Override
	public void update() {
		/*if (mc.currentScreen != null) return;
		boolean keyDown = mc.gameSettings.keyBindSneak.isKeyDown();


        // Détecter relâchement
        if (keyDown) {
            if (time.check(300)) {
            	mc.gameSettings.keyBindSneak.pressed = !mc.gameSettings.keyBindSneak.pressed;
                time.reset();
            } else {
            	toggledSneak = true;
            }
            
        }
        
        mc.gameSettings.keyBindSneak.pressed = toggledSneak;*/
        //mc.thePlayer.movementInput.sneak = true;
    }
}
