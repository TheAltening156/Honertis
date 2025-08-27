package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.NumberSettings;

public class HitColor extends ModuleBase{
	public NumberSettings r = new NumberSettings("color.red", 180, 0, 255, 1),
						  g = new NumberSettings("color.green", 0, 0, 255, 1),
						  b = new NumberSettings("color.blue", 0, 0, 255, 1),
						  a = new NumberSettings("color.alpha", 153, 0, 255, 1);
	public BooleanSettings bypass = new BooleanSettings("module.hitcolor.bypass", true),
						   custom = new BooleanSettings("module.hitcolor.custom", true);
	
	public HitColor() {
		super("HitColor", "module.hitcolor", Category.UTILITIES);
		this.addSettings(r,g,b,a, custom,bypass);
	}
	@Override
	public void update() {
		r.show = g.show = b.show = a.show = bypass.show = custom.isToggled();
	}
	
}
