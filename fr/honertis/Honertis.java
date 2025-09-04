package fr.honertis;

import fr.honertis.event.Event;
import fr.honertis.guis.music.MusicPlayer;
import fr.honertis.manager.FileManager;
import fr.honertis.manager.ResourcePackManager;
import fr.honertis.module.ModulesManager;
import fr.honertis.utils.WebUtils;

public class Honertis {
	public static Honertis INSTANCE = new Honertis();
	public String name;
	public String version;
	public String title;
	public boolean started;
	public ModulesManager modulesManager;
	public Event event;
	public DiscordRPCInit rpc;
	public ResourcePackManager packManager;
	public boolean update;
	public MusicPlayer musicPlayer;
	public String songName = "";
    public String thumbnail = "";
	
	public void start() {
		name = "Honertis";
		version = "1.7";
		title = "Minecraft 1.8.8 | Honertis v" + version;
		modulesManager = new ModulesManager();
		event = new Event();
		rpc = new DiscordRPCInit();
		packManager = new ResourcePackManager();
		started = true;
		FileManager.init();
		rpc.init();
		update = WebUtils.update();
		musicPlayer = new MusicPlayer();
	}

	public ResourcePackManager getPackManager() {
		return packManager;
	}
}
