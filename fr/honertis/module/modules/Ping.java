package fr.honertis.module.modules;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Ping extends ModuleBase{

	public BooleanSettings tab = new BooleanSettings("Tab", true);

	public Ping() {
		super("Ping", "module.ping", Category.UTILITIES);
		this.addSettings(tab, posX, posY);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		String pingString;
		NetworkPlayerInfo d = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
		if (mc.isSingleplayer()) {
			pingString = "0";
		} else if (d != null) {
			pingString = String.valueOf(d.getResponseTime());			
		} else {
			pingString = "-1";
		}
		pingString = "Ping : " + pingString + " ms";
		double width = mc.fontRendererObj.getStringWidth(pingString) + 8;
		double height = 15;
		Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(pingString, posX.getValue() + 4, posY.getValue() + 4, -1);
		
		Utils.calculate(width, height, posX, posY, e.sr);
	}
	
}
