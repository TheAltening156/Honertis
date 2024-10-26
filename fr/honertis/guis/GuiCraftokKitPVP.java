package fr.honertis.guis;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;

public class GuiCraftokKitPVP extends GuiScreen{
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, this.width, this.height, new Color(0,0,0, 100).getRGB());
		mc.fontRendererObj.drawCenteredString("Activer le mode kitpvp?", this.width / 2, this.height / 2 + -(mc.fontRendererObj.FONT_HEIGHT / 2), -1);
		mc.fontRendererObj.drawCenteredString("(marche seulement pendant les events)", this.width / 2, this.height / 2 + (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
	}
	
}
