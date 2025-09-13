package fr.honertis.guis.music;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import fr.honertis.Honertis;

public class MusicPlayer {

	private SourceDataLine line;
	private AudioInputStream audioStream;
	public Thread playThread;
	boolean paused = true;
	private Object pauseLock = new Object();
	public long durationMillis = 0;
	public long currentStateMillis = 0;
	public File currentFile;
	public File lastFile;
	public boolean wasYoutube;
	
	public void play(File file, boolean isYoutube) throws Exception {
		currentFile = file;
		paused = false;
		stop();
		
	    deleteLastFile();

	    audioStream = AudioSystem.getAudioInputStream(file);
	    
	    AudioFormat format = audioStream.getFormat();
	    AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
	    long frameLength = fileFormat.getFrameLength();
        if (frameLength > 0 && format.getFrameRate() > 0) {
            double durationSeconds = (double) frameLength / format.getFrameRate();
            durationMillis = (long) (durationSeconds * 1000);
        } else {
            durationMillis = -1;
        }
	    AudioFormat targetFormat = new AudioFormat(
	            AudioFormat.Encoding.PCM_SIGNED,
	            44100, 16, 2, 4, 44100, false
	    );
	    audioStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);

	    DataLine.Info info = new DataLine.Info(SourceDataLine.class, targetFormat);
	    line = (SourceDataLine) AudioSystem.getLine(info);
	    line.open(targetFormat);
	    setVolume(Honertis.INSTANCE.musicPlayer.volume);
	    line.start();
		wasYoutube = isYoutube;
		lastFile = file;
	    playThread = new Thread(() -> {
	        try {
	            byte[] buffer = new byte[16384];
	            int bytesRead;
	            while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
	            	synchronized (pauseLock) {
	                    while (paused) pauseLock.wait();
	                }
	                if (paused) break;	                
	                long frames = line.getLongFramePosition();
	                double seconds = (double) frames / targetFormat.getFrameRate();
	                currentStateMillis = (long) (seconds * 1000);
	                line.write(buffer, 0, bytesRead);
	            }

	            line.drain();
	            stop();
	            currentStateMillis = durationMillis;
	            paused = true;
	            if (Honertis.INSTANCE.musicPlayer.repeat) playLastFile();
	        } catch (Exception e) {
	            //e.printStackTrace();
	        }
	    });
	    playThread.start();
	}
	
	public void deleteLastFile() {
    	try {
    		if (lastFile != null && lastFile != currentFile && wasYoutube) 
    			Files.delete(lastFile.toPath());
    	} catch (IOException e) {

    	}
        
	}
	public void deleteCurrentFile() {
    	try {
    		if (currentFile != null && wasYoutube) 
    			Files.delete(currentFile.toPath());
    	} catch (IOException e) {
    		
    	}
	}
	
	public void playLastFile() {
		if (lastFile == null) return;
		try {
			play(lastFile, wasYoutube);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		//paused = true;
		try {
			if (playThread != null && playThread.isAlive()) {
				playThread.interrupt();
			}
			if (line != null) {
				line.stop();
				line.close();
			}
			if (audioStream != null) {
				audioStream.close();
			}
		} catch (IOException ignored) {
		}
		line = null;
		audioStream = null;
		playThread = null;
	}
	
	public void pause() {
		paused = true;
	}

	public void resume() {
		synchronized (pauseLock) {
			paused = false;
			pauseLock.notifyAll();
		}
	}

	public void setVolume(int percent) {
		if (line == null)
			return;
		if (!line.isControlSupported(FloatControl.Type.MASTER_GAIN))
			return;

		FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		if (percent < 0)
			percent = 0;
		if (percent > 100)
			percent = 100;

		double linear = percent / 100.0;
		float dB;
		if (linear == 0.0)
			dB = volumeControl.getMinimum();
		else
			dB = (float) (20.0 * Math.log10(linear));

		if (dB < volumeControl.getMinimum())
			dB = volumeControl.getMinimum();
		if (dB > volumeControl.getMaximum())
			dB = volumeControl.getMaximum();

		volumeControl.setValue(dB);
	}

}
