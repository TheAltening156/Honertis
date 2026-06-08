package fr.honertis.guis.alts;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import fr.honertis.Honertis;
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
	public List<String> account;
	public boolean showAccounts;
	public GuiButton connectButton; 
	public String action;
	public int actionColor;
	
	public GuiAltManager(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		accountName = null;
		setActionTextAndColor(LangManager.format("gui.honertis.altManager.action"), Color.GRAY);
		showAccounts = false;
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, I18n.format("gui.back", new Object[0])));
		buttonList.add(connectButton = new GuiButton(1, this.width / 2 - 100, this.height - 47, LangManager.format("gui.honertis.altManager.connect")));
		noAccount.put(mc.session.getUsername(), new AccountData(mc.session.getUsername(), null));
		account = new ArrayList<String>();
		try {
			accs = loadAccounts();
			for (String entry : accs.keySet()) {
				if (accountName == null) accountName = entry;
				account.add(entry);
			}
		} catch (Exception e) {
			accs = noAccount;
			e.printStackTrace();
		}
		if (accs == noAccount) connectButton.enabled = false;
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
		if (accs == noAccount) {
			drawRect(posX - 50, posY + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + 13.5, isHovered(posX - 50, posY + mc.fontRendererObj.FONT_HEIGHT/2 - 9.5, posX + 50, posY + 13.5, mouseX, mouseY) ? new Color(255,255,255,50).getRGB() : new Color(25,25,25,128).getRGB());
			mc.fontRendererObj.drawCenteredStringWithShadow(accountName, posX, posY, -1);
		} else {
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
	}
	
	@Override
	public void updateScreen() {
		if (accs == noAccount) showAccounts = false;
	}
	
	private void setActionTextAndColor(String text, Color color) {
		action = text;
		actionColor = color.getRGB();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (accs != noAccount) {
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
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (!button.enabled) return;
		if (button.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
		if (button.id == 1 && accs != noAccount) {
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
	
    public File accountJson = new File(Utils.workdir, "accounts.json");
    public Map<String, AccountData> noAccount = new HashMap<String, AccountData>();
    
    public Map<String, AccountData> loadAccounts() throws IOException {
		if (!accountJson.exists()) 
			return noAccount;

	    Gson gson = new GsonBuilder().setPrettyPrinting().create();

	    try (Reader reader = new FileReader(accountJson)) {
	        Type type = new TypeToken<Map<String, AccountData>>(){}.getType();
	        Map<String, AccountData> accounts = gson.fromJson(reader, type);
	        if (accounts != null && !accounts.isEmpty()) {
	        	return accounts;
	        }
	    }catch (Exception e) {
	    	backupBadJson();
	    	throw new IOException("Fichier json corrompu ou invalide.");
	    }
	    try (Reader reader = new FileReader(accountJson)) {
	    	AccountData oldAccount = gson.fromJson(reader, AccountData.class);
	    	
	    	if (oldAccount == null || oldAccount.getUsername() == null) {
	    		throw new IOException("Ancen format invalide.");
	    	}
	    	
	    	Files.copy(
	    			accountJson.toPath(),
	    			new File(Utils.workdir, "accounts.json.bak").toPath(), 
	    			StandardCopyOption.REPLACE_EXISTING
	    	);
	    	
	    	Map<String, AccountData> accounts = new HashMap<String, AccountData>();
	    	accounts.put(oldAccount.getUsername(), oldAccount);
	    	
	    	try (FileWriter writer = new FileWriter(accountJson)) {
	    		gson.toJson(accounts, writer);
	    	}
	    	return accounts;
	    } catch (Exception e) {
	    	backupBadJson();
	    	throw new IOException("Impossible de charger ou de convertir accounts.json", e);
	    }
	}
    
    public void backupBadJson() throws IOException {
		Files.move(
    			accountJson.toPath(), 
    			new File(Utils.workdir, "accountsBAD.json").toPath(),
    			StandardCopyOption.REPLACE_EXISTING
    	);
	}

}
