package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;

public class CustomTime extends ModuleBase {
	public static NumberSettings time = new NumberSettings("module.customtime.time", 6000, 0, 18000);
	
	public CustomTime() {
		super("CustomTime", "module.customtime", Category.UTILITIES);
		this.addSettings(time);
	}

	@Override
	public void onUpdate(EventUpdate e) {
		mc.theWorld.setWorldTime(time.getLongValue());
	}
	
}
