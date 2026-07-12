package fr.honertis.module.modules;

import java.util.stream.Collectors;

import fr.honertis.event.EventAttack;
import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;

public class Reach extends ModuleBase{
	public float lastReach = 0;
	
	public Reach() {
		super("ReachIndicator", "module.reachIndicator", Category.UTILITIES);
		this.addSettings(posX, posY);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		String name = "Reach : " + (lastReach > 3 ? EnumChatFormatting.RED +  "" : "") + String.format("%.2f", lastReach);
		double width = mc.fontRendererObj.getStringWidth(name) + 8;
		double height = 15;
		Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(name, posX.getValue() + 4, posY.getValue() + 4, -1);
		
		Utils.calculate(width, height, posX, posY, e.sr);
	}
	
	@Override
	public void onAttacking(EventAttack e) {
		float dist = mc.thePlayer.getDistanceToEntity(e.entity);
		lastReach = dist <= 5 ? mc.thePlayer.getDistanceToEntity(e.entity) : lastReach;
	}
}
