package fr.honertis.module.addons;

import org.lwjgl.input.Mouse;

import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.NumberSettings;
import fr.honertis.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class MiniPlayer extends ModuleBase{
	
	public MiniPlayer() {
		super("MiniPlayer", "MiniPlayer position", Category.UTILITIES, false);
		this.addSettings(posX,posY);
		posX.setSize(225);
		posY.setSize(40);
	}
	
}
