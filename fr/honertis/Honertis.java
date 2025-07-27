package fr.honertis;

import fr.honertis.event.Event;
import fr.honertis.manager.FileManager;
import fr.honertis.manager.ResourcePackManager;
import fr.honertis.module.ModulesManager;
import fr.honertis.utils.MC;
import fr.honertis.utils.WebUtils;

public class Honertis {
	public static String name = "Honertis";
	public static String version = "1.4";
	public static boolean started;
	public static ModulesManager modulesManager;
	public static Event event;
	public static DiscordRPCInit rpc;
	public static ResourcePackManager packManager;
	
	public static void onStartup() {
		modulesManager = new ModulesManager();
		event = new Event();
		rpc = new DiscordRPCInit();
		packManager = new ResourcePackManager();
		started = true;
		FileManager.init();
		rpc.init();
		if (WebUtils.update()) {
			System.out.println("Nouvelle version de Honertis disponible ! https://bit.ly/honertis");
			//MC.mc.displayGuiScreen(new UpdateScreen()); à ajouter après
		}
	}

	public static ResourcePackManager getPackManager() {
		return packManager;
	}
}
