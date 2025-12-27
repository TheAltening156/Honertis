package fr.honertis.module.addons;

import fr.honertis.event.EventMouseClick;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

public class MiniPlayer extends ModuleBase{
	
	public MiniPlayer() {
		super("MiniPlayer", "MiniPlayer position", Category.UTILITIES, false);
		this.addSettings(posX,posY);
	}
	
	@Override
	public void onUpdate(EventUpdate e) {
		if (!e.isPre()) return;
		//Utils.calculate(225, 40, posX, posY, new ScaledResolution(mc));
	}
	
	@Override
	public void onMouseClicked(EventMouseClick e) {
		if (mc.currentScreen instanceof GuiChat) {
			Utils.calculate(225, 40, posX, posY, new ScaledResolution(mc));
			System.out.println("dfsiopgjdfsgop");
			super.onMouseClicked(e);
		}
	}
	
}
