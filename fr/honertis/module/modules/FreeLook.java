package fr.honertis.module.modules;

import org.lwjgl.input.Keyboard;

import fr.honertis.event.EventRender2D;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.KeyBindSettings;

public class FreeLook extends ModuleBase {
	public KeyBindSettings key = new KeyBindSettings("module.freelook.key", 0);
	public BooleanSettings hold = new BooleanSettings("module.freelook.keyhold", true);

	public FreeLook() {
		super("FreeLook", "module.freelook", Category.UTILITIES);
		this.addSettings(hold, key);
		this.enabled = false;
	}

	public float rotYaw = 0.0F;
	public float rotPitch = 0.0F;
	
	public float lastYaw = 0.0F;
	public float lastPitch = 0.0F;

	public int lastView;
	
	@Override
	public void onEnable() {
		lastView = mc.gameSettings.thirdPersonView;
	    this.rotYaw = lastYaw = mc.thePlayer.rotationYaw;
	    this.rotPitch = lastPitch = mc.thePlayer.rotationPitch;
	    mc.gameSettings.thirdPersonView = 1;

	}

	@Override
	public void onDisable() {
		mc.thePlayer.rotationYaw = this.lastYaw;
		mc.thePlayer.rotationPitch = this.lastPitch;
	    mc.gameSettings.thirdPersonView = lastView;

	}

	@Override
	public void onRender2D(EventRender2D e) {
		if (enabled) {
		    mc.gameSettings.thirdPersonView = 1;
		}
	}
	
	@Override
	public void update() {
		if (hold.isEnabled()) {
			if (mc.currentScreen == null) {
    			if (Keyboard.getEventKey() == key.getKey()) {
    				if (Keyboard.getEventKeyState()) {
    					if (!isEnabled())
    						toggle();
    				} else {
    					if (isEnabled()) {
   							toggle();
    					}
    				}
    			}

    		} else {
    			if (isEnabled()) {
    				setEnabled(false);
    				onDisable();
    			}
    		}
		}
	}

}
