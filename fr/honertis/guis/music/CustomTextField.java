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
	private int cursorPosition = 0;

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
		Gui.drawRoundedRect(x, y, x + width, y + height, 5, new Color(0, 0, 0, 40).getRGB());
		Gui.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 5, focused ? 0xFF333333 : 0xFF555555);

		int maxWidth = (int) width - 8;

		int start = 0;
		String displayText = text;
		while (mc.fontRendererObj.getStringWidth(displayText) > maxWidth && start < cursorPosition) {
			start++;
			displayText = text.substring(start);
		}

		mc.fontRendererObj.drawString(displayText, (int) (x + 4), (int) (y + (height - 8) / 2), 0xFFFFFF);

		if (focused && (cursorCounter / 10) % 2 == 0) {
			int cursorInDisplay = cursorPosition - start;
			if (cursorInDisplay < 0)
				cursorInDisplay = 0;
			if (cursorInDisplay > displayText.length())
				cursorInDisplay = displayText.length();
			int cursorX = mc.fontRendererObj.getStringWidth(displayText.substring(0, cursorInDisplay));
			mc.fontRendererObj.drawString("_", (int) (x + 4 + cursorX), (int) (y + (height - 8) / 2), 0xFFFFFF);
		}

		if (text.isEmpty() && !focused) {
			mc.fontRendererObj.drawString(LangManager.format("gui.musicPlayer.search"), (int) (x + 4),
					(int) (y + (height - 8) / 2), new Color(155, 155, 155).getRGB());
		}
	}
	private int keyRepeatCounter = 0;
	private int lastKeyPressed = -1;
	public void keyTyped(char typedChar, int keyCode) {
		if (!focused)
			return;
		
		lastKeyPressed = keyCode;
	    keyRepeatCounter = 0; 
	    
		handleKey(keyCode, typedChar);
	}

	private void handleKey(int keyCode, char typedChar) {
		if (keyCode == Keyboard.KEY_BACK && cursorPosition > 0) {
			text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
			cursorPosition--;
		} else if (keyCode == Keyboard.KEY_DELETE && cursorPosition < text.length()) {
			text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
		} else if (keyCode == Keyboard.KEY_LEFT && cursorPosition > 0) {
			cursorPosition--;
		} else if (keyCode == Keyboard.KEY_RIGHT && cursorPosition < text.length()) {
			cursorPosition++;
		} else if (GuiScreen.isCtrlKeyDown() && keyCode == Keyboard.KEY_V) {
			String paste = GuiScreen.getClipboardString();
			text = text.substring(0, cursorPosition) + paste + text.substring(cursorPosition);
			cursorPosition += paste.length();
		} else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
			text = text.substring(0, cursorPosition) + typedChar + text.substring(cursorPosition);
			cursorPosition++;
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY) {
		this.focused = (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height);
	}

	public void update() {
		cursorCounter++;
		if (focused && lastKeyPressed != -1 && Keyboard.isKeyDown(lastKeyPressed)) {
			keyRepeatCounter++;
			if (keyRepeatCounter > 10) {
				handleKey(lastKeyPressed, '\0');
			}
		} else {
			lastKeyPressed = -1;
	        keyRepeatCounter = 0;
		}
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