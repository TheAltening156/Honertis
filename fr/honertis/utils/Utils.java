package fr.honertis.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Utils {
	public static double textScroll(double textWidth, double maxTextWidth, long time, double speed) {
        double offset = 0;
		if (textWidth > maxTextWidth) {
            double cycle = textWidth + maxTextWidth + 2;
            offset = (time / 20.0 * speed) % cycle;
            if (offset > textWidth) offset = 0;
        }
        return offset;
	}
	
	public static String replaceUpperCase(String input) {
		StringBuilder sb = new StringBuilder();
        Pattern diacritics = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                String normalized = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
                normalized = diacritics.matcher(normalized).replaceAll("");
                sb.append(normalized);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
	}
	
	public static void scissorGui(double guiX, double guiY, double guiW, double guiH) {
	    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	    int scale = sr.getScaleFactor();

	    int x = (int) Math.floor(guiX * scale);
	    int width = (int) Math.ceil(guiW * scale);

	    int y = Minecraft.getMinecraft().displayHeight - (int) Math.ceil((guiY + guiH) * scale);
	    int height = (int) Math.ceil(guiH * scale);

	    GL11.glScissor(x, y, width, height);
	}
	
}
