package fr.honertis;

import fr.honertis.event.Event;
import fr.honertis.manager.FileManager;
import fr.honertis.module.ModulesManager;
import fr.honertis.utils.WebUtils;

public class Honertis {
	public static String name = "Honertis";
	public static String version = "1.4";
	public static ModulesManager modulesManager;
	public static boolean started;
	public static Event event = new Event();
	public static DiscordRPCInit rpc = new DiscordRPCInit();
	
	public static void onStartup() {
		modulesManager = new ModulesManager();
		started = true;
		FileManager.init();
		rpc.init();
		if (WebUtils.update()) {
			System.out.println("Nouvelle version de Honertis disponible ! https://bit.ly/honertis");
		}
	}
}
