package fr.honertis.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import fr.honertis.settings.NumberSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;

public class Utils {
	public static File dir = new File("Honertis");
	public static File workdir = new File(getAppData(), ".honertis");
;

	public static File getAppData() {
		EnumOS os = net.minecraft.util.Util.getOSType();
		if (os == EnumOS.WINDOWS)
			return new File(System.getenv("APPDATA"));
		if (os == EnumOS.OSX)
			return new File(new File(System.getProperty("user.home"), "Library"), "Application Support");
		return new File(System.getProperty("user.home"));
	}
	
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
	
	private static String[] getVersionParts() {
		String version = System.getProperty("os.version");
	    String[] parts = version.split("\\.");
	    return parts;
	}
	
	public static int getVersionFromInt(int i) {
		return getVersionParts().length > i ? Integer.parseInt(getVersionParts()[i]) : 0;
	}
	public static boolean isMacOSAtLeast(int major, int minor) {
	    int vMajor = getVersionFromInt(0);
	    int vMinor = getVersionFromInt(1);

	    if (vMajor > major) return true;
	    if (vMajor == major && vMinor >= minor) return true;

	    return false;
	}
	
	
	public static void openPath(String s, File file1) {
		if (Util.getOSType() == Util.EnumOS.OSX) {
			try {
				LogManager.getLogger().info(s);
				Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
				return;
			} catch (IOException ioexception1) {
				LogManager.getLogger().error("Couldn\'t open file", ioexception1);
			}
		} else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
			String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });

			try {
				Runtime.getRuntime().exec(s1);
				return;
			} catch (IOException ioexception) {
				LogManager.getLogger().error("Couldn\'t open file", ioexception);
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
	
	public static void cantBeOut(NumberSettings posX, NumberSettings posY) {
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
	
	/**
	 * I dont know how to name this
	 */
	public static void calculate(NumberSettings posX, NumberSettings posY, ScaledResolution scale) {
		calculate(posX.getSize(), posY.getSize(), posX, posY, scale);
	}

	/*
	 * I dont know how to name this too
	 */
	
	public static void calc2(NumberSettings pos) {
		if (pos.getValue() < pos.getMin()) {
			pos.setValue(pos.getMin());
		}
		if (pos.getValue() > pos.getMax()) {
			pos.setValue(pos.getMax());
		}
	}
	
	/*
	 * 
	 * Colors
	 * 
	 */
	public static int getRainbow(float seconds, float saturation, float brightness, long speed) {
	    float hue = (System.currentTimeMillis() + speed) % ((int)(seconds * 1000.0F)) / (seconds * 1000.0F);
	    int color = Color.HSBtoRGB(hue, saturation, brightness);
	    return color;
	}
}
