package fr.honertis.module.modules;

import fr.honertis.event.EventAttack;
import fr.honertis.event.EventReceivePacket;
import fr.honertis.event.EventRenderGui;
import fr.honertis.module.Category;
import fr.honertis.module.ModuleBase;
import fr.honertis.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Reach extends ModuleBase{
	public float lastReach = 0;
	private float pendingReach;
	private int sentAttack = -1;
	
	public Reach() {
		super("ReachIndicator", "module.reachIndicator", Category.UTILITIES);
		this.addSettings(posX, posY);
	}
	
	@Override
	public void onGuiRender(EventRenderGui e) {
		String name = "Reach : " + (lastReach > 4 ? EnumChatFormatting.DARK_RED + "": lastReach > 3 ? EnumChatFormatting.RED +  "" : "") + String.format("%.2f", lastReach);
		double width = mc.fontRendererObj.getStringWidth(name) + 8;
		double height = 15;
		Gui.drawRect(posX.getValue(), posY.getValue(), posX.getValue() + width, posY.getValue() + height, 0x90000000);
		mc.fontRendererObj.drawStringWithShadow(name, posX.getValue() + 4, posY.getValue() + 4, -1);
		
		Utils.calculate(width, height, posX, posY, e.sr);
	}
	
	@Override
	public void onAttacking(EventAttack e) {
		if (e.isPost()) {
	        MovingObjectPosition mop = mc.objectMouseOver;
	        if (mop != null && mop.entityHit != null) {
	            Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
	            double reach = eyes.distanceTo(mop.hitVec);

	            if (reach <= 6) {
	                pendingReach = (float) reach;
	                sentAttack = mop.entityHit.getEntityId();
	            }
	        }
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
	        this.lastReach = pendingReach; // seulement ici, sur confirmation serveur
	    }
	}
}
