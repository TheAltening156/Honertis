/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.inventory.Slot
 */
package yalter.mousetweaks.handlers;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.MouseButton;
import yalter.mousetweaks.api.IMTModGuiContainer2Ex;

public class IMTModGuiContainer2ExHandler
implements IGuiScreenHandler {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected IMTModGuiContainer2Ex modGuiContainer;

    public IMTModGuiContainer2ExHandler(IMTModGuiContainer2Ex modGuiContainer) {
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
        return this.modGuiContainer.MT_getSlots();
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
        this.modGuiContainer.MT_clickSlot(slot, mouseButton.getValue(), shiftPressed);
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

