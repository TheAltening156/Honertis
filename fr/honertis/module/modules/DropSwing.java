package fr.honertis.module.modules;

import org.lwjgl.input.Keyboard;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.utils.TimeUtils;
import net.minecraft.client.gui.GuiScreen;

public class DropSwing extends ModuleBase{
	public BooleanSettings armSwing = new BooleanSettings("module.dropSwing.arm", false);
	public BooleanSettings fastDropping = new BooleanSettings("Fast Dropping", true);

	public DropSwing() {
		super("1.15 Drop", "module.dropSwing", Category.UTILITIES);
		this.addSettings(fastDropping, armSwing);
	}
	
	TimeUtils dropTime = new TimeUtils();

    TimeUtils dropTime1 = new TimeUtils();
    private boolean autoDrop = false;
	
	@Override
	public void update() {
		if (mc.currentScreen == null && (isEnabled() && fastDropping.isEnabled())) {
	        if (Keyboard.getEventKey() == mc.gameSettings.keyBindDrop.getKeyCode() && Keyboard.getEventKeyState()) {
	        	if (mc.gameSettings.keyBindDrop.isPressed() && !mc.thePlayer.isSpectator()) {
		            boolean delayResetDone = false;
		            mc.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
		
		            autoDrop = true;
		            if (!delayResetDone) {
		                dropTime1.reset();
		                delayResetDone = true;
		            }
		
		            dropTime.reset();
		        } 
		        if (autoDrop) {
		            if (dropTime1.hasTimeElapsed(500, false)) {
		                if (dropTime.hasTimeElapsed(50, true)) {
		                    mc.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
		                }
		            }
		        }
		    }
        }
	}
	
}
