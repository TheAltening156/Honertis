package fr.honertis.module.modules;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Ping extends ModuleBase{

	public BooleanSettings tab = new BooleanSettings("Tab", true);

	public Ping() {
		super("Ping", "module.ping", Category.UTILITIES);
		this.addSettings(tab);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		NetworkPlayerInfo d = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
		if (d != null) {
			String pingString = "Ping : " + (mc.isSingleplayer() ? "0" : d.getResponseTime() + " ms");
			double width = mc.fontRendererObj.getStringWidth(pingString) + 8;
			double height = 15;
			Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
			mc.fontRendererObj.drawStringWithShadow(pingString, posX.getValue() + 4, posY.getValue() + 4, -1);
			
			Utils.calculate(width, height, posX, posY, new ScaledResolution(Minecraft.getMinecraft()));
		}
	}
	
}
