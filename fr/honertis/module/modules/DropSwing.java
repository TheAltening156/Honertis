package fr.honertis.module.modules;

import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;

public class DropSwing extends ModuleBase{
	public BooleanSettings armSwing = new BooleanSettings("module.dropSwing.arm", false);
	public BooleanSettings fastDropping = new BooleanSettings("Fast Dropping", true);

	public DropSwing() {
		super("1.15 Drop", "module.dropSwing", Category.UTILITIES);
		this.addSettings(fastDropping, armSwing);
	}
	
}
