package fr.honertis.guis.music;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import fr.honertis.utils.LangManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public class CustomTextField {

    private double x, y, width, height;
    private String text = "";
    private boolean focused = false;
    private int cursorCounter = 0;

    public CustomTextField(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void draw(Minecraft mc) {
        Gui.drawRoundedRect(x, y, x + width, y + height, 5, new Color(0,0,0,40).getRGB());
        Gui.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 5, focused ? 0xFF333333 : 0xFF555555);
        
        mc.fontRendererObj.drawString(text, x + 4, y + (height - 8) / 2, 0xFFFFFF);

        if (focused && (cursorCounter / 10) % 2 == 0) {
            int textWidth = mc.fontRendererObj.getStringWidth(text);
            mc.fontRendererObj.drawString("_", x + 4 + textWidth, y + (height - 8) / 2, 0xFFFFFF);
        }
        
        if (text.isEmpty() && !focused) {
        	mc.fontRendererObj.drawString(LangManager.format("gui.musicPlayer.search"), x + 4, y + (height - 8) / 2, new Color(155,155,155).getRGB());
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!focused) return;

        if (keyCode == Keyboard.KEY_BACK && text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            text += typedChar;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.focused = (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height);
    }

    public void update() {
        cursorCounter++;
    }

    public String getText() {
        return text;
    }

	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public boolean isFocused() {
		return focused;
	}
}