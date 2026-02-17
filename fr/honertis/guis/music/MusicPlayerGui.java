package fr.honertis.guis.music;

import static net.minecraft.client.renderer.GlStateManager.*;

import static fr.honertis.utils.Utils.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
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
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.honertis.Honertis;
import fr.honertis.manager.FileManager;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.addons.MiniPlayer;
import fr.honertis.settings.NumberSettings;
import fr.honertis.utils.DrawUtils;
import fr.honertis.utils.LangManager;
import fr.honertis.utils.Utils;
import fr.honertis.utils.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;

public class MusicPlayerGui extends GuiScreen {
	public boolean clicked;
	public double posX;
	public double posY;
	public double oldX;
	public double oldY;
	public CustomTextField text = new CustomTextField(posX + 70, posY + 7, 150, 20);
	public static String currentSearch;
	
	public MusicPlayer musicPlayer = new MusicPlayer();
	public String songName = "";
    public String thumbnail = "";
	
	public String ytState = "";
	public List<SongItem> songs = createNewSongList();
	public List<SongItem> lastSongs;
	
	public int scrollOffset = 0;
    public int maxScroll = 0;
        
    public boolean isYoutube = true;
    public boolean repeat = false;
        
    private static final File BIN_DIR = new File("Honertis/musicPlayer/");
    private static final File songDir = new File(BIN_DIR + "/songs");
	private static final File localSongs = new File(BIN_DIR + "/localSongs");
	public MiniPlayer player = (MiniPlayer) Honertis.INSTANCE.modulesManager.getMobuleByClass(MiniPlayer.class);
	
	public static String ytdlp = "yt-dlp" + getOs();
	public static String ffmpeg = "ffmpeg" + getOs();
	private static String YTDLP_URL;
    private static  String FFMPEG_URL;
    
	static {
		ytdlp = "yt-dlp" + getOs();
        ffmpeg = "ffmpeg" + getOs();

        YTDLP_URL = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/" + ytdlp;
        FFMPEG_URL = "https://pixelpc.fr/" + ffmpeg;
	}
	
	private static String getOs() {
		if (Util.getOSType() == EnumOS.WINDOWS) {
			return ".exe";
		} else if (Util.getOSType() == EnumOS.LINUX) {
			return "_linux";
		} else if (Util.getOSType() == EnumOS.OSX) {
			return "_macos";
		} else {
			return "";
		}
	}
    
