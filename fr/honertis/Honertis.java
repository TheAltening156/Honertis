package fr.honertis;

import fr.honertis.event.Event;
import fr.honertis.manager.FileManager;
import fr.honertis.manager.ResourcePackManager;
import fr.honertis.module.ModulesManager;
import fr.honertis.utils.WebUtils;

public class Honertis {
	public static Honertis INSTANCE = new Honertis();
	public String name = "Honertis";
	public String version = "1.4";
	public boolean started;
	public ModulesManager modulesManager;
	public Event event;
	public DiscordRPCInit rpc;
	public ResourcePackManager packManager;
	public boolean update;
	
	public void onStartup() {
		modulesManager = new ModulesManager();
		event = new Event();
		rpc = new DiscordRPCInit();
		packManager = new ResourcePackManager();
		started = true;
		FileManager.init();
		rpc.init();
		update = WebUtils.update();
	}

	public ResourcePackManager getPackManager() {
		return packManager;
	}
}
