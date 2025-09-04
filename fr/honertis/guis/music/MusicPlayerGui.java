package fr.honertis.guis.music;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.honertis.Honertis;
import fr.honertis.manager.FileManager;
import fr.honertis.utils.DrawUtils;
import fr.honertis.utils.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MusicPlayerGui extends GuiScreen {
	public boolean clicked;
	public double posX;
	public double posY;
	public double oldX;
	public double oldY;
	public CustomTextField text;
	public String currentSearch;
	
	public int scrollOffset = 0;
    public int maxScroll = 0;
    
    private static final String BIN_DIR = "honertis/musicPlayer/";
    private static final File songDir = new File(BIN_DIR + "/songs");

    private static final String YTDLP_URL = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe";
    private static final String FFMPEG_URL = "https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip";
    
    
	@Override
	public void initGui() {
		text = new CustomTextField(posX + 70, posY + 7, 150, 20);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (clicked) {
            posX += mouseX - oldX;
            posY += mouseY - oldY;
        }
        text.setPosition(posX + 70, posY + 5);

        for (double d : new double[]{4, 4.25, 4.50, 4.75}) {
            drawRoundedRect(posX + d, posY + d, posX + 281, posY + 255, 7, new Color(50, 50, 50, 30).getRGB());
        }
        drawRoundedRect(posX + 5, posY + 5, posX + 280, posY + 254, 5, new Color(50, 50, 50).getRGB());
        drawRect(posX + 5, posY + 25, posX + 280, posY + 26, new Color(25, 25, 25).getRGB());
        
        drawRect(posX + 5, posY + 210, posX + 280, posY + 211, new Color(25, 25, 25).getRGB());
        
        text.draw(mc);

        if (currentSearch != null) {
            List<SongItem> songs = parseSongs(currentSearch);

            double contentX = posX + 4;
            double contentY = posY + 26;
            double contentWidth = 275;
            double contentHeight = 184;

            int imagesPerRow = 3;
            double gap = 5;
            double imageWidth = 80;
            double imageHeight = 45;
            int lineSpacing = 12;

            int totalRows = (int) Math.ceil(songs.size() / (double) imagesPerRow);
            maxScroll = (int) Math.max(0, totalRows * (imageHeight + 5 + lineSpacing * 2) - contentHeight);

            GlStateManager.pushMatrix();
            ScaledResolution sr = new ScaledResolution(mc);
            int scaleFactor = sr.getScaleFactor();

            int scissorX = (int) (posX * scaleFactor);
            int scissorY = (int) ((sr.getScaledHeight() - posY - 208) * scaleFactor);
            int scissorWidth = (int) (275 * scaleFactor);
            int scissorHeight = (int) (180 * scaleFactor);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
            
            double songPosX = contentX + 15;
            double songPosY = contentY + 5 - scrollOffset;
            int count = 0;
            for (SongItem song : songs) {
            	song.drawImageAndText(songPosX, songPosY, imageWidth, imageHeight, lineSpacing, mouseX, mouseY);
            	
                count++;
                if (count % imagesPerRow == 0) {
                    songPosX = contentX + 15;
                    songPosY += imageHeight + 5 + lineSpacing * 2;
                } else {
                    songPosX += imageWidth + gap;
                }
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
            
        }
        
        boolean hoverPlay = isHovered(posX + 155, posY + 225, posX + 155 + 18, posY + 225 + 18, mouseX, mouseY);
        
        drawImage(posX + 155 + (hoverPlay ? -1 : 0), posY + 225+ (hoverPlay ? -1 : 0), 18+ (hoverPlay ? 2 : 0), 18 + (hoverPlay ? 2 : 0), new ResourceLocation("honertis/music/" + (Honertis.INSTANCE.musicPlayer.paused ? "play" : "pause") + ".png"));
        drawImage(posX + 130, posY + 225, 18, 18, new ResourceLocation("honertis/music/back.png"));
        drawImage(posX + 180, posY + 225, 18, 18, new ResourceLocation("honertis/music/ff.png"));

        if (!Honertis.INSTANCE.songName.equals("") || !Honertis.INSTANCE.thumbnail.equals("")) {
            mc.fontRendererObj.drawCenteredStringWithShadow(StringEscapeUtils.unescapeHtml4(Honertis.INSTANCE.songName), (int)posX + 165, (int)posY + 215, -1);	
            
	        drawImageFromYoutubeURL(posX + 15, posY + 215, 45, 35, Honertis.INSTANCE.thumbnail);
	        
	    }
        drawRoundedRect(posX+95, posY + 246, posX + 245, posY + 248, 2, new Color(255,255,255,125).getRGB());
        drawRoundedRect(posX+95, posY + 246, posX + 165, posY + 248, 2, -1);
        mc.fontRendererObj.drawStringWithShadow("--:--", (int)posX + 65, (int)posY + 243, -1);
        mc.fontRendererObj.drawStringWithShadow("--:--", (int)posX + 250, (int)posY + 243, -1);
        //Honertis.INSTANCE.musicPlayer.stop();
		oldX = mouseX;
		oldY = mouseY;
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
			// Format as days:hours:minutes:seconds
			return String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds);
		} else if (hours > 0) {
			// Format as hours:minutes:seconds
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		} else if (minutes > 0) {
			// Format as minutes:seconds
			return String.format("%d:%02d", minutes, seconds);
		} else {
			// Format as seconds only
			return String.format("0:%02d", seconds);
		}
	}
	
	@Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            scrollOffset -= wheel / 5;
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        }
    }
	
	@Override
	public void updateScreen() {
		text.update();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		text.mouseClicked(mouseX, mouseY, mouseButton);
		if (isHovered(posX + 5, posY + 5, posX + 280, posY + 26, mouseX, mouseY)) {
			if (!isHovered(posX + 70, posY + 6, posX + 220, posY + 24, mouseX, mouseY))
			clicked = true;
		}
		if (currentSearch != null) {
	        List<SongItem> songs = parseSongs(currentSearch);	
            double contentX = posX + 4;
            double contentY = posY + 26;
            double contentWidth = 275;
            double contentHeight = 184;

            int imagesPerRow = 3;
            double gap = 5;
            double imageWidth = 80;
            double imageHeight = 45;
            int lineSpacing = 12;

            int totalRows = (int) Math.ceil(songs.size() / (double) imagesPerRow);
            maxScroll = (int) Math.max(0, totalRows * (imageHeight + 5 + lineSpacing * 2) - contentHeight);

            GlStateManager.pushMatrix();
            ScaledResolution sr = new ScaledResolution(mc);
            int scaleFactor = sr.getScaleFactor();

            int scissorX = (int) (posX * scaleFactor);
            int scissorY = (int) ((sr.getScaledHeight() - posY - 208) * scaleFactor);
            int scissorWidth = (int) (275 * scaleFactor);
            int scissorHeight = (int) (180 * scaleFactor);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
            
            double songPosX = contentX + 15;
            double songPosY = contentY + 5 - scrollOffset;
            int count = 0;
            if (isHovered(posX + 5, posY + 28, posX + 280, posY + 207, mouseX, mouseY)) {
	            for (SongItem song : songs) {
	            	song.mouseClicked(songPosX, songPosY, imageWidth, imageHeight, lineSpacing, mouseX, mouseY);
	            	if (song.hover) {
	            		try {
							downloadAndPlaySong(song.getVideoId(), "down" + song.getVideoId() + ".wav", song);
						} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
							e.printStackTrace();
						}
	            	}
	                count++;
	                if (count % imagesPerRow == 0) {
	                    songPosX = contentX + 15;
	                    songPosY += imageHeight + 5 + lineSpacing * 2;
	                } else {
	                    songPosX += imageWidth + gap;
	                }
	            }
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
			
        }
		if (isHovered(posX + 155, posY + 225, posX + 155 + 18, posY + 225 + 18, mouseX, mouseY)) {
			if (!Honertis.INSTANCE.musicPlayer.paused)
				Honertis.INSTANCE.musicPlayer.pause();
			else 
				Honertis.INSTANCE.musicPlayer.resume();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		text.keyTyped(typedChar, keyCode);
		if (keyCode == Keyboard.KEY_ESCAPE) {
			if (!text.isFocused())
				mc.displayGuiScreen(null);
			else 
				text.setFocused(false);
		}
		if (keyCode == Keyboard.KEY_RETURN) {
			text.setFocused(false);
			new Thread(() -> {
				if (!text.getText().isEmpty())
				searchYoutube(text.getText());
			}).start();
		}
	}
	
	private void searchYoutube(String text) {
		String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=22&key=AIzaSyCer_ztDdAmZJqLSX1FsNFkb_t1v29A5Ls&q=" + text.replace(" ", "%20") + "&type=video";
		currentSearch = new String(WebUtils.visitSite(url).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);		
	}
	
	private static void downloadFile(String url, File destination) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
	
	private static void unzipFfmpeg(File zipFile, File destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith("ffmpeg.exe")) {
                    File newFile = new File(destDir, "ffmpeg.exe");
                    Files.copy(zis, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                }
            }
        }
    }
	
	private void downloadAndPlaySong(String videoUrl, String outputName, SongItem song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        new Thread(() -> {
			File binDir = new File(BIN_DIR);
	        if (!binDir.exists()) binDir.mkdirs();
	        if (!songDir.exists()) songDir.mkdirs();
	
	        File ytDlp = new File(binDir, "yt-dlp.exe");
	        if (!ytDlp.exists()) {
	            try {
					downloadFile(YTDLP_URL, ytDlp);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	
	        File ffmpegExe = new File(BIN_DIR, "ffmpeg.exe");
	        if (!ffmpegExe.exists()) {
	            File ffmpegZip = new File(BIN_DIR, "ffmpeg.zip");
	            try {
					downloadFile(FFMPEG_URL, ffmpegZip);
		            unzipFfmpeg(ffmpegZip, binDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            ffmpegZip.delete();
	        }
	        
	        ProcessBuilder pb = new ProcessBuilder(
	                BIN_DIR + "yt-dlp.exe",
	                "-x", "--audio-format", "wav",
	                "-o", songDir + "/" + outputName,
	                videoUrl
	        );
	        pb.redirectErrorStream(true);
	        try {
		        Process process = pb.start();
		
		        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
		            String line;
		            while ((line = reader.readLine()) != null) {
		                System.out.println(line);
		            }
		        }
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }

	        try {
				Honertis.INSTANCE.musicPlayer.play(new File(songDir + "/" + outputName));
				Honertis.INSTANCE.songName = song.getTitle();
				Honertis.INSTANCE.thumbnail = song.getThumbnailUrl();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }).start();
	}
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		clicked = false;
	}
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
		super.onResize(mcIn, p_175273_2_, p_175273_3_);
	}

	public static List<SongItem> parseSongs(String json) {
        List<SongItem> songs = new ArrayList<>();
        JSONObject obj = new JSONObject(json);
        JSONArray items = obj.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject id = item.getJSONObject("id");
            
            if (!id.getString("kind").equals("youtube#video")) continue;
            
            JSONObject snippet = item.getJSONObject("snippet");

            String videoId = id.getString("videoId");
            String title = snippet.getString("title");

            SongItem song = new SongItem(videoId, title, "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg");
            songs.add(song);
        }

        return songs;
    }
}
