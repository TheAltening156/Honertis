package fr.honertis.module;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import fr.honertis.event.Events;
import fr.honertis.settings.*;
import fr.honertis.settings.Settings;
import net.minecraft.client.Minecraft;

public class ModuleBase extends Events{
	public String name,
				  desc;
	public boolean enabled;
	public Category cat;
	public Minecraft mc = Minecraft.getMinecraft();
	public List<Settings> settings = Lists.newArrayList();
	public boolean showSettings;
	
	public ModuleBase(String name, String desc, Category cat) {
		this.name = name;
		this.desc = desc;
		this.cat = cat;
	}
	
	public void addSettings(Settings... s) {
		settings.addAll(Arrays.asList(s));
		settings.sort(Comparator.comparingInt(ss -> ss instanceof BooleanSettings ? 1 : -1));
		settings.sort(Comparator.comparingInt(ss -> ss instanceof NumberSettings ? 1 : -1));
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
		setEnabled(!enabled);
		if (enabled) {
			onEnable(); 
		} else {
			onDisable();
		}
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void update() {}
}
