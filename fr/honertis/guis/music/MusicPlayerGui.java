package fr.honertis.guis.music;

import static net.minecraft.client.renderer.GlStateManager.*;

import static fr.honertis.utils.Utils.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
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
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.honertis.Honertis;
import fr.honertis.manager.FileManager;
import fr.honertis.utils.DrawUtils;
import fr.honertis.utils.LangManager;
import fr.honertis.utils.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class MusicPlayerGui extends GuiScreen {
	public boolean clicked;
	public double posX;
	public double posY;
	public double oldX;
	public double oldY;
	public CustomTextField text = new CustomTextField(posX + 70, posY + 7, 150, 20);;
	public String currentSearch;
	
	public MusicPlayer musicPlayer = new MusicPlayer();
	public String songName = "";
    public String thumbnail = "";
	
	public String ytState = "";
	public List<SongItem> songs = new ArrayList<SongItem>();
	
	public int scrollOffset = 0;
    public int maxScroll = 0;
        
    public boolean isYoutube = true;
    
    private static final String BIN_DIR = "Honertis/musicPlayer/";
    private static final File songDir = new File(BIN_DIR + "/songs");

    private static final String YTDLP_URL = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe";
    private static final String FFMPEG_URL = "https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip";
    
    public int volume = 75;
    
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (clicked) {
            posX += mouseX - oldX;
            posY += mouseY - oldY;
        }
		if (isYoutube)
			text.setPosition(posX + 70, posY + 5);

        for (double d : new double[]{4, 4.25, 4.50, 4.75}) {
            drawRoundedRect(posX + d, posY + d, posX + 281, posY + 255, 7, new Color(50, 50, 50, 30).getRGB());
        }
        drawRoundedRect(posX + 5, posY + 5, posX + 280, posY + 254, 5, new Color(50, 50, 50).getRGB());
        drawRect(posX + 5, posY + 25, posX + 280, posY + 26, new Color(25, 25, 25).getRGB());
        
        drawRect(posX + 5, posY + 210, posX + 280, posY + 211, new Color(25, 25, 25).getRGB());
        if (isYoutube)
        	text.draw(mc);
        
    	drawRoundedRect(posX + 15, posY + 9, posX + 27, posY + 21, 5, isYoutube ? new Color(100,100,100).getRGB() : new Color(0, 125, 255).getRGB());
    	mc.fontRendererObj.drawStringWithShadow("Local", (int)posX + 32, (int)posY + 11, -1);
        if (!isYoutube) {
        	double position = posX + 79 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.refresh"));
        	drawRoundedRect(posX + 71, posY + 6, position, posY + 24, 5, isHovered(posX + 71, posY + 6, position, posY + 24, mouseX, mouseY) ? new Color(150,150,150).getRGB() : new Color(100,100,100).getRGB());
        	mc.fontRendererObj.drawStringWithShadow(LangManager.format("gui.refresh"), (int)posX + 75, (int)posY + 11, -1);
        	
        	drawRoundedRect(position + 5, posY + 6, position + 15 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, 5, isHovered(position + 5, posY + 6, position + 15 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, mouseX, mouseY) ? new Color(150,150,150).getRGB() : new Color(100,100,100).getRGB());
        	mc.fontRendererObj.drawStringWithShadow(LangManager.format("gui.open"), (int)position + 10, (int)posY + 11, -1);

        }
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
        
        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = sr.getScaleFactor();

        int scissorX = (int) ((posX + 8) * scaleFactor);
        int scissorY = (int) ((sr.getScaledHeight() - posY - 208) * scaleFactor);
        int scissorWidth = (int) ((275-6) * scaleFactor);
        int scissorHeight = (int) (180 * scaleFactor);

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);

        double songPosX = contentX + 15;
        double songPosY = contentY + 5 - scrollOffset;
        int count = 0;
        for (SongItem song : songs) {
            song.drawImageAndText(songPosX, songPosY, imageWidth, imageHeight, lineSpacing, posX, posY, mouseX, mouseY);

            count++;
            if (count % imagesPerRow == 0) {
                songPosX = contentX + 15;
                songPosY += imageHeight + lineSpacing * 2;
            } else {
                songPosX += imageWidth + gap;
            }
        }

        count = 0;
        songPosX = contentX + 15;
        songPosY = contentY + 5 - scrollOffset;
        {
	        for (SongItem song : songs) {
	            int textX = (int) songPosX;
	            int textY = (int) (songPosY + imageHeight + 5);
	            int maxTextWidth = (int) imageWidth;
	            String text = StringEscapeUtils.unescapeHtml4(song.getTitle());
	            text = text.replace(".wav", "");
	            int textWidth = mc.fontRendererObj.getStringWidth(text + "       ");

	            double offset = textScroll(textWidth, maxTextWidth, System.currentTimeMillis(), 0.5);
	            
	            GlStateManager.pushMatrix();
	            GL11.glEnable(GL11.GL_SCISSOR_TEST);

	            double localX = Math.max(textX, contentX);
	            double localY = Math.max(textY, contentY);
	            double localW = Math.min(textX + maxTextWidth, contentX + (scissorWidth / scaleFactor)) - localX;
	            double localH = Math.min(textY + lineSpacing, contentY + (scissorHeight / scaleFactor)) - localY;

	            if (localW > 0 && localH > 0) {
	                scissorGui(localX, localY, localW, localH);

	                if (textWidth > maxTextWidth) {
	                    GlStateManager.translate(-offset, 0, 0);
	                    mc.fontRendererObj.drawStringWithShadow(text + "       " + text, textX, textY, -1);
	                    GlStateManager.translate(offset, 0, 0);
	                } else {
	                    mc.fontRendererObj.drawCenteredStringWithShadow(text, textX + maxTextWidth / 2, textY, -1);
	                }
	            }

	            GL11.glDisable(GL11.GL_SCISSOR_TEST);
	            GlStateManager.popMatrix();
	            
	            count++;
	            if (count % imagesPerRow == 0) {
	                songPosX = contentX + 15;
	                songPosY += imageHeight + lineSpacing * 2;
	            } else {
	                songPosX += imageWidth + gap;
	            }
	        }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        
        CurrentPlayingSong c = new CurrentPlayingSong();
        c.draw(mc, songName, thumbnail, ytState, posX, posY, musicPlayer, width, height, mouseX, mouseY);
        oldX = mouseX;
		oldY = mouseY;
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
		if (isYoutube)
			text.update();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY)) {
			isYoutube = !isYoutube;
		}
		double position = posX + 79 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.refresh"));
		if (!isYoutube) {
			File file1 = new File(BIN_DIR + "/localSongs");
			file1.mkdirs();
			if (isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY) || isHovered(posX + 71, posY + 6, position, posY + 24, mouseX, mouseY)) {
				songs = new ArrayList<SongItem>();
				
				File [] files = file1.listFiles();
				ytState = "Searching for local songs ...";
				for (File f : files) {
					if (f.isFile() && f.getName().endsWith(".wav")) {
						ytState = "Found " + f.getName();
						songs.add(new SongItem("", f.getName(), "https://pixelpc.fr/art169.png"));	
					}
				}
				ytState = "";
			}
			if (isHovered(position + 5, posY + 6, position + 15 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, mouseX, mouseY)) {
				String s = file1.getAbsolutePath();

				if (Util.getOSType() == Util.EnumOS.OSX) {
					try {
						System.out.println(s);
						Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
						return;
					} catch (IOException ioexception1) {
						System.out.println((String) "Couldn\'t open file");
					}
				} else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
					String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });

					try {
						Runtime.getRuntime().exec(s1);
						return;
					} catch (IOException ioexception) {
						System.out.println((String) "Couldn\'t open file");
					}
				}

				boolean flag = false;

				try {
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
							new Object[] { file1.toURI() });
				} catch (Throwable throwable) {
					flag = true;
				}

				if (flag) {
					Sys.openURL("file://" + s);
				}
			}
		} else {
			songs = new ArrayList<SongItem>();
		}
		if (isYoutube)
			text.mouseClicked(mouseX, mouseY, mouseButton);
		if (isHovered(posX + 5, posY + 5, posX + 280, posY + 26, mouseX, mouseY)) {
			if (!(isYoutube ? isHovered(posX + 70, posY + 6, posX + 220, posY + 24, mouseX, mouseY) : (isHovered(posX + 71, posY + 6, position, posY + 24, mouseX, mouseY)) || isHovered(position + 5, posY + 6, position + 15 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, mouseX, mouseY)) && !isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY))
			clicked = true;
		}
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
            	if (song.hover && mouseButton == 0) {
            		try {
						downloadAndPlaySong(song.getVideoId(), "down" + song.getVideoId() + ".wav", song);
					} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
						e.printStackTrace();
					}
            	}
            	 count++;
 	            if (count % imagesPerRow == 0) {
 	                songPosX = contentX + 15;
 	                songPosY += imageHeight + lineSpacing * 2;
 	            } else {
 	                songPosX += imageWidth + gap;
 	            }
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
			
        if (mouseButton == 0) {
			if (isHovered(posX + 165, posY + 225, posX + 165 + 18, posY + 225 + 18, mouseX, mouseY)) {
				if (!musicPlayer.paused) {
					musicPlayer.pause();
				} else { 
					if (musicPlayer.currentStateMillis == musicPlayer.durationMillis) 
						musicPlayer.playLastFile();
					else
						musicPlayer.resume();
				}
			}
			
			if (isHovered(posX + 140, posY + 225, posX + 140 + 18, posY + 225 + 18, mouseX, mouseY)) {
				musicPlayer.playLastFile();
			}
			if (isHovered(posX + 190, posY + 225, posX + 190 + 18, posY + 225 + 18, mouseX, mouseY)) {
				musicPlayer.pause();
				musicPlayer.currentStateMillis = musicPlayer.durationMillis;
			}
        }
		/*double barWidth = 245 - 95;
		if (isHovered(posX+95, posY + 246, posX + 245, posY + 248, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			double relativeX = mouseX - (posX + 95);
			if(relativeX < 0) relativeX = 0;
			if(relativeX > 245) relativeX = barWidth;
			double progress = relativeX / barWidth;
			musicPlayer.currentStateMillis = (long) (progress * musicPlayer.durationMillis);
			try {
				musicPlayer.setState(progress);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
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
		if (keyCode == Keyboard.KEY_RETURN && text.isFocused()) {
			text.setFocused(false);
			new Thread(() -> {
				if (!text.getText().isEmpty()) 
					searchYoutube(text.getText());
				
			}).start();
		}
	}
	
	private void searchYoutube(String text) {
		try {
			String encodedUrl = URLEncoder.encode(text, "UTF-8");
			String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=22&key=AIzaSyCer_ztDdAmZJqLSX1FsNFkb_t1v29A5Ls&q=" + encodedUrl + "&type=video";
			currentSearch = new String(WebUtils.visitSite(url).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);		
			if (currentSearch != null) {
				this.songs = parseSongs(currentSearch, encodedUrl);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	
	private static void downloadFile(String url, File destination) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
	
	private void unzipFfmpeg(File zipFile, File destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith("ffmpeg.exe")) {
                    File newFile = new File(destDir, "ffmpeg.exe");
                    ytState = "Extracting ffmpeg ... ";
                    newFile.getParentFile().mkdirs();
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
					ytState = "Downloading yt-dlp ...";
					downloadFile(YTDLP_URL, ytDlp);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	
	        File ffmpegExe = new File(binDir, "ffmpeg.exe");
	        if (!ffmpegExe.exists()) {
	            File ffmpegZip = new File(binDir, "ffmpeg.zip");
	            try {
		        	ytState = "Downloding ffmpeg ... ";
					downloadFile(FFMPEG_URL, ffmpegZip);
		            unzipFfmpeg(ffmpegZip, binDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            ffmpegZip.delete();
	        }
	        if (isYoutube) {
	        	ytState = "Getting song from link: " + videoUrl;
		        ProcessBuilder pb = new ProcessBuilder(
		        		binDir + "/yt-dlp.exe",
		                "-x", "--audio-format", "wav",
		                "-o", songDir + "/" + outputName,
		                "--", videoUrl
		        );
	
		        pb.redirectErrorStream(true);
		        try {
			        Process process = pb.start();
			
			        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			            String line;
			            while ((line = reader.readLine()) != null) {
			                System.out.println(line);
			                ytState = line;
			            }
			        }
		        } catch (IOException e) {
		        	e.printStackTrace();
		        }
	        }
	        ytState = "";
	        try {
				musicPlayer.play(new File((isYoutube ? songDir + "/" + outputName : binDir + "/localSongs/" + song.getTitle())));
				songName = song.getTitle();
				thumbnail = song.getThumbnailUrl();
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

	public static List<SongItem> parseSongs(String json, String searchQuery) {
        List<SongItem> songs = new ArrayList<>();
        if (json.startsWith("{  \"kind")) {
            JSONObject obj = new JSONObject(json);
	        JSONArray items = obj.getJSONArray("items");
	
	        for (int i = 0; i < items.length(); i++) {
	            JSONObject item = items.getJSONObject(i);
	            JSONObject id = item.getJSONObject("id");
	            
	            if (!id.getString("kind").equals("youtube#video")) continue;
	            
	            JSONObject snippet = item.getJSONObject("snippet");
	
	            String videoId = id.getString("videoId");
	            String title = snippet.getString("title");
	
	            SongItem song = new SongItem(videoId, title.replace("È", "è"), "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg");
	            songs.add(song);
	        }
        } else if (json.startsWith("{  \"error")) {
        	String html = visitSite("https://www.youtube.com/results?search_query=" + searchQuery);

            int start = html.indexOf("ytInitialData") + 15;
            int end = html.indexOf("};", start) + 1;
            String jsonData = html.substring(start, end);

            JSONObject obj = new JSONObject(jsonData);
            JSONArray contents = obj.getJSONObject("contents")
                    .getJSONObject("twoColumnSearchResultsRenderer")
                    .getJSONObject("primaryContents")
                    .getJSONObject("sectionListRenderer")
                    .getJSONArray("contents")
                    .getJSONObject(0)
                    .getJSONObject("itemSectionRenderer")
                    .getJSONArray("contents");

            for (int i = 0; i < contents.length(); i++) {
                JSONObject videoRenderer = contents.getJSONObject(i).optJSONObject("videoRenderer");
                if (videoRenderer == null) continue;

                String videoId = videoRenderer.getString("videoId");
                String title = videoRenderer
                        .getJSONObject("title")
                        .getJSONArray("runs")
                        .getJSONObject(0)
                        .getString("text");

            	songs.add(new SongItem(videoId, title.replace("È", "è"), "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg"));
            }
        } else {
        	songs.add(new SongItem("--", "Error while getting videos", "https://pixelpc.fr/art169.png"));
        }
        return songs;
    }
	
	public static String visitSite(String urly) {
	    StringBuilder stuff = new StringBuilder();
	    try {
	        URL url = new URL(urly);
	        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
	        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

	        try (BufferedReader in = new BufferedReader(
	                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
	            String line;
	            while ((line = in.readLine()) != null) {
	                stuff.append(line);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return stuff.toString();
	}

}
