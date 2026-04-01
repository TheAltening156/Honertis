package fr.honertis.guis.alts;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.honertis.utils.LangManager;
import fr.honertis.utils.Utils;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public class GuiAltManager extends GuiScreen{
	public GuiScreen parent;
	public Map<String, AccountData> accs;
	public String accountName;
	public String action;
	public int actionColor;
	public List<String> account;
	public boolean showAccounts;
	
	public GuiAltManager(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		accountName = null;
		action = LangManager.format("gui.honertis.altManager.action");
		showAccounts = false;
		actionColor = Color.GRAY.getRGB();
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, I18n.format("gui.back", new Object[0])));
		buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 47, LangManager.format("gui.honertis.altManager.connect")));
		account = new ArrayList<String>();
		try {
			accs = loadAccounts();
			for (String entry : accs.keySet()) {
				if (accountName == null) accountName = entry;
				account.add(entry);
			}
		} catch (Exception e) {
			accs = new HashMap<>();
			e.printStackTrace();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.5, 1.5, 0);
		GlStateManager.translate(-this.width / 6, -this.height/6, 0);
		mc.fontRendererObj.drawCenteredStringWithShadow(LangManager.format("gui.honertis.altManager"), this.width/2, this.height / 4, -1);
		GlStateManager.popMatrix();
		mc.fontRendererObj.drawCenteredStringWithShadow(action, width/2, height / 5f + mc.fontRendererObj.FONT_HEIGHT, actionColor);
		super.drawScreen(mouseX, mouseY, partialTicks);
		double posX = this.width/2;
		double posY = this.height/2;
		if (showAccounts) {
			double spacing = 0;
			for (String accName : account) {
				drawRect(posX - 50, posY + spacing + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + spacing + 13.5, isHovered(posX - 50, posY + spacing + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + spacing + 13.5, mouseX, mouseY) ? new Color(255,255,255,50).getRGB() : new Color(25,25,25,128).getRGB());
				mc.fontRendererObj.drawCenteredStringWithShadow(accName, posX, posY + spacing, -1);
				spacing += mc.fontRendererObj.FONT_HEIGHT + 10;
			}
		} else {
			drawRect(posX - 50, posY + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + 13.5, isHovered(posX - 50, posY + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + 13.5, mouseX, mouseY) ? new Color(255,255,255,50).getRGB() : new Color(25,25,25,128).getRGB());
			mc.fontRendererObj.drawCenteredStringWithShadow(accountName, posX, posY, -1);
		}
	}
	
	private void setActionTextAndColor(String text, Color color) {
		action = text;
		actionColor = color.getRGB();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		double posX = this.width/2;
		double posY = this.height/2;
		double spacing = 0;
		if (showAccounts) {
			for (int i = 0; i < account.size(); i++) {
				String accName = account.get(i);
				if (isHovered(posX - 50, posY + spacing + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + spacing + 13.5, mouseX, mouseY)) {
					showAccounts = false;
					account.remove(i);
					account.add(0, accName);
					accountName = accName;
					break;
				}
				spacing += mc.fontRendererObj.FONT_HEIGHT + 10;
			}
		} else {
			if (isHovered(posX - 50, posY + mc.fontRendererObj.FONT_HEIGHT/2 - 10, posX + 50, posY + 14, mouseX, mouseY)) {
				showAccounts = true;
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (!button.enabled) return;
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
		if (button.id == 1) {
			if (accountName == null) return;
			try {
				MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithRefreshToken(accs.get(accountName).refreshToken);
				mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "legacy");
				setActionTextAndColor(LangManager.format("gui.honertis.altManager.connected") + " " + accountName + ".", Color.GREEN);
			} catch (MicrosoftAuthenticationException e) {
				setActionTextAndColor(LangManager.format("gui.honertis.altManager.logInError") + ": " + e.getLocalizedMessage(), Color.GREEN);
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}
	
    public File accountsJson = new File(Utils.workdir, "accounts.json");

	public Map<String, AccountData> loadAccounts() throws IOException {
		if (!accountsJson.exists()) return new HashMap<>();

	    Gson gson = new Gson();

	    try (Reader reader = new FileReader(accountsJson)) {
	        Type type = new TypeToken<Map<String, AccountData>>(){}.getType();
	        return gson.fromJson(reader, type);
	    }
	}

}
