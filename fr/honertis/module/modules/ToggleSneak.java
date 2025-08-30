package fr.honertis.module.modules;

import org.lwjgl.input.Keyboard;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.TimeUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;

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
