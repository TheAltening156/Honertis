package fr.honertis.module.modules;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class CPS extends ModuleBase {
    public List<Long> lCps = Lists.newArrayList();
    public List<Long> rCps = Lists.newArrayList();
    
    private PressState leftButtonState;
    private PressState rightButtonState;
    
    public CPS() {
        super("CPS", "module.cps", Category.ANCIEN_MODULES);
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
        double width =  mc.fontRendererObj.getStringWidth(text) + 8;
        double height = 15;
        Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
        mc.fontRendererObj.drawStringWithShadow(text, posX.getValue() + 4, posY.getValue() + 4, -1);

		Utils.calculate(width, height, posX, posY, new ScaledResolution(Minecraft.getMinecraft()));
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
