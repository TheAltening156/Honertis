package fr.honertis;

import fr.honertis.event.Event;
import fr.honertis.manager.FileManager;
import fr.honertis.module.ModulesManager;

public class Honertis {
	public static String name = "Honertis";
	public static String version = "1.2";
	public static ModulesManager modulesManager;
	public static boolean started;
	public static Event event = new Event();
	
	public static void onStartup() {
		modulesManager = new ModulesManager();
		started = true;
		FileManager.init();
	}
}
