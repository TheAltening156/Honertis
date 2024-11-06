package fr.honertis.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Maps;

import fr.honertis.Honertis;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.ModulesManager;
import fr.honertis.settings.Settings;
import fr.honertis.utils.YamlUtils;
import fr.honertis.utils.YamlUtils.ConfigMod;

public class FileManager {
	public static File dir = new File("Honertis/");
	public static File configFile = new File(dir + "/config.yml");
	
	public static void init() {
		dir.mkdirs();
		loadConfig();
	}
	
	public static void save() {
		saveConfigFile();
		System.out.println("Fichier config sauvegardé.");

	}
	
	public static InputStream getFile(String path) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(dir + "/" + path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	private static void loadConfig() {
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			for (ConfigMod m : YamlUtils.getModSetts()) {
				Honertis.modulesManager.loadModSett(m);
			}
			save();
		}
	}
	
	private static void saveConfigFile() {
		OutputStream out = null;
		try {
			out = new FileOutputStream(configFile);
			Map<String, Object> data = Maps.newHashMap();
			for (ModuleBase m : ModulesManager.modules) {
				Map<String, Object> module = Maps.newHashMap();
				Map<String, Object> setts = Maps.newHashMap();
				module.put("enabled", m.isEnabled());
				for (Settings s : m.settings) {
					setts.put(s.name, s.getValue());
				}
				module.put("settings", setts);
				data.put(m.name, module);
			}
			out.write(new Yaml().dump(data).getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
