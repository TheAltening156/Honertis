package fr.honertis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class DrawUtils extends GuiScreen{
	
	public static void drawImage(int x, int y, int width, int height, ResourceLocation image) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);

    }
}
