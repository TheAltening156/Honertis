package fr.honertis.guis.music;

import org.apache.commons.lang3.StringEscapeUtils;

import fr.honertis.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class SongItem {
	private String videoId;
	private String title;
	private String thumbnailUrl;
	public boolean hover;

	public SongItem(String videoId, String title, String thumbnailUrl) {
		this.videoId = videoId;
		this.title = title;
		this.thumbnailUrl = thumbnailUrl;
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
		if (GuiScreen.isHovered(posX + 5, posY + 28, posX + 280, posY + 207, mouseX, mouseY)) {
            hover = GuiScreen.isHovered(songPosX, songPosY, songPosX + imageWidth, songPosY + imageHeight, mouseX, mouseY);
    	}
		DrawUtils.drawImageFromYoutubeURL(songPosX + (hover ? -2 : 0), songPosY + (hover ? -2 : 0), imageWidth + (hover ? 4 : 0), imageHeight + (hover ? 4 : 0), getThumbnailUrl());

        int textX = (int) (songPosX + imageWidth / 2);
        int textY = (int) (songPosY + imageHeight + 5);

        for (Object line : Minecraft.getMinecraft().fontRendererObj.listFormattedStringToWidth(StringEscapeUtils.unescapeHtml4(getTitle()), (int) imageWidth)) {                	
        	Minecraft.getMinecraft().fontRendererObj.drawCenteredStringWithShadow((String) line, textX, textY, -1);
        	textY += lineSpacing;
        }
        
	}
	
	public void mouseClicked(double songPosX, double songPosY, double imageWidth, double imageHeight, double lineSpacing, int mouseX, int mouseY) {
        hover = GuiScreen.isHovered(songPosX, songPosY, songPosX + imageWidth, songPosY + imageHeight, mouseX, mouseY);
	}

}
