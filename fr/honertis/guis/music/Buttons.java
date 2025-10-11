package fr.honertis.guis.music;

import static net.minecraft.client.renderer.GlStateManager.*;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import fr.honertis.Honertis;

import static fr.honertis.utils.DrawUtils.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public enum Buttons {
	MINIPLAYER(new ResourceLocation("honertis/music/miniPlayer.png")),
	REPEAT(new ResourceLocation("honertis/music/repeat.png")),
	BACK(new ResourceLocation("honertis/music/back.png")),
	PLAY(new ResourceLocation("honertis/music/play.png")),
	FASTFORWARD(new ResourceLocation("honertis/music/ff.png")),
	VOLUME(new ResourceLocation("honertis/music/volume.png"));
	
	public ResourceLocation res;
	
    public long lastTime = System.currentTimeMillis();
    public double hoverProgress = 0;
    public boolean hover;
    
	Buttons(ResourceLocation res) {
		this.res = res;
	}
	
	public void draw(double posX, double posY, double pos, MusicPlayer player, boolean repeat, int mouseX, int mouseY) {
        long currentTime = System.currentTimeMillis();
        float delta = (currentTime - lastTime);
	    if (this != VOLUME) {
			
	    	posX -= 50;
			hover = GuiScreen.isHovered(posX + 140 + pos, posY + 225, posX + 140 + pos + 18, posY + 225 + 18, mouseX, mouseY);
	        delta /=  2.5f;
	        lastTime = currentTime;
	        if (hover) {
	        	hoverProgress = Math.min(1f, hoverProgress + delta * 0.05f);
	        } else {
	        	hoverProgress = Math.max(0f, hoverProgress - delta * 0.05f);
	        }
	        double offset = -1 * hoverProgress;
	        double extra = 2 * hoverProgress;
	        if (this == REPEAT) {
	        	res = new ResourceLocation("honertis/music/repeat" + (repeat ? "On" : "Off") + ".png");
	        }
	        if (this == PLAY) {
	        	res = new ResourceLocation("honertis/music/" + (player.paused ? "play" : "pause") + ".png");
	        }
	        
	        enableBlend();
	        disableLighting();
	        color(1,1,1f, 1f);
	    	drawImage(posX + 140 + pos + offset, posY + 225 + offset, 18 + extra, 18 + extra, res);
	    	disableBlend();
		} else {
			hover = GuiScreen.isHovered(posX + 115 + pos, posY + 225, posX + 115 + pos + 18 + 45, posY + 225 + 18, mouseX, mouseY);
			
            delta /= 47f;
	        lastTime = currentTime;
	        if (hover) {
	        	hoverProgress = Math.min(47f, hoverProgress + delta * 16f); 
	        } else {
	        	hoverProgress = Math.max(0f, hoverProgress - delta * 16f); 
	        }
	        drawRoundedRect(posX + 214, posY + 226, posX + 230 + (Honertis.INSTANCE.musicPlayer.volume >= 75 ? 2 : 0) + hoverProgress/*231 278*/, posY + 242, 16, new Color(80,80,80).getRGB());
	        double soundBarWidthProgress = (hoverProgress * (47/38));

	        double soundBarWidth = 38;
	        double animatedWidth = Math.min(soundBarWidth, soundBarWidthProgress);
	        if (!(soundBarWidthProgress < 0.2)) {
	        	drawRoundedRect(posX + 234, posY + 233, posX + 225.5 + soundBarWidthProgress, posY + 235, 2, new Color(255,255,255,125).getRGB());

	        	double soundRelativeX = mouseX - (posX + 234);
				if(soundRelativeX < 0) soundRelativeX = 0;
				if(soundRelativeX > soundBarWidth) soundRelativeX = soundBarWidth;
				double soundProgress = soundRelativeX / soundBarWidth;
	        	if (Mouse.isButtonDown(0)) {
	        		player.setVolume(Honertis.INSTANCE.musicPlayer.volume = (int) (soundProgress * 100));
	        	}
	        }
	        int volumeWidth = (int)((Honertis.INSTANCE.musicPlayer.volume / 100.0) * animatedWidth);
	    	drawRoundedRect(posX + 234, posY + 233, posX + 234 + volumeWidth, posY + 235, volumeWidth <= 1? 0 : 2, -1);
	        
	        enableBlend();
	        disableLighting();
	        color(1,1,1f, 1f);
	        double x = 12;
	        drawImage(posX + 217, posY + 228, 0.0f, Honertis.INSTANCE.musicPlayer.volume <= 25 ? 0 : Honertis.INSTANCE.musicPlayer.volume <= 75 ? 12 : Honertis.INSTANCE.musicPlayer.volume >= 75 ? 24 :0, x, x, x, x*3, res);
	        disableBlend();
		}
	}
}
