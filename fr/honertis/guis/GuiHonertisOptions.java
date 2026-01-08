package fr.honertis.guis;

import java.io.IOException;

import fr.honertis.utils.LangManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiHonertisOptions extends GuiScreen {
	public GuiScreen parent;
	
	public GuiHonertisOptions(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		this.mc.displayGuiScreen(new GuiModuleOptions(parent));
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, I18n.format("gui.back", new Object[0])));
		buttonList.add(new GuiButton(1, this.width / 2 - 152, this.height/3 + 35 - 10, 150, 20, LangManager.format("gui.honertis.module.options.name")));
		buttonList.add(new GuiButton(2, this.width / 2 + 2,   this.height/3 + 35 - 10, 150, 20, LangManager.format("gui.honertis.gui.options.name")));
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.5, 1.5, 0);
		GlStateManager.translate(-this.width / 6, -this.height/6, 0);
		mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.honertis.options.name"), this.width/2, this.height / 4, -1);
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiModuleOptions(parent));
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}
	
}
