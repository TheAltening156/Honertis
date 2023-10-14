package fr.honertis.module;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.honertis.module.modules.*;

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
			new AutoGG());
	}
	
	
	
	private void add(ModuleBase... m) {
		modules.addAll(Arrays.asList(m));
	}



	public ModuleBase getModuleByName(String name) {
		for(ModuleBase m : modules) 
			if (m.getName().equalsIgnoreCase(name)) return m;
		
		return null;
	}
}

