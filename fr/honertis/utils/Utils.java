package fr.honertis.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import fr.honertis.guis.resourcePacks.GuiScreenPackManager;
import fr.honertis.settings.NumberSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Util;

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

	public static void openPath(String s, File file1) {
		if (Util.getOSType() == Util.EnumOS.OSX) {
			try {
				LogManager.getLogger().info(s);
				Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
				return;
			} catch (IOException ioexception1) {
				LogManager.getLogger().error((String) "Couldn\'t open file", (Throwable) ioexception1);
			}
		} else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
			String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });

			try {
				Runtime.getRuntime().exec(s1);
				return;
			} catch (IOException ioexception) {
				LogManager.getLogger().error((String) "Couldn\'t open file", (Throwable) ioexception);
			}
		}

		boolean flag = false;

		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
					new Object[] { file1.toURI() });
		} catch (Throwable throwable) {
			LogManager.getLogger().error("Couldn\'t open link", throwable);
			flag = true;
		}

		if (flag) {
			LogManager.getLogger().info("Opening via system class!");
			Sys.openURL("file://" + s);
		}
	}
	
	public static void cantBeOut(double posX, double posY, double minX, double maxX, double minY, double maxY) {
		NumberSettings x = new NumberSettings("", posX, minX, maxX, 0);
		NumberSettings y = new NumberSettings("", posY, minY, maxY, 0);
		cantBeOut(x,y);
	}
	
	public static void cantBeOut(NumberSettings posX, NumberSettings posY) {
		/*if (posX.getValue() < posX.getMin()) {
			posX.setValue(posX.getMin());
		}
		if (posY.getValue() < posY.getMin()) {
			posY.setValue(posY.getMin());
		}
		if (posX.getValue() > posX.getMax()) {
			posX.setValue(posX.getMax());
		}
		if (posY.getValue() > posY.getMax()) {
			posY.setValue(posY.getMax());
		}*/
		calc2(posX);
		calc2(posY);
	}
	
	/**
	 * I dont know how to name this
	 */
	public static void calculate(double x, double y, NumberSettings posX, NumberSettings posY, ScaledResolution scale) {
		
		posX.setSize(x);
		posY.setSize(y);
		
		posX.setMax(scale.getScaledWidth() - x);
		posY.setMax(scale.getScaledHeight() - y);
		cantBeOut(posX, posY);
	}

	/*
	 * I dont know how to name this too
	 */
	
	public static void calc2(NumberSettings posX) {
		if (posX.getValue() < posX.getMin()) {
			posX.setValue(posX.getMin());
		}
		if (posX.getValue() > posX.getMax()) {
			posX.setValue(posX.getMax());
		}
	}
	
	
}
