package fr.honertis.module;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.honertis.module.modules.AutoGG;
import fr.honertis.module.modules.BlockHit;
import fr.honertis.module.modules.BlockTap;
import fr.honertis.module.modules.CPS;
import fr.honertis.module.modules.CustomTime;
import fr.honertis.module.modules.FPS;
import fr.honertis.module.modules.FishingRod;
import fr.honertis.module.modules.FreeLook;
import fr.honertis.module.modules.FullBright;
import fr.honertis.module.modules.HitColor;
import fr.honertis.module.modules.KeyStrokes;
import fr.honertis.module.modules.LowFire;
import fr.honertis.module.modules.MoreParticles;
import fr.honertis.module.modules.Ping;
import fr.honertis.module.modules.ToggleSprint;
import fr.honertis.settings.Settings;
import fr.honertis.utils.YamlUtils.ConfigMod;

public class ModulesManager {
	public static CopyOnWriteArrayList<ModuleBase> modules = new CopyOnWriteArrayList<ModuleBase>();

	public ModulesManager() {
		add(
			new BlockHit(),
			new FishingRod(),
			new BlockTap(),
			new HitColor(),
			new CPS(),
			new FPS(),
			new FullBright(),
			new Ping(),
			new MoreParticles(),
			new AutoGG(),
			new LowFire(),
			new ToggleSprint(),
			new KeyStrokes(),
			new CustomTime(),
			new FreeLook()
			/*new ToggleSneak()*/);
	}
	
	
	
	private void add(ModuleBase... m) {
		modules.addAll(Arrays.asList(m));
	}

	public ModuleBase getModuleByName(String name) {
		for(ModuleBase m : modules) 
			if (m.getName().equalsIgnoreCase(name)) return m;
		
		return null;
	}
	
	public static void loadModSett(ConfigMod mod) {
		mod.getModule().setEnabled(mod.isToggled());
		for (Settings s : mod.getSetts().keySet()) {
			mod.getModule().settings.remove(s);
			mod.getModule().settings.add(s);
		}
	}
}

