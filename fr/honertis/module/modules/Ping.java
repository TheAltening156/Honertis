package fr.honertis.module.modules;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Ping extends ModuleBase{

	public Ping() {
		super("Ping", "module.ping", Category.UTILITIES);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		NetworkPlayerInfo d = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
		if (d != null) {
			String pingString = "Ping : " + (mc.isSingleplayer() ? "0" : d.getResponseTime() + " ms");
			Gui.drawRect(6, 47, mc.fontRendererObj.getStringWidth(pingString) + 15, 61, 0x90000000);
			mc.fontRendererObj.drawStringWithShadow(pingString, 10, 50, -1);
		}
	}
	
}
