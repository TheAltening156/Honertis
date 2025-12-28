package fr.honertis.guis;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.honertis.Honertis;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.modules.HitColor;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.KeyBindSettings;
import fr.honertis.settings.NumberSettings;
import fr.honertis.settings.Settings;
import fr.honertis.utils.AnimUtils;
import fr.honertis.utils.DrawUtils;
import fr.honertis.utils.LangManager;
import fr.honertis.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

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
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int color = new Color(90, 0, 180, 60).getRGB();
		int color1 = 0;
		drawDefaultBackground();
		/*drawGradientRect(0, 0, width, height/2, color, color1);
		drawGradientRect(0, height/2, width, height, color1, color);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		drawGradientRect(0, -width, height, 0, color1, color);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		drawGradientRect(0, -width, height, 0, color, color1);
		GlStateManager.popMatrix();*/
				
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
