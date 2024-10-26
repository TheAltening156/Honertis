package fr.honertis.utils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class AnimUtils extends GuiScreen{
	
	public static void drawRectOnHover(int x, int y, int width, int height, int color, boolean hover) {
        if (hover) {
            drawRect(x, y, width, height, color);
        }
    }
}
