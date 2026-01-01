package fr.honertis.module;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import fr.honertis.event.Events;
import fr.honertis.module.modules.FreeLook;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.KeyBindSettings;
import fr.honertis.settings.NumberSettings;
import fr.honertis.settings.Settings;
import fr.honertis.utils.LangManager;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ModuleBase extends Events{
	private String name,
				  desc;
	protected boolean enabled;
	private Category cat;
	public Minecraft mc = Minecraft.getMinecraft();
	public List<Settings> settings = Lists.newArrayList();
	private boolean showSettings;
	public boolean shown;
	
	public NumberSettings posX = new NumberSettings("posX", 0, 0, 1920, 1, false);
	public NumberSettings posY = new NumberSettings("posY", 0, 0, 1080, 1, false);
	public boolean isClicked;
	public double oldX;
	public double oldY;
	
	public ModuleBase(String name, String desc, Category cat) {
		this.name = name;
		this.desc = desc;
		this.cat = cat;
		this.shown = true;
	}
	
	public ModuleBase(String name, String desc, Category cat, boolean shown) {
		this.name = name;
		this.desc = desc;
		this.cat = cat;
		this.shown = shown;
	}

	public void addSettings(Settings... s) {
		settings.addAll(Arrays.asList(s));
		settings.sort(Comparator.comparingInt(ss -> ss instanceof BooleanSettings ? 1 : -1));
		settings.sort(Comparator.comparingInt(ss -> ss instanceof NumberSettings ? 1 : -1));
		settings.sort(Comparator.comparingInt(ss -> ss instanceof KeyBindSettings ? 1 : -1));
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return LangManager.format(desc);
	}
	public Category getCat() {
		return cat;
	}
	public void setCat(Category cat) {
		this.cat = cat;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isShown() {
		return shown;
	}
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	
	public void toggle() {
		if (this instanceof FreeLook && mc.currentScreen != null) return;
		enabled = !enabled;
		if (enabled) {
			onEnable(); 
		} else {
			onDisable();
		}
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void update() {}

	public boolean showSettings() {
		return showSettings;
	}

	public void toggleShowSettings() {
		showSettings = !showSettings;
	}
	
	public boolean mouseClicked(int mouseX, int mouseY) {
    	if (GuiScreen.isHovered(posX.getValue(), posY.getValue(), posX.getValue() + posX.getSize(), posY.getValue() + posY.getSize(), mouseX, mouseY) && isEnabled()) {
    		return isClicked = true;
    	}
    	return false;
    }
	
	public boolean mouseReleased(int mouseX, int mouseY) {
		if (isEnabled())
			return isClicked = false;
		return false;
    }
	
	public boolean drawScreen(int mouseX, int mouseY) { 
		if (isEnabled()) {
	    	if (isClicked) {
				posX.setDefValue(posX.getDefValue() + mouseX - oldX);
				posY.setDefValue(posY.getDefValue() + mouseY - oldY);
				Utils.calculate(posX, posY, new ScaledResolution(mc));
			}
			oldX = mouseX;
			oldY = mouseY;
			return true;
	    }	
		return false;
	}

}
