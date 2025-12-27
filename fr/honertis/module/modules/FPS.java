package fr.honertis.module.modules;

import java.util.List;

import com.google.common.collect.Lists;

import fr.honertis.event.EventRenderGui;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class FPS extends ModuleBase{
	public List<Long> list = Lists.newArrayList();
	
	public BooleanSettings realTime = new BooleanSettings("module.fps.realtime", false);
	public FPS() {
		super("FPS", "module.fps", Category.UTILITIES);
		this.addSettings(realTime, posX, posY);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		String name = "FPS : " + (realTime.isEnabled() ? list.size() : mc.getDebugFPS());
		double width = mc.fontRendererObj.getStringWidth(name) + 8;
		double height = 15;
		Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(name, posX.getValue() + 4, posY.getValue() + 4, -1);
		
		Utils.calculate(width, height, posX, posY, new ScaledResolution(mc));
	}
	
	@Override
	public void update() {
		list.add(System.currentTimeMillis());
		list.removeIf(aLong -> aLong + 1000 < System.currentTimeMillis());
	}

	
}
