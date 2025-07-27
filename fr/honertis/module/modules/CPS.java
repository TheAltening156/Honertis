package fr.honertis.module.modules;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import net.minecraft.client.gui.Gui;

public class CPS extends ModuleBase {
    public List<Long> lCps = Lists.newArrayList();
    public List<Long> rCps = Lists.newArrayList();
    public int posX = 10;
    public int posY = 30;
    
    private PressState leftButtonState;
    private PressState rightButtonState;
    
    public CPS() {
        super("CPS", "Montre combien de cps vous faites en 1 seconde", Category.ANCIEN_MODULES);
        leftButtonState = new PressState(lCps);
        rightButtonState = new PressState(rCps);
    }
    
    @Override
    public void onGuiRender(EventRenderGui e) {
        leftButtonState.isKeyPressed(Mouse.isButtonDown(0));
        rightButtonState.isKeyPressed(Mouse.isButtonDown(1));

        String lmb = "LMB: " + lCps.size();
        String rmb = "RMB: " + rCps.size();
        String text = lmb + " " + rmb;
        
        Gui.drawRect(posX - 4, posY - 4, posX + mc.fontRendererObj.getStringWidth(text) + 4, posY + 11, 0x90000000);
        mc.fontRendererObj.drawStringWithShadow(text, posX, posY, -1);
    }
    
    public class PressState {
        public boolean pressed;
        public long lastPress;
        public List<Long> list;

        PressState(List<Long> list) {
            this.list = list;
        }

        public void isKeyPressed(boolean key) {
            if (key != pressed) {
                lastPress = System.currentTimeMillis();
                pressed = key;
                if (key) {
                    list.add(lastPress);
                }
            }
            cleanOldClicks();
        }

        private void cleanOldClicks() {
            long currentTime = System.currentTimeMillis();
            list.removeIf(aLong -> aLong + 1000 < currentTime);
        }
    }
}
