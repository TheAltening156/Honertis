package fr.honertis.guis;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import fr.honertis.utils.LangManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiHonertisCredits extends GuiScreen {
	public GuiScreen parent;
	
	public GuiHonertisCredits(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, I18n.format("gui.back", new Object[0])));
		super.initGui();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		int posY = 0;
		for (String txt : new String[] {"gui.honertis.credits.text1", "gui.honertis.credits.text2", "gui.honertis.credits.text3", "gui.honertis.credits.text4"}) {
			mc.fontRendererObj.drawCenteredString(LangManager.format(txt), this.width/2,  this.height/3 + posY, -1);
			posY += mc.fontRendererObj.FONT_HEIGHT*2;
		}
		
		mc.fontRendererObj.drawCenteredString("Credits screen", this.width/2, 15, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen(parent);
		}
	}
}
