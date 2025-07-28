package fr.honertis.utils;

import java.util.ArrayList;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.honertis.Honertis;
import fr.honertis.manager.FileManager;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.ModulesManager;
import fr.honertis.settings.Settings;

public class YamlUtils {
	public static ArrayList<ConfigMod> getModSetts() {
		ArrayList<ConfigMod> out = Lists.newArrayList();
		Map<String, Object> mods = (Map<String, Object>) new Yaml().load(FileManager.getFile("config.yml"));
		for (String s : mods.keySet()) {
			try {
				out.add(new ConfigMod(s, (Map<String, Object>)mods.get(s)));
			} catch (InvalidConfig i) {
				System.out.println("Une erreur dans la configuration a été détectée. Rien de grave, nous nous en occupons");
			}
		}
		return out;
	}
	
	public static class ConfigMod {
		private ModuleBase module;
		private Map<Settings, Object> setts;
		private boolean toggled;
		
		public ConfigMod(String modName, Map<String, Object> mod) throws InvalidConfig{
			this.setts = Maps.newHashMap();
			if (!(mod.get("enabled") instanceof Boolean) || !(mod.get("settings") instanceof Map)) throw new InvalidConfig();
			this.module = Honertis.INSTANCE.modulesManager.getModuleByName(modName);
			if (this.module == null) throw new InvalidConfig();
			this.toggled = (boolean) mod.get("enabled");
			Map<String, Object> setts = (Map<String, Object>) mod.get("settings");
			for (String key : setts.keySet()) {
				for (Settings s : this.module.settings) {
					if (key.equals(s.name)) {
						s.setValue(setts.get(key));
						break;
					}
				}
			}
		}
		
		public ModuleBase getModule() {
			return module;
		}
		public void setModule(ModuleBase module) {
			this.module = module;
		}
		public Map<Settings, Object> getSetts() {
			return setts;
		}
		public void setSetts(Map<Settings, Object> setts) {
			this.setts = setts;
		}
		public boolean isToggled() {
			return toggled;
		}
		public void setToggled(boolean toggled) {
			this.toggled = toggled;
		}
		
	}
	public static class InvalidConfig extends Throwable {}
 }
