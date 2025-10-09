package fr.honertis;

import fr.honertis.event.Event;
import fr.honertis.guis.GuiHonertisOptions;
import fr.honertis.guis.music.MusicPlayer;
import fr.honertis.guis.music.MusicPlayerGui;
import fr.honertis.manager.FileManager;
import fr.honertis.module.ModulesManager;
import fr.honertis.utils.WebUtils;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import yalter.mousetweaks.Constants;

public class Honertis {
	public static Honertis INSTANCE = new Honertis();
	public String name;
	public String version;
	public String title;
	public ModulesManager modulesManager;
	public Event event;
	public DiscordRPCInit rpc;
	public boolean update;
	public MusicPlayerGui musicPlayer;
	private GuiHonertisOptions optionsGUI;
	
	public void start() {
		name = "Honertis";
		version = "1.7U3";
		title = "Minecraft 1.8.8 | Honertis v" + version;
		
		modulesManager = new ModulesManager();
		event = new Event();
		rpc = new DiscordRPCInit();
		musicPlayer = new MusicPlayerGui();
		FileManager.init();
		rpc.init();
		yalter.mousetweaks.Main.initialize(Constants.EntryPoint.UNDEFINED);
	}

	public GuiScreen getOptionsGUI(GuiScreen guiScreen) {
		return optionsGUI == null ? optionsGUI = new GuiHonertisOptions(guiScreen) : optionsGUI;
	}
}
