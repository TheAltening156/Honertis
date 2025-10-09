/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Slot
 */
package yalter.mousetweaks.handlers;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.MouseButton;
import yalter.mousetweaks.api.IMTModGuiContainer2;

public class IMTModGuiContainer2Handler
implements IGuiScreenHandler {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected IMTModGuiContainer2 modGuiContainer;

    public IMTModGuiContainer2Handler(IMTModGuiContainer2 modGuiContainer) {
        this.modGuiContainer = modGuiContainer;
    }

    @Override
    public boolean isMouseTweaksDisabled() {
        return this.modGuiContainer.MT_isMouseTweaksDisabled();
    }

    @Override
    public boolean isWheelTweakDisabled() {
        return this.modGuiContainer.MT_isWheelTweakDisabled();
    }

    @Override
    public List<Slot> getSlots() {
        return this.modGuiContainer.MT_getContainer().inventorySlots;
    }

    @Override
    public Slot getSlotUnderMouse() {
        return this.modGuiContainer.MT_getSlotUnderMouse();
    }

    @Override
    public boolean disableRMBDraggingFunctionality() {
        return this.modGuiContainer.MT_disableRMBDraggingFunctionality();
    }

    @Override
    public void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed) {
        this.mc.playerController.windowClick(this.modGuiContainer.MT_getContainer().windowId, slot.slotNumber, mouseButton.getValue(), shiftPressed ? 1 : 0, (EntityPlayer)this.mc.thePlayer);
    }

    @Override
    public boolean isCraftingOutput(Slot slot) {
        return this.modGuiContainer.MT_isCraftingOutput(slot);
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return this.modGuiContainer.MT_isIgnored(slot);
    }
}

