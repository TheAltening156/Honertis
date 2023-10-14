package fr.honertis.guis;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;

import fr.honertis.Honertis;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.modules.HitColor;
import fr.honertis.settings.NumberSettings;
import fr.honertis.settings.Settings;
import fr.honertis.utils.DrawUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiHonertisOptions extends GuiScreen {
	public GuiScreen parent;
	public boolean clicked;
	
	public GuiHonertisOptions(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, "Back"));
		super.initGui();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int wheel = -Mouse.getDWheel();
		if (wheel != 0)
		System.out.println("t " + wheel);
		drawDefaultBackground();
		mc.fontRendererObj.drawCenteredStringWithShadow("Honertis Options", this.width / 2, 10, -1);
		int posY = 50;
		for (Category c : Category.values()) {
			mc.fontRendererObj.drawCenteredStringWithShadow(c.name, this.width / 2, posY, -1);
			int modX = this.width / 2 + 60;
			int modY = posY + 23;
			for (ModuleBase m : Honertis.modulesManager.modules) {
				int settsX = modX - 95;
				int settsY = modY + 25;
				if (m.cat == c) {
					mc.fontRendererObj.drawStringWithShadow(m.name, modX - 175, modY, -1);
					drawRect(modX + 35, modY - 4, modX + 65, modY + 11, m.isEnabled() ? new Color(0, 205, 0, 190).getRGB() : new Color(205, 0, 0, 190).getRGB() );
					mc.fontRendererObj.drawCenteredString(m.isEnabled() ? "I" : "O", modX + 50, modY, -1);
					drawRect(  modX + (m.isEnabled() ? 60 : 34), modY - 5, modX + (m.isEnabled() ? 66 : 40), modY + 12, -1);
				
					if (!m.settings.isEmpty()) {
						boolean hover = isHovered(modX + 8, modY - 5, (modX + 8) + 18, (modY - 5) + 18, mouseX, mouseY);
						DrawUtils.drawImage(modX + 8, modY - 5, 18, 18, new ResourceLocation("honertis/settings" + (hover ? (m.showSettings ? "OH" : "H") : m.showSettings ? "O" : "C") + ".png"));
					}
					
					if (m.showSettings) {
						HitColor clr = (HitColor) Honertis.modulesManager.getModuleByName("HitColor");
						if (m.name.equals(clr.name)) {
							drawRect(settsX + 165, settsY + 15, settsX + 190, settsY + 40, new Color((int)clr.r.getDefValue(), (int)clr.g.getDefValue(), (int)clr.b.getDefValue(), (int)clr.a.getDefValue()).getRGB());
							mc.fontRendererObj.drawString("rendu :", settsX + 160, settsY, -1);
						}
					}
					
					for (Settings s : m.settings) {
						if (!m.settings.isEmpty() && m.showSettings && s.show) {
							int width = settsX + 127;
							if (s instanceof NumberSettings) {
								NumberSettings num = (NumberSettings) s;
								double min = num.getMin();
								double max = num.getMax();
								double rdrWidth = 127 * ((num.getDefValue() - min) / (max - min));
								Gui.drawRect(settsX, settsY + 3, settsX + 127, settsY + 7, -1);
								Gui.drawRect(settsX - 3 + (int)rdrWidth, settsY - 3, settsX + 3 + (int)rdrWidth, settsY + 13, -1);
								mc.fontRendererObj.drawStringWithShadow(String.format("%.0f", num.getDefValue()), settsX + 133, settsY + 1, -1);
								
								double mouseSnap = mouseX;
								mouseSnap = Math.max(settsX, mouseSnap);
								mouseSnap = Math.min(width, mouseSnap);
								double test = (mouseSnap - settsX)/(width-settsX);
								double diff = max - min;
								double val = min + test * diff;
		                       
								if (isHovered(settsX - 3, settsY - 3, settsX + 127, settsY + 13, mouseX, mouseY) && clicked) {
									num.setValue(num.getIncrement() == 1 ? (int) val : val);
								}
		                      
							}
		                    mc.fontRendererObj.drawString(s.name, settsX - 70, settsY, -1);
		                    posY += 25;
							modY += 25;
		                    settsY += 25;
						}
					}
					modY += 25;
					posY += 25;
				}
			}
			posY += 25;
		}
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
		int posY = 50;
		for (Category c : Category.values()) {
			int modX = this.width / 2 + 60;
			int modY = posY + 23;
			for (ModuleBase m : Honertis.modulesManager.modules) {
				int settsX = modX - 95;
				int settsY = modY + 25;
				if (m.cat == c) {
					if (mouseButton == 0) {
						if (isHovered(modX + 35, modY - 5, modX + 66, modY + 12, mouseX, mouseY)) {
							m.toggle();
						}
						if (isHovered(modX + 8, modY - 5, (modX + 8) + 18, (modY - 5) + 18, mouseX, mouseY)) {
							m.showSettings = !m.showSettings;
						}
					}
					for (Settings s : m.settings) {
						if (!m.settings.isEmpty() && m.showSettings && s.show) {
							if (s instanceof NumberSettings) {
								if (isHovered(settsX - 3, settsY - 3, settsX + 127, settsY + 13, mouseX, mouseY) && mouseButton == 0) {
									clicked = true;
								}
							}
							posY += 25;
							modY += 25;
		                    settsY += 25;
						}
					}
					modY += 25;
					posY += 25;
				}
			}
			posY += 25;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		clicked = false;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
	}
}
