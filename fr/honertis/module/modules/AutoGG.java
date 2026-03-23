package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.TimeUtils;

public class AutoGG extends ModuleBase{
	public TimeUtils timer = new TimeUtils();
	public AutoGG() {
		super("AutoGG", "module.gg", Category.UTILITIES);
	}
	
	@Override
	public void onEnable() {
}
	
	@Override
	public void onUpdate(EventUpdate e) {
		boolean gg = eq(mc.ingameGUI.displayedTitle, "Victoire") || eq(mc.ingameGUI.displayedTitle, "Défaite") || eq(mc.ingameGUI.displayedTitle, "GG") || eq(mc.ingameGUI.displayedTitle, "VICTORY") || eq(mc.ingameGUI.displayedTitle, "DEFEAT");
		boolean gl = eq(mc.ingameGUI.displayedTitle, "Bonne chance") || eq(mc.ingameGUI.displayedTitle, "c'est parti");
		if (timer.hasTimeElapsed(60000, gg || gl))
		if (gg) {
			mc.thePlayer.sendChatMessage("gg");
		} else if (gl) {
			mc.thePlayer.sendChatMessage("gl");
		}
	}
	
	private boolean eq(String text, String eq) {
		return text.contains(eq);
	}
	
}
