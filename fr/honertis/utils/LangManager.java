package fr.honertis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LangManager implements MC{
	private static final Map<String, String> translations = new HashMap<String, String>();
	private static final Map<String, String> fallback = new HashMap<String, String>();
	private static String currentLang = "";
	
	public static void loadLang(String langCode) {
		translations.clear();
        fallback.clear();

        loadFile("en_US.lang", fallback);

        if (!langCode.equals("en_US")) {
            loadFile(langCode + ".lang", translations);
        } else {
        	translations.putAll(fallback);
        }
	}
	
	public static void loadFile(String fileName, Map<String, String> target) {
		String langPath = "/assets/minecraft/honertis/lang/" + fileName;
        InputStream file = null;
        File f = new File("assets/minecraft/honertis/lang/" + fileName);

        if (f.exists()) {
	        try {
				file = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
        
        if (file == null) file = LangManager.class.getResourceAsStream(langPath);
        
        if (file == null) {
        	System.out.println("Fichier introuvable :" + fileName);
        	return;
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("=")) continue;
                String[] parts = line.split("=", 2);
                target.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static String format(String key) {
		if (!mc.gameSettings.language.equals(currentLang)) {
	        currentLang = mc.gameSettings.language;
	        loadLang(currentLang);
	    }
        return translations.getOrDefault(key, fallback.getOrDefault(key, key));
	}
	
}
