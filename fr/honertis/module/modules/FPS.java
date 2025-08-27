package fr.honertis.module.modules;

import java.util.List;

import com.google.common.collect.Lists;

import fr.honertis.event.EventRenderGui;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import net.minecraft.client.gui.Gui;

public class FPS extends ModuleBase{
	public int posX = 50,
			   posY = 50,
			   oldX,
			   oldY;
	public List<Long> list = Lists.newArrayList();
	
	public BooleanSettings realTime = new BooleanSettings("module.fps.realtime", false);
	public FPS() {
		super("FPS", "module.fps", Category.UTILITIES);
		this.addSettings(realTime);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		posX = posY = 10;
		String name = "FPS : " + (realTime.isToggled() ? list.size() : mc.getDebugFPS());
		Gui.drawRect(posX - 4, posY - 4, posX + mc.fontRendererObj.getStringWidth(name) + 4, posY + 11, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(name, posX, posY, -1);
	}
	
	@Override
	public void update() {
		list.add(System.currentTimeMillis());
		list.removeIf(aLong -> aLong + 1000 < System.currentTimeMillis());
	}
	
}
