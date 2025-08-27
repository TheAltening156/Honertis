package fr.honertis.module;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import fr.honertis.event.Events;
import fr.honertis.module.modules.FreeLook;
import fr.honertis.settings.*;
import fr.honertis.settings.Settings;
import fr.honertis.utils.AnimUtils;
import fr.honertis.utils.LangManager;
import net.minecraft.client.Minecraft;

public class ModuleBase extends Events{
	private String name,
				  desc;
	protected boolean enabled;
	private Category cat;
	public Minecraft mc = Minecraft.getMinecraft();
	public List<Settings> settings = Lists.newArrayList();
	private boolean showSettings;
	
	public ModuleBase(String name, String desc, Category cat) {
		this.name = name;
		this.desc = desc;
		this.cat = cat;
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
}
