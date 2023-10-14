package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.NumberSettings;

public class HitColor extends ModuleBase{
	public NumberSettings r = new NumberSettings("Rouge", 180, 0, 255, 1),
						  g = new NumberSettings("Vert", 0, 0, 255, 1),
						  b = new NumberSettings("Bleu", 0, 0, 255, 1),
						  a = new NumberSettings("Alpha", 153, 0, 255, 1);
	public BooleanSettings bypass = new BooleanSettings("Bypass armure", true),
						   custom = new BooleanSettings("Valeur Personalisée", true);
	
	public HitColor() {
		super("HitColor", "Réajoute la couleur de coups d'une valeur personalisée", Category.OLD1_7);
		this.addSettings(r,g,b,a, custom,bypass);
	}
	@Override
	public void update() {
		r.show = g.show = b.show = a.show = bypass.show = custom.isToggled();
	}
	
}
