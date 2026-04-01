package fr.honertis.guis;

import java.awt.Color;
import java.io.IOException;

import fr.honertis.Honertis;
import fr.honertis.guis.alts.GuiAltManager;
import fr.honertis.utils.LangManager;
import fr.honertis.utils.WebUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.src.Config;

public class GuiHonertisOptions extends GuiScreen {
	public GuiScreen parent;
	public GuiButton discordButton;
	public GuiButton websiteButton;
	public GuiButton altButton;
	
	public GuiHonertisOptions(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, I18n.format("gui.back", new Object[0])));
		buttonList.add(new GuiButton(1, this.width / 2 - 152, this.height/2 - 21, 150, 20, LangManager.format("gui.honertis.moduleOptions.name")));
		buttonList.add(new GuiButton(2, this.width / 2 + 2,   this.height/2 - 21, 150, 20, LangManager.format("gui.honertis.credits.name")));
		buttonList.add(websiteButton = new GuiButton(3, this.width / 2 - 152, this.height/2 + 1, 150, 20, LangManager.format("gui.honertis.website")));
		buttonList.add(discordButton = new GuiButton(4, this.width / 2 + 2,   this.height/2 + 1, 150, 20, LangManager.format("gui.honertis.discord")));
		buttonList.add(altButton = new GuiButton(5, this.width / 2 - 75,   this.height/2 + 25, 150, 20, LangManager.format("gui.honertis.altManager")));
		altButton.enabled = Honertis.INSTANCE.isLauncher;
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
		mc.fontRendererObj.drawStringWithShadow("Minecraft 1.8.9", 2, height - mc.fontRendererObj.FONT_HEIGHT*2, Color.GRAY.getRGB());
		mc.fontRendererObj.drawStringWithShadow(Config.VERSION, 2, height - mc.fontRendererObj.FONT_HEIGHT, Color.GRAY.getRGB());
		mc.fontRendererObj.drawStringWithShadow(Honertis.INSTANCE.name + " v" + Honertis.INSTANCE.version, width - 1 - mc.fontRendererObj.getStringWidth(Honertis.INSTANCE.name + " v" + Honertis.INSTANCE.version), height - (mc.fontRendererObj.FONT_HEIGHT*(Honertis.INSTANCE.isLauncher ? 2 : 1)), Color.GRAY.getRGB());
		if (Honertis.INSTANCE.isLauncher) {
			mc.fontRendererObj.drawStringWithShadow("Launcher v" + Honertis.INSTANCE.launcherVersion, width - 1- mc.fontRendererObj.getStringWidth("Launcher v" + Honertis.INSTANCE.launcherVersion), height - mc.fontRendererObj.FONT_HEIGHT, Color.GRAY.getRGB());
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (isHovered(this.width/2 - 75, this.height / 2 + 25, this.width / 2 - 75 + 150, this.height/2 + 25 + 20, mouseX, mouseY) && !altButton.enabled) {
			drawRect(mouseX + 7, mouseY + 6, mouseX + 15 + mc.fontRendererObj.getStringWidth(LangManager.format("gui.honertis.altWarn")), mouseY - mc.fontRendererObj.FONT_HEIGHT, new Color(0,0,0,128).getRGB());
			mc.fontRendererObj.drawStringWithShadow(LangManager.format("gui.honertis.altWarn"), mouseX + 10.5, mouseY - 5, -1);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (!button.enabled) return;
		
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiModuleOptions(this));
		}
		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiHonertisCredits(this));
		}
		if (button.id == 3) {
			WebUtils.browseWebsite("https://thealtening156.github.io/HonertisWebSite/");
			new Thread(() -> {
				try {
					websiteButton.enabled = false;
					Thread.sleep(5000L);
					websiteButton.enabled = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
		if (button.id == 4) {
			WebUtils.browseWebsite("https://discord.gg/hrCY2ZtF5g");
			new Thread(() -> {
				try {
					discordButton.enabled = false;
					Thread.sleep(5000L);
					discordButton.enabled = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
		if (button.id == 5) {
			this.mc.displayGuiScreen(new GuiAltManager(this));
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
}
