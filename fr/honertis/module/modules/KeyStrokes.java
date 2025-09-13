package fr.honertis.module.modules;

import java.awt.Color;
import java.util.List;
import java.util.Timer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import fr.honertis.event.EventRenderGui;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.NumberSettings;
import fr.honertis.utils.MC;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;

public class KeyStrokes extends ModuleBase{    
	public static BooleanSettings space = new BooleanSettings("module.keystrokes.spacebar", true);
    public static NumberSettings delay = new NumberSettings("module.keystrokes.delay", 50, 1, 500, 1);
    
    public KeyStrokes() {
		super("Keystrokes", "module.keystrokes", Category.UTILITIES);
		this.addSettings(delay, space);
	}
	
	public enum Keys implements MC{
		W(mc.gameSettings.keyBindForward, 23, 70),
		A(mc.gameSettings.keyBindLeft, 0, 93),
		S(mc.gameSettings.keyBindBack, 23, 93),
		D(mc.gameSettings.keyBindRight, 46, 93),
		JUMP(mc.gameSettings.keyBindJump, 0, 116),
		LMB(mc.gameSettings.keyBindAttack, 0, 130),
		RMB(mc.gameSettings.keyBindUseItem, 35, 130);
		public KeyBinding key;
		public int posX;
		public int posY;
		private float colorProgress = 0f;
	    private boolean animating = false;
	    private boolean toWhite = true;
	    private long lastTime;
	    public boolean wasKeyDown = false;
	    public List<Long> lCps = Lists.newArrayList();
	    public List<Long> rCps = Lists.newArrayList();
	    public PressState leftButtonState;
	    public PressState rightButtonState;
	    
	    public int getLeftClicks() {
			return rCps.size();
		}
	    
	    public int getRightClicks() {
			return lCps.size();
		}
	    
		Keys(KeyBinding key, int posX, int posY) {
			this.key = key;
			this.posX = posX;
			this.posY = posY;
			leftButtonState = new PressState(lCps);
			rightButtonState = new PressState(rCps);
		}
		
		private void drawRect(int progress, int keyCode, int x, int y) {
			leftButtonState.isKeyPressed(Mouse.isButtonDown(0));
			rightButtonState.isKeyPressed(Mouse.isButtonDown(1));
			
			float colorValue = progress / 255.0f;
			int color = new Color(colorValue, colorValue, colorValue, colorValue).getRGB();
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
			int alpha = (int)(125 - colorProgress); 
			alpha = Math.max(0, Math.min(125, alpha)); 
			Color animatedColor = new Color(0, 0, 0, alpha);
			Gui.drawRect(x, y, x + x1, y + y1, animatedColor.getRGB());
			Gui.drawRect(x, y, x + x1, y + y1, color);

			int textColorValue = 255 - progress;
	        int colorCode = new Color(textColorValue, textColorValue, textColorValue).getRGB();

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
		        fr.drawCenteredString(((this == Keys.LMB) ? lCps.size() : rCps.size()) + " CPS", x + 16, y +21, colorCode);
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
		public class PressState {
	        public boolean pressed;
	        public long lastPress;
	        public List<Long> list;

	        PressState(List<Long> list) {
	            this.list = list;
	        }

	        public void isKeyPressed(boolean key) {
	        	if (mc.currentScreen == null) {
		            if (key != pressed) {
		                lastPress = System.currentTimeMillis();
		                pressed = key;
		                if (key) {
		                    list.add(lastPress);
		                }
		            }
	        	}
	            long currentTime = System.currentTimeMillis();
	            list.removeIf(aLong -> aLong + 1000 < currentTime);
	        }
	    }
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
	    for (Keys k : Keys.values()) {
	        if (!space.isToggled() && k == Keys.JUMP) continue;
	        int posX = k.posX;
	        int posY = k.posY;
	        KeyBinding keys = k.key;
	        boolean isDown = keys.isKeyDown();

	        if (k.wasKeyDown != isDown) {
	            k.startAnimation(isDown);
	        }
	        k.wasKeyDown = isDown;

	        k.updateAnimation();		
	        k.drawRect((int) k.colorProgress, keys.getKeyCode(), posX, posY);
	    }
	}
	
	@Override
	public void onUpdate(EventUpdate e) {
		
	}

}

