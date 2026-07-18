package fr.honertis.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.honertis.Honertis;
import fr.honertis.module.addons.MiniPlayer;
import fr.honertis.module.modules.*;
import fr.honertis.settings.Settings;
import fr.honertis.utils.YamlUtils.ConfigMod;

public class ModulesManager {
	public static CopyOnWriteArrayList<ModuleBase> modules = new CopyOnWriteArrayList<ModuleBase>();
	private volatile List<ModuleBase> cachedReversed = null;
	
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
			new FreeLook(),
			new DropSwing(),
			new ToggleSneak(),
			new Zoom(),
			new MouseTweaks(),
			new MiniPlayer(),
			new ItemPhysics(),
			new NorCamHurt(),
			new Saturation(),
			new Reach(),
			new Combo());
		
	}
	
	public List<ModuleBase> getModulesPriority() {
	    List<ModuleBase> local = cachedReversed;
	    if (local == null) {
	    	local = new ArrayList<>(modules);
	    	Collections.reverse(local);
	    	cachedReversed = local;
	    }
	    return local;
	}
	
	private void add(ModuleBase... m) {
		modules.addAll(Arrays.asList(m));
		cachedReversed = null;
	}

	public ModuleBase getModuleByName(String name) {
		for(ModuleBase m : modules) 
			if (m.getName().equalsIgnoreCase(name)) return m;
		
		return null;
	}
	
	public <T extends ModuleBase> T getModuleByClass(Class<T> clazz) {
		for (ModuleBase m : modules) 
			if (m.getClass().equals(clazz))
				return clazz.cast(m);
		
		return null;
	}
	
	public static void loadModSett(ConfigMod mod) {
		mod.getModule().setEnabled(mod.isEnabled());
		for (Settings s : mod.getSetts().keySet()) {
			mod.getModule().settings.remove(s);
			mod.getModule().settings.add(s);
		}
	}
}