    public int volume = 75;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (isYoutube && Util.getOSType() == EnumOS.OSX && !Utils.isMacOSAtLeast(10,15)) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
					LangManager.format("gui.musicPlayer.macTooOld1") + getVersionFromInt(0) + "." + Utils.getVersionFromInt(1) + LangManager.format("gui.musicPlayer.macTooOld2"),
					LangManager.format("gui.error"), JOptionPane.ERROR_MESSAGE));
			isYoutube = false;
			refreshSongList();
		}
        ScaledResolution sr = new ScaledResolution(mc);
		if (isYoutube && songs != lastSongs)
			lastSongs = songs;
		if (!localSongs.exists()) localSongs.mkdirs();
		if (clicked) {
            posX += mouseX - oldX;
            posY += mouseY - oldY;
    		if (posX > sr.getScaledWidth() - 280) posX = sr.getScaledWidth() - 280;
        	if (posX <= -5) posX = -5;
    		if (posY > sr.getScaledHeight() - 254) posY = sr.getScaledHeight() - 254;
    		if (posY <= -5) posY = -5;
		}
		if (isYoutube)
			text.setPosition(posX + 70, posY + 5);

        for (double d : new double[]{0.5, 1}) {
            drawRoundedRect(posX + 4 - d, posY + 4 - d, posX + 281 + d, posY + 255 + d, 10, new Color(50, 50, 50, 50).getRGB());
        }
        drawRoundedRect(posX + 4, posY + 4, posX + 281, posY + 255, 10, new Color(50, 50, 50, 50).getRGB());
        drawRoundedRect(posX + 5, posY + 5, posX + 280, posY + 254, 10, new Color(50, 50, 50).getRGB());
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
        	
        	drawRoundedRect(position + 5, posY + 6, position + 13 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, 5, isHovered(position + 5, posY + 6, position + 13 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, mouseX, mouseY) ? new Color(150,150,150).getRGB() : new Color(100,100,100).getRGB());
        	mc.fontRendererObj.drawStringWithShadow(LangManager.format("gui.open"), (int)position + 10, (int)posY + 11, -1);
        	if (songs == songList && songList.size() < 1) {
        		mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.musicPlayer.wav"), posX + 280/2 + 5, posY + (210 + mc.fontRendererObj.FONT_HEIGHT)/2 + 5, -1);
            	drawRoundedRect(posX + 280/2 - mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2, posY + 120 + 5f, posX + 280/2 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2 + 7.5f, posY + 120 + 18 + 5f, 5, isHovered(posX + 280/2 - mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2, posY + 120 + 5f, posX + 280/2 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2 + 7.5f, posY + 120 + 18 + 5f, mouseX, mouseY) ? new Color(150,150,150).getRGB() : new Color(100,100,100).getRGB());
            	//drawRoundedRect(posX + 280/2 - 60, posY + (210 + mc.fontRendererObj.FONT_HEIGHT)/2 - 2, posX + 280/2 + 60, posY + (210 + mc.fontRendererObj.FONT_HEIGHT)/2 + 10, 2, -1);
            	mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.open"), posX + 285/2 + 2.5f, posY + 121 + mc.fontRendererObj.FONT_HEIGHT/2 + 5f, -1);

            	//posX + 280/2 - mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2, posY + 120
        	}
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
        
        for (SongItem song : songs) {
            int textX = (int) songPosX;
            int textY = (int) (songPosY + imageHeight + 5);
            int maxTextWidth = (int) imageWidth;
            String text = StringEscapeUtils.unescapeHtml4(song.getTitle());
            text = replaceUpperCase( text.replace(".wav", ""));
            int textWidth = mc.fontRendererObj.getStringWidth(text + "       ");

            double offset = textScroll(textWidth, maxTextWidth, System.currentTimeMillis(), 0.75);
            
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
        
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        Honertis.INSTANCE.playingSong.draw(mc, replaceUpperCase(songName), thumbnail, ytState, posX, posY, musicPlayer, repeat, player.isEnabled(), width, height, mouseX, mouseY, 0);
        for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority()) 
			m.drawScreen(mouseX, mouseY);
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
		Honertis.INSTANCE.playingSong.mouseClick();
		
		if (isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY) && mouseButton == 0) {
			isYoutube = !isYoutube;
		}
		double position = posX + 79 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.refresh"));
		if (isHovered(posX + 5, posY + 5, posX + 280, posY + 26, mouseX, mouseY)) {
			if (!(isYoutube ? isHovered(posX + 70, posY + 6, posX + 220, posY + 24, mouseX, mouseY) : (isHovered(posX + 71, posY + 6, position, posY + 24, mouseX, mouseY)) || isHovered(position + 5, posY + 6, position + 15 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, mouseX, mouseY)) && !isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY))
			clicked = true;
		}
		
		if (!isHovered(posX + 5, posY + 5, posX + 280, posY + 255, mouseX, mouseY)) 
			for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority()) 
				if (m.mouseClicked(mouseX, mouseY))
					break;

		if (!isYoutube) {
			File dummyFile = new File(localSongs + "/Wav file only");
			if (!dummyFile.exists()) dummyFile.createNewFile();
			if (isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY) || isHovered(posX + 71, posY + 6, position, posY + 24, mouseX, mouseY) && mouseButton == 0) {
				refreshSongList();
			}
			if ((isHovered(position + 5, posY + 6, position + 13 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open")), posY + 24, mouseX, mouseY) || (isHovered(posX + 280/2 - mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2, posY + 120 + 5f, posX + 280/2 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.open"))/2 + 7.5f, posY + 120 + 18 + 5f, mouseX, mouseY)) && songs == songList && songList.size() < 1) && mouseButton == 0) {
				Utils.openPath(localSongs.getAbsolutePath(), localSongs);
			}
		} else {
			if (isHovered(posX + 15, posY + 9, posX + 27, posY + 21, mouseX, mouseY) && mouseButton == 0 && lastSongs != null)
				songs = lastSongs;
		}
		if (isYoutube)
			text.mouseClicked(mouseX, mouseY);
		
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
            	if (song.hover && mouseButton == 0 && !song.getTitle().equals("Error while getting videos")) {
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
			if (isHovered(posX + 173, posY + 225, posX + 173 + 18, posY + 225 + 18, mouseX, mouseY)) {
				if (!musicPlayer.paused) {
					musicPlayer.pause();
				} else { 
					if (musicPlayer.currentStateMillis == musicPlayer.durationMillis) 
						musicPlayer.playLastFile();
					else
						musicPlayer.resume();
				}
			}
			
			if (isHovered(posX + 148, posY + 225, posX + 148 + 18, posY + 225 + 18, mouseX, mouseY)) {
				musicPlayer.playLastFile();
			}
			if (isHovered(posX + 123, posY + 225, posX + 123 + 18, posY + 225 + 18, mouseX, mouseY)) {
				repeat = !repeat;
			}
			if (isHovered(posX + 198, posY + 225, posX + 198 + 18, posY + 225 + 18, mouseX, mouseY)) {
				musicPlayer.pause();
				musicPlayer.currentStateMillis = musicPlayer.durationMillis;
			}
			if (isHovered(posX + 98, posY + 225, posX + 98 + 18, posY + 225 + 18, mouseX, mouseY)) {
				player.setEnabled(!player.isEnabled());
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
	
	private void refreshSongList() {
		songs = createNewSongList();
		
		File [] files = localSongs.listFiles();
		ytState = "Searching for local songs ...";
		for (File f : files) {
			if (f.isFile() && f.getName().endsWith(".wav")) {
				ytState = "Found " + f.getName();
				songs.add(new SongItem("", f.getName(), null));	
			}
		}
		ytState = "";
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
	
	private void downloadAndPlaySong(String videoUrl, String outputName, SongItem song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        new Thread(() -> {
	        if (!BIN_DIR.exists()) BIN_DIR.mkdirs();
	        if (!songDir.exists()) songDir.mkdirs();
	
	        File ytDlp = new File(BIN_DIR, ytdlp);
	        if (!ytDlp.exists()) {
	        	downloadYtDlpAndSetExecutable(ytDlp);
	        }
	
	        if (isYoutube) {
	        	ytState = "Getting song from link: " + videoUrl;
		        ProcessBuilder pb = ytdlpProcessBuilder(outputName, null, videoUrl);
		        pb.redirectErrorStream(true);
		        try {
			        Process process = pb.start();
			
			        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			            String line;
			            while ((line = reader.readLine()) != null) {
			                System.out.println(line);
			                ytState = line;
			                if (line.startsWith("ERROR:")) {
								File ffmpeg = new File(BIN_DIR, this.ffmpeg);

								if (!ffmpeg.exists()) {
									try {
										ytState = "Downloding ffmpeg... ";
										downloadFile(FFMPEG_URL, ffmpeg);
										ffmpeg.setExecutable(true);
										try {
											if (Util.getOSType() != EnumOS.WINDOWS)
												new ProcessBuilder("chmod", "+x", ffmpeg.getAbsolutePath()).start().waitFor();
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
			                	downloadYtDlpAndSetExecutable(ytDlp);
			                	ytState = "Getting song from link: " + videoUrl;
			     		        pb = ytdlpProcessBuilder(outputName, ffmpeg, videoUrl);
			     		        
			     		        pb.redirectErrorStream(true);
			     		        try {
			     			        process = pb.start();
			     			
			     			        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			     			            String line1;
			     			            while ((line1 = reader1.readLine()) != null) {
			     			                System.out.println(line1);
			     			                ytState = line1;
			     			                if (line1.startsWith("ERROR:")) {
			     			     				ytState = "Unable to play song.";
			     			     				if (line1.contains("ffmpeg")) {
			     			     					if (!ffmpeg.exists()) {
			    			     			        	SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, 
			    			     			        			LangManager.format("gui.ffmpegNotFound"),
			    			     			        			LangManager.format("gui.error"), JOptionPane.ERROR_MESSAGE));
			    			     			        }
			     			     				}
			     			                }
			     			            }
			     			        }
			     		        } catch (IOException e) {
			     		        	e.printStackTrace();
			     		        }
			                }
			            }
			        }
		        } catch (IOException e) {
		        	e.printStackTrace();
		        }
		        try {
					musicPlayer.play(new File(songDir + "/" + outputName), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        } else {
	        	try {
					musicPlayer.play(new File(BIN_DIR + "/localSongs/" + song.getTitle()), false);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        ytState = "";
	        songName = song.getTitle();
			thumbnail = song.getThumbnailUrl();
        }).start();
	}
	
	private void downloadYtDlpAndSetExecutable(File ytDlp) {
		downloadYtDlp(ytDlp);
		ytDlp.setExecutable(true);
		try {
			if (Util.getOSType() != EnumOS.WINDOWS)
				new ProcessBuilder("chmod", "+x", ytDlp.getAbsolutePath()).start().waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ProcessBuilder ytdlpProcessBuilder(String outputName, File ffmpeg, String videoUrl) {
		return new ProcessBuilder(
        		BIN_DIR + "/" + ytdlp,
                "-x", "--audio-format", "wav",
                "-o", songDir + "/" + outputName,
                "--ffmpeg-location", ffmpeg.getAbsolutePath(),
                "--", videoUrl
        );
	}
	
	private void downloadYtDlp(File ytDlp) {
		try {
			ytState = "Downloading yt-dlp ...";
			downloadFile(YTDLP_URL, ytDlp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		clicked = false;
		player.isClicked = false;
		Honertis.INSTANCE.playingSong.releaseClick();
		for (ModuleBase m : Honertis.INSTANCE.modulesManager.getModulesPriority()) 
			if (!(m instanceof MiniPlayer))
				if (m.mouseReleased(mouseX, mouseY)) 
					break;
		
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public List<SongItem> songList;
	
	public List<SongItem> createNewSongList() {
		return songList = new ArrayList<SongItem>();
	}
	
	public List<SongItem> parseSongs(String json, String searchQuery) {
        List<SongItem> songs = createNewSongList();
        if (json.startsWith("{  \"kind")) {
            JSONObject obj = new JSONObject(json);
	        JSONArray items = obj.getJSONArray("items");
	
	        for (int i = 0; i < items.length(); i++) {
	            JSONObject item = items.getJSONObject(i);
	            JSONObject id = item.getJSONObject("id");
	            
	            if (id.getString("kind").equals("youtube#video")) {
		            JSONObject snippet = item.getJSONObject("snippet");
		
		            String videoId = id.getString("videoId");
		            String title = snippet.getString("title");
		
		            songs.add(new SongItem(videoId, title, "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg"));
	            }
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

            	songs.add(new SongItem(videoId, title, "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg"));
            }
        } else {
        	songs.add(new SongItem("--", "Error while getting videos", null));
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
