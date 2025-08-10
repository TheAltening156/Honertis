package fr.honertis.module.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import fr.honertis.event.EventRender2D;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.settings.BooleanSettings;
import fr.honertis.settings.KeyBindSettings;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class FreeLook extends ModuleBase {
	public KeyBindSettings key = new KeyBindSettings("Touche", 0);
	public BooleanSettings hold = new BooleanSettings("Maintient de la touche", true);

	public FreeLook() {
		super("FreeLook", "Permet de regarder autour de soi en 3eme personne", Category.UTILITIES);
		this.addSettings(hold, key);
		this.enabled = false;
	}

	public float rotYaw = 0.0F;
	public float rotPitch = 0.0F;
	
	public float lastYaw = 0.0F;
	public float lastPitch = 0.0F;


	@Override
	public void onEnable() {
	    this.rotYaw = lastYaw = mc.thePlayer.rotationYaw;
	    this.rotPitch = lastPitch = mc.thePlayer.rotationPitch;
	    mc.gameSettings.thirdPersonView = 1;

	}

	@Override
	public void onDisable() {
		mc.thePlayer.rotationYaw = this.lastYaw;
		mc.thePlayer.rotationPitch = this.lastPitch;
	    mc.gameSettings.thirdPersonView = 0;

	}

	@Override
	public void onRender2D(EventRender2D e) {
		if (enabled) {
		    mc.gameSettings.thirdPersonView = 1;
		}
	}

}
