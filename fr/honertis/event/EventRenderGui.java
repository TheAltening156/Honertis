package fr.honertis.event;

import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGui extends Event<EventRenderGui>{
	public ScaledResolution sr;
	
	public EventRenderGui(ScaledResolution sr) {
		this.sr = sr;
	}
	
}
