package fr.honertis.module.modules;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.TimeUtils;
import net.minecraft.util.ChatComponentText;

public class AutoGG extends ModuleBase{
	public TimeUtils timer = new TimeUtils();
	public AutoGG() {
		super("AutoGG", "Dis GG ou GL dans le chat a chaque perdue perdu ou gagnée", Category.UTILITIES);
	}
	
	@Override
	public void onEnable() {
}
	
	@Override
	public void onUpdate(EventUpdate e) {
		boolean gg = eq(mc.ingameGUI.title, "Victoire") || eq(mc.ingameGUI.title, "Défaite");
		boolean gl = eq(mc.ingameGUI.title, "Bonne chance");
		if (timer.hasTimeElapsed(3200, gg || gl))
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
