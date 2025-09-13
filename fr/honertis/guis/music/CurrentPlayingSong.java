package fr.honertis.guis.music;

import java.awt.Color;

import org.apache.commons.lang3.StringEscapeUtils;
import org.lwjgl.opengl.GL11;

import static fr.honertis.utils.DrawUtils.*;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class CurrentPlayingSong {
	private float smoothProgress = 0f;  
	private long lastTime = System.currentTimeMillis();
	
	public void draw(Minecraft mc, String songName, String thumbnail, String ytState, double posX, double posY, MusicPlayer musicPlayer, boolean repeat, double width, double height, int mouseX, int mouseY) {
       songName = songName.replace(".wav", "");
		if (!songName.equals("") || !thumbnail.equals("")) {
        	GlStateManager.pushMatrix();
        	GL11.glEnable(GL11.GL_SCISSOR_TEST);
        	Utils.scissorGui(posX + 70, posY, 205, 254);
        	String text = StringEscapeUtils.unescapeHtml4(songName);
	        int textWidth = mc.fontRendererObj.getStringWidth(text + "           ");
	        double maxTextWidth = 205;
        	double offset = Utils.textScroll(textWidth, maxTextWidth, System.currentTimeMillis(), 1);
        	if (textWidth > maxTextWidth) {
        		GlStateManager.translate(-offset, 0, 0);
        		mc.fontRendererObj.drawStringWithShadow(text + "           " + text, (int) posX + 75, (int) posY + 215, -1);
        		GlStateManager.translate(offset, 0, 0);
        	} else {
        		mc.fontRendererObj.drawCenteredStringWithShadow(text, (int) posX + 175, (int) posY + 215, -1);
        	}
        	GL11.glDisable(GL11.GL_SCISSOR_TEST);
        	GlStateManager.popMatrix();
        	drawImageFromYoutubeURL(posX + 12, posY + 215, 55, 30.93, thumbnail);
	        offset++;
	    }
	
        double pos = 0;
        for (Buttons buttons : Buttons.values()) {
        	buttons.draw(posX, posY, pos, musicPlayer, repeat, mouseX, mouseY);
        	pos += 25;
        }
		long current = System.currentTimeMillis();
    	float delta = (current - lastTime) / 1000f;
    	lastTime = current;

    	float targetProgress = 0f;
    	if (musicPlayer.durationMillis > 0) {
    	    targetProgress = musicPlayer.currentStateMillis / (float) musicPlayer.durationMillis;
    	} else {
    		smoothProgress = 0f;
    	}
		
    	if (Float.isNaN(smoothProgress) || Float.isInfinite(smoothProgress)) {
    	    smoothProgress = 1f;
    	}
    	smoothProgress = Math.max(0f, Math.min(1f, (targetProgress - smoothProgress) + delta));

    	int barWidth = 245 - 95;
    	int filledWidth = (int)(barWidth * smoothProgress);

    	drawRoundedRect(posX+95, posY+247, posX+245, posY+249, 2, new Color(255,255,255,125).getRGB());
    	drawRoundedRect(posX+95, posY+247, posX+95+filledWidth, posY+249, 2, Color.WHITE.getRGB());
        mc.fontRendererObj.drawStringWithShadow(millisecToTime(musicPlayer.currentStateMillis), (int)posX + 93 - mc.fontRendererObj.getStringWidth(millisecToTime(musicPlayer.currentStateMillis)), (int)posY + 244, -1);
        mc.fontRendererObj.drawStringWithShadow(millisecToTime(musicPlayer.durationMillis), (int)posX + 277 - mc.fontRendererObj.getStringWidth(millisecToTime(musicPlayer.durationMillis)), (int)posY + 244, -1);
        if (!ytState.isEmpty() || !ytState.equals("")) {
        	drawRoundedRect((double)width - mc.fontRendererObj.getStringWidth(ytState + "    "), (double)height-30D,(double) width - 2D, (double)height -12D, 5D, Color.DARK_GRAY.getRGB());
        	mc.fontRendererObj.drawString(ytState, width - mc.fontRendererObj.getStringWidth(ytState + "  "), height - 25, -1);
        }
	}
	public String millisecToTime(long millis) {
		long totalSeconds = millis / 1000;
		long seconds = totalSeconds % 60;
		long totalMinutes = totalSeconds / 60;
		long minutes = totalMinutes % 60;
		long totalHours = totalMinutes / 60;
		long hours = totalHours % 24;
		long days = totalHours / 24;

		if (days > 0) {
			return String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds);
		} else if (hours > 0) {
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		} else if (minutes > 0) {
			return String.format("%d:%02d", minutes, seconds);
		} else {
			return String.format("0:%02d", seconds);
		}
	}
}
