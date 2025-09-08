package fr.honertis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;

import static net.minecraft.client.renderer.GlStateManager.*;

import static net.minecraft.client.gui.GuiScreen.*;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.util.ResourceLocation;

public class DrawUtils{
	
	public static void setColor(int color) {
		float r = (color >> 16 & 255) / 255f;
		float g = (color >> 8 & 255) / 255f;
		float b = (color & 255) / 255f;
		float a = (color >> 24 & 255) / 255f;
		color(r, g, b, a);
	}
	
	public static void drawImage(double x, double y, double width, double height, ResourceLocation image) {
		drawImage(x, y, 0.0f, 0.0f, width, height, width, height, image);
	}
	public static void drawImage(double x, double y, double u, double v, double width, double height, double vw, double vh, ResourceLocation image) {
		pushMatrix();
        color(1,1,1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, (float)u, (float)v, width, height, (float)vw, (float)vh);
        popMatrix();
	}
	public static void drawImageShadow(double x, double y, double width, double height, ResourceLocation image) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		enableBlend();
        disableLighting();
        color(1,1,1f, 1f);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        disableBlend();
	}
	
	private static Map<String, ResourceLocation> thumbnailCache = new HashMap<>();

	private static BufferedImage image;
    public static void drawImageFromYoutubeURL(double x, double y, double width, double height, String urlString) {
        pushMatrix();
        try {
	    	ResourceLocation resource = thumbnailCache.get(urlString);
	    	
	        if (resource == null) {
	    		try {
	            	image = ImageIO.read(new URL(urlString));
		        	
		        } catch (Exception e) {
		        	for (String size : new String[] {"mqdefault", "hqdefault", "sddefault", "default"}) {
	                	try {
							image = ImageIO.read(new URL(urlString.replace("maxresdefault", size)));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	
			        }
	        		
	        	}
		    	if (image != null) {
	        		DynamicTexture dynamicTexture = new DynamicTexture(image);
		            ResourceLocation res = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("thumb_" + urlString.hashCode(), dynamicTexture);
		            thumbnailCache.put(urlString, res);
	        	}
	            return;
	        }
	
	        enableBlend();
	        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
	        tryBlendFuncSeparate(770, 771, 1, 0);
	        color(1, 1, 1);
	        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
	        disableBlend();
        } finally {
        	glPopMatrix();
        }
    }

	public static void drawCircle(double x, double y, double radius, int color) {
		for (int i : new int[3])
		drawCircleA(x, y, radius, color);
	}
	
	private static void drawCircleA(double x, double y, double radius, int color) {
		pushMatrix();

		disableTexture2D();
		setColor(color);
		enableBlend();
		glEnable(GL_POLYGON_SMOOTH);
		glBegin(GL_POLYGON);
		for (int i = 0; i <= 360; i++) {
			glVertex2d(x + Math.sin((i * Math.PI) / 180.0D) * radius,
					   y + Math.cos((i * Math.PI) / 180.0D) * radius);
			
		}
		glEnd();
		disableBlend();
		glDisable(GL_POLYGON_SMOOTH);
		disableTexture2D();
		popMatrix();

	}
	
	public static void drawRoundedRect(double x, double y, double width, double height, double round, int color) {
		glScaled(0.5D, 0.5D, 0.5D);
		x *= 2D;
		y *= 2D;
		width *= 2D;
		height *= 2D;
		glDisable(GL_TEXTURE_2D);
		setColor(color);
		glEnable(GL_BLEND);
		glBegin(GL_POLYGON);
		int i;
		for(i = 0; i <= 90; i += 3) {
			glVertex2d(    x + round + Math.sin((i * Math.PI) / 180D) * round * -1D,
					       y + round + Math.cos((i * Math.PI) / 180D) * round * -1D);
		}
		for(i = 90; i <= 180; i += 3) {
			glVertex2d(    x + round + Math.sin((i * Math.PI) / 180D) * round * -1D,
			          height - round + Math.cos((i * Math.PI) / 180D) * round * -1D);
		}
		for(i = 0; i <= 90; i += 3) {
			glVertex2d(width - round + Math.sin((i * Math.PI) / 180D) * round,
					  height - round + Math.cos((i * Math.PI) / 180D) * round);
		}
		for(i = 90; i <= 180; i += 3) {
			glVertex2d(width - round + Math.sin((i * Math.PI) / 180D) * round,
					       y + round + Math.cos((i * Math.PI) / 180D) * round);
		}
		glEnd();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glScaled(2D, 2D, 2D);
	}

}
