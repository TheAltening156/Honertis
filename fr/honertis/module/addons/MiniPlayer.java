package fr.honertis.module.addons;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;
import net.minecraft.client.gui.ScaledResolution;

public class MiniPlayer extends ModuleBase{
	public boolean isClicked;
	public double oldX;
	public double oldY;
	
	public NumberSettings posX = new NumberSettings("posX", 0, 0, 1920, 1);
	public NumberSettings posY = new NumberSettings("posY", 0, 0, 1080, 1);
	
	public MiniPlayer() {
		super("MiniPlayer", "MiniPlayer position", Category.UTILITIES, false);
		this.addSettings(posX,posY);
	}
	
	@Override
	public void onUpdate(EventUpdate e) {
		if (!e.isPre()) return;
		ScaledResolution scale = new ScaledResolution(mc);
		posX.setMax(scale.getScaledWidth() - 225);
		posY.setMax(scale.getScaledHeight() - 40);
		if (posX.getValue() < posX.getMin()) {
			posX.setValue(posX.getMin());
		}
		if (posY.getValue() < posY.getMin()) {
			posY.setValue(posY.getMin());
		}
		if (posX.getValue() > posX.getMax()) {
			posX.setValue(posX.getMax());
		}
		if (posY.getValue() > posY.getMax()) {
			posY.setValue(posY.getMax());
		}
	}
	
	
}
