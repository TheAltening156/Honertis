/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Slot
 */
package yalter.mousetweaks.api;

import java.util.List;
import net.minecraft.inventory.Slot;

public interface IMTModGuiContainer2Ex {
    public boolean MT_isMouseTweaksDisabled();

    public boolean MT_isWheelTweakDisabled();

    public List<Slot> MT_getSlots();

    public Slot MT_getSlotUnderMouse();

    public boolean MT_isCraftingOutput(Slot var1);

    public boolean MT_isIgnored(Slot var1);

    public boolean MT_disableRMBDraggingFunctionality();

    public void MT_clickSlot(Slot var1, int var2, boolean var3);
}

