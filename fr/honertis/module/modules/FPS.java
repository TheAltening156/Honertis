package fr.honertis.module.modules;

import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import net.minecraft.client.gui.Gui;

public class FPS extends ModuleBase{
	public int posX = 50,
			   posY = 50,
			   oldX,
			   oldY;
	
	public FPS() {
		super("FPS", "Montrer vos FPS actuels", Category.UTILITIES);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		posX = posY = 10;
		Gui.drawRect(posX - 4, posY - 4, posX + mc.fontRendererObj.getStringWidth("FPS : " + mc.getDebugFPS()) + 4, posY + 11, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow("FPS : " + mc.getDebugFPS(), posX, posY, -1);
	}
	
}
