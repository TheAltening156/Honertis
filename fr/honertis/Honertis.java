package fr.honertis;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.honertis.event.Event;
import fr.honertis.guis.music.CurrentPlayingSong;
import fr.honertis.guis.music.MusicPlayerGui;
import fr.honertis.manager.FileManager;
import fr.honertis.module.ModuleBase;
import fr.honertis.module.ModulesManager;
import fr.honertis.other.GameModeInfo;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
	public CurrentPlayingSong playingSong;
	public boolean isLauncher;
	public String launcherVersion;
	public Map<File, List<File>> subFolderPacks;
	public Thread packsThread = null;
	public GuiScreenResourcePacks guiScreenResourcePacks;
	
	public void start() {
		name = "Honertis";
		version = "1.9U1";
		title = "Minecraft 1.8.9 | " + name + " v" + version;
		
		modulesManager = new ModulesManager();
		event = new Event();
		rpc = new DiscordRPCInit();
		musicPlayer = new MusicPlayerGui();
		playingSong = new CurrentPlayingSong();
		FileManager.init();
		rpc.init();
		yalter.mousetweaks.Main.initialize(Constants.EntryPoint.UNDEFINED);
		GameModeInfo.init();
	}
	
	public <T extends ModuleBase> T getModule(Class<T> m) {
		return modulesManager.getModuleByClass(m);
	}

}
