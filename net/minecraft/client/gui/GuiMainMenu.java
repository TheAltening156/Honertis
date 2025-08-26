package net.minecraft.client.gui;

import java.io.IOException;

import fr.honertis.Honertis;
import fr.honertis.utils.WebUtils;
import net.minecraft.client.resources.I18n;

public class GuiMainMenu extends GuiScreen {
	public Honertis instance = Honertis.INSTANCE;
	public boolean update;
	
	public GuiMainMenu() {
	}

	@Override
	public void initGui() {
		super.initGui();
		if (instance.update) {
			System.out.println("Nouvelle version de Honertis disponible ! https://bit.ly/honertis");
			this.buttonList.add(new GuiButton(0, this.width / 2 - 105, this.height / 2 + 40, 100, 20, "Mettre à jour"));
			this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height / 2 + 40, 100, 20, "Ignorer"));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 105, this.height / 2 + 62, 210, 20, "Vous pouvez aussi utiliser le launcher !"));
		} else {
			this.mc.displayGuiScreen(new McMainMenu());
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);

		mc.fontRendererObj.drawCenteredString("Nouvelle version de Honertis disponible !", this.width / 2, this.height / 2 - 50, -1);
		mc.fontRendererObj.drawCenteredString("Vous êtes toujours en " + instance.version + ", la version actuelle est la " + WebUtils.currentVersion, this.width / 2, this.height / 2 - 50 + mc.fontRendererObj.FONT_HEIGHT, -1);
		if (update) {
			mc.fontRendererObj.drawCenteredString("Ouverture de la page web ...", this.width / 2, this.height / 2 + 1, -1);
			
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (!update) {
			if (button.id == 0) {
				update = true;
				WebUtils.browseWebsite("https://bit.ly/honertis");	
			}
			if (button.id == 2) {
				update = true;
				WebUtils.browseWebsite("https://github.com/TheAltening156/HonertisLauncher/releases/");
			}
			try {
				Thread.sleep(1500L);
				update = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 1) {
			instance.update = false;
			this.mc.displayGuiScreen(new McMainMenu());
		}
		
	}
	
}
