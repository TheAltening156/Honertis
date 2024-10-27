package fr.honertis.module.modules;

import java.awt.Color;
import java.util.Timer;

import org.lwjgl.input.Keyboard;

import fr.honertis.event.EventRenderGui;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.MC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

public class KeyStrokes extends ModuleBase{    
    
	public KeyStrokes() {
		super("Keystrokes", "Affiche les touches de mouvement pressés", Category.UTILITIES);
	}
	
	public enum Keys implements MC{
		W(mc.gameSettings.keyBindForward, 50, 50),
		A(mc.gameSettings.keyBindLeft, 90, 90),
		S(mc.gameSettings.keyBindBack, 120, 120),
		D(mc.gameSettings.keyBindRight, 150, 150);
		public KeyBinding key;
		public int posX;
		public int posY;
		private float colorProgress = 0f;
	    private boolean animating = false;
	    private boolean toWhite = true;
	    private long lastTime;
		Keys(KeyBinding key, int posX, int posY) {
			this.key = key;
			this.posX = posX;
			this.posY = posY;
		}
		
		private void drawRect(int progress, int keyCode, int x, int y) {
			float colorValue = progress / 255.0f;
			int color = new Color(colorValue, colorValue, colorValue, colorValue / 2f).getRGB();
			FontRenderer fr = mc.fontRendererObj;
			Gui.drawRect(x, y, x + 22, y + 22, new Color(0,0,0, 125).getRGB());
			Gui.drawRect(x, y, x + 22, y + 22, color);
			Gui.drawRect(x, y, x + 22, y + 22, color);

			
			int textColorValue = 255 - progress;
	        int colorCode = new Color(textColorValue, textColorValue, textColorValue).getRGB();
	        //fr.drawCenteredString(Keyboard.getKeyName(keyCode), 63, 59, colorCode);
	        //x, y, x + 25, y + 25
	        fr.drawString(Keyboard.getKeyName(keyCode), x + 8, y + 7, colorCode);
		}
		
		
		private void startAnimation(boolean toWhiteDirection) {
	        animating = true;
	        toWhite = toWhiteDirection;
	        lastTime = System.currentTimeMillis();
	    }	

		private void updateAnimation() {
			if (animating) {
		        long currentTime = System.currentTimeMillis();
		        float deltaTime = (currentTime - lastTime) / 50.0f;
		        lastTime = currentTime;

		        if (toWhite) {
		            colorProgress = Math.min(255f, colorProgress + deltaTime * 255);
		        } else {
		            colorProgress = Math.max(0f, colorProgress - deltaTime * 255);
		        }

		        if ((toWhite && colorProgress >= 255f) || (!toWhite && colorProgress <= 0f)) {
		            animating = false;
		        }
		    }
	    }
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		for (Keys k : Keys.values()) {
			int posX = k.posX;
			int posY = k.posY;
			if (k == k.W) {
				posX = 23;
				posY = 70;
			}
			if (k == k.A) {
				posX = 0;
				posY = 93;
			}
			if (k == k.S) {
				posX = 23;
				posY = 93;
			}
			if (k == k.D) {
				posX = 46;
				posY = 93;
			}
			//mc.gameSettings.keyBindAttack = null; 
			//mc.gameSettings.keyBindPickBlock = null;
			//mc.gameSettings.keyBindUseItem = null;
			KeyBinding keys = k.key;
			k.updateAnimation();		
			k.drawRect((int) k.colorProgress, keys.getKeyCode(), posX, posY);
			if (!k.animating) {
				k.startAnimation(keys.isKeyDown() ? true : false);
			}
			
		}
	}
	
	@Override
	public void onUpdate(EventUpdate e) {
		
	}

	
}

