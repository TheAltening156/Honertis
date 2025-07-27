package fr.honertis.module.modules;

import java.awt.Color;
import java.util.Timer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import fr.honertis.event.EventRenderGui;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.NumberSettings;
import fr.honertis.utils.MC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;

public class KeyStrokes extends ModuleBase{    
	public static BooleanSettings space = new BooleanSettings("Barre espace", true);
    public static NumberSettings delay = new NumberSettings("Délai", 50, 1, 500, 1);
	
    public KeyStrokes() {
		super("Keystrokes", "Affiche les touches de mouvement pressés", Category.UTILITIES);
		this.addSettings(delay, space);
	}
	
	public enum Keys implements MC{
		W(mc.gameSettings.keyBindForward, 23, 70),
		A(mc.gameSettings.keyBindLeft, 0, 93),
		S(mc.gameSettings.keyBindBack, 23, 93),
		D(mc.gameSettings.keyBindRight, 46, 93),
		JUMP(mc.gameSettings.keyBindJump, 0, 116),
		LMB(mc.gameSettings.keyBindAttack, 0, 130),
		RMB(mc.gameSettings.keyBindUseItem, 34, 130);
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
			int x1 = 22;
			int y1 = 22;
			if (this == Keys.JUMP) {
				x1 = 68;
				y1 = 13;
			}else if (this == Keys.LMB || this == Keys.RMB) {
				x1 = 33;
				y1 = 24;
			}
			Gui.drawRect(x, y, x + x1, y + y1, new Color(0,0,0, 125).getRGB());
			Gui.drawRect(x, y, x + x1, y + y1, color);
			Gui.drawRect(x, y, x + x1, y + y1, color);
			
			int textColorValue = 255 - progress;
	        int colorCode = new Color(textColorValue, textColorValue, textColorValue).getRGB();
	        //fr.drawCenteredString(Keyboard.getKeyName(keyCode), 63, 59, colorCode);
	        //x, y, x + 25, y + 25
	        String name = this == Keys.JUMP ? "Jump" : this == Keys.LMB ? "LMB" : this == Keys.RMB ? "RMB" : Keyboard.getKeyName(keyCode);
	        x+=8;
	        y+=7;
	        if (this == Keys.JUMP) {
	        	x+=14;
	        	y-=4;
	        } else if (this == Keys.LMB || this == Keys.RMB) {
	        	y-=2;
	        	GlStateManager.pushMatrix();
				GlStateManager.scale(0.5, 0.5, 1);
				GlStateManager.translate(x, y, 1);
		        fr.drawString("Test", x + 7, y +20, colorCode);
				GlStateManager.popMatrix();
	        }
	        fr.drawString(name, x, y, colorCode);
	        
		}
		
		
		private void startAnimation(boolean toWhiteDirection) {
	        animating = true;
	        toWhite = toWhiteDirection;
	        lastTime = System.currentTimeMillis();
	    }	

		private void updateAnimation() {
			if (animating) {
		        long currentTime = System.currentTimeMillis();
		        float deltaTime = (currentTime - lastTime) / delay.getFloatValue();//50.0f
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
			
			//mc.gameSettings.keyBindAttack = null; 
			//mc.gameSettings.keyBindPickBlock = null;
			//mc.gameSettings.keyBindUseItem = null;
			if (!space.isToggled() && k == Keys.JUMP )return;
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

