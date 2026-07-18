package fr.honertis.module.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.honertis.event.EventAttack;
import fr.honertis.event.EventReceivePacket;
import fr.honertis.event.EventRenderGui;
import fr.honertis.event.EventUpdate;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.TimeUtils;
import fr.honertis.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.world.World;

public class Combo extends ModuleBase{
	public Entity lastEntity;
	public int combos;
	public TimeUtils timer = new TimeUtils();
	
	private int sentAttack = -1;
	private int lastAttackId;

	public Combo() {
		super("Combo", "module.combo", Category.UTILITIES);
		this.addSettings(posX, posY);
	}
	
	@Override
	public void onAttacking(EventAttack e) {
		if (e.isPost()) {
	        Entity currentTarget = e.getEntity();
	        if (currentTarget == null) return;

	        if (currentTarget.getEntityId() != lastAttackId) {
	            combos = 0;
	        }

	        sentAttack = currentTarget.getEntityId();
	        lastEntity = currentTarget;
	        timer.reset();
	    }
	}
	
	@Override
	public void onUpdate(EventUpdate e) {
		resetCombos();
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		String name = "Combo : " + combos;
		double width = mc.fontRendererObj.getStringWidth(name) + 8;
		double height = 15;
		Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(name, posX.getValue() + 4, posY.getValue() + 4, -1);
		
		Utils.calculate(width, height, posX, posY, e.sr);
	}
	
	public void resetCombos() {
		if (timer.check(2500)) {
			combos = 0;
			lastEntity = null;
		}
	}
	
	@Override
	public void onReceivePacket(EventReceivePacket e) {
		Packet packet = e.getPacket();
	    if (!(packet instanceof S19PacketEntityStatus)) return;

	    S19PacketEntityStatus s19 = (S19PacketEntityStatus) packet;
	    if (s19.getOpCode() != 2) return;

	    Entity target = s19.getEntity((World) this.mc.theWorld);
	    if (target == null) return;

	    if (this.sentAttack != -1 && target.getEntityId() == this.sentAttack) {
	        this.sentAttack = -1;

	        if (this.lastAttackId == target.getEntityId()) {
	            this.combos++;
	        } else {
	            this.combos = 1;
	        }

	        this.lastAttackId = target.getEntityId();
	        timer.reset();

	    } else if (target.getEntityId() == this.mc.thePlayer.getEntityId()) {
	        this.combos = 0;
	    }
	}
	
}
