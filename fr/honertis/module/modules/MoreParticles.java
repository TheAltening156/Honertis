package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;

public class MoreParticles extends ModuleBase{
	public NumberSettings particles = new NumberSettings("module.particles.multiply", 2, 2, 20, 1);
	
	public MoreParticles() {
		super("MoreParticles", "module.particles", Category.UTILITIES);
		this.addSettings(particles);
	}
	
	
	
}
