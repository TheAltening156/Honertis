package fr.honertis.module.modules;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import net.minecraft.client.gui.Gui;

public class CPS extends ModuleBase{
	public List<Long> lCps = Lists.newArrayList();
	public boolean lPressed;
	public long lLastPressed;
	public List<Long> rCps = Lists.newArrayList();
	public boolean rPressed;
	public long rLastPressed;
	public int posX = 10;
	public int posY = 30;
	
	public CPS() {
		super("CPS", "Montre combien de cps vous faites en 1 seconde", Category.UTILITIES);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		if (mc.currentScreen == null) {
			boolean lPressed = Mouse.isButtonDown(0);
			if (lPressed != this.lPressed) {
				this.lLastPressed = System.currentTimeMillis();
				this.lPressed = lPressed;
				if (lPressed) {
					this.lCps.add(this.lLastPressed);
				}
			}
			boolean rPressed = Mouse.isButtonDown(1);
			if (rPressed != this.rPressed) {
				this.rLastPressed = System.currentTimeMillis();
				this.rPressed = rPressed;
				if (rPressed) {
					this.rCps.add(this.rLastPressed);
				}
			}
		}
		String lmb = "LMB: " + getLCps();
		String rmb = "RMB: " + getRCps();
		Gui.drawRect(posX - 4, posY - 4, posX + mc.fontRendererObj.getStringWidth(lmb + " " + rmb) + 4, posY + 11, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(lmb + " " + rmb, posX, posY, -1);
	}
	
	public int getLCps() {
		long time = System.currentTimeMillis();
		this.lCps.removeIf(aLong -> aLong + 1000 < time);
		return  this.lCps.size();
	}
	public int getRCps() {
		long time = System.currentTimeMillis();
		this.rCps.removeIf(aLong -> aLong + 1000 < time);
		return  this.rCps.size();
	}
}
