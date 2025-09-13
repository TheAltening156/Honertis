package fr.honertis.guis.music;

import org.apache.commons.lang3.StringEscapeUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.honertis.Honertis;
import fr.honertis.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class SongItem {
	private String videoId;
	private String title;
	private String thumbnailUrl;
	public boolean hover;
	private float hoverProgress = 0f; 
    private long lastTime;

	public SongItem(String videoId, String title, String thumbnailUrl) {
		this.videoId = videoId;
		this.title = title;
		this.thumbnailUrl = thumbnailUrl;
        this.lastTime = System.currentTimeMillis();
	}
	
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void drawImageAndText(double songPosX, double songPosY, double imageWidth, double imageHeight, double lineSpacing, double posX, double posY, int mouseX, int mouseY) {		
        hover = (Honertis.INSTANCE.musicPlayer.isYoutube ? GuiScreen.isHovered(songPosX, songPosY, songPosX + imageWidth, songPosY + imageHeight, mouseX, mouseY) : GuiScreen.isHovered(songPosX + 17, songPosY, songPosX + imageWidth - 18, songPosY + imageHeight, mouseX, mouseY))
        	 && GuiScreen.isHovered(posX + 5, posY + 28, posX + 280, posY + 207, mouseX, mouseY);
    	
		long current = System.currentTimeMillis();
        float delta = (current - lastTime) / 2.5f;
        lastTime = current;

        if (hover) {
            hoverProgress = Math.min(1f, hoverProgress + delta * 0.05f);
        } else {
            hoverProgress = Math.max(0f, hoverProgress - delta * 0.05f);
        }
		
		double offset = -2 * hoverProgress;
        double extraSize = 4 * hoverProgress;
		DrawUtils.drawImageFromYoutubeURL(songPosX + offset, songPosY + offset, imageWidth + extraSize, imageHeight + extraSize, getThumbnailUrl());
	}
	
	public void mouseClicked(double songPosX, double songPosY, double imageWidth, double imageHeight, double lineSpacing, int mouseX, int mouseY) {
        hover = Honertis.INSTANCE.musicPlayer.isYoutube ? GuiScreen.isHovered(songPosX, songPosY, songPosX + imageWidth, songPosY + imageHeight, mouseX, mouseY) : GuiScreen.isHovered(songPosX + 17, songPosY, songPosX + imageWidth - 18, songPosY + imageHeight, mouseX, mouseY);
	}

}
