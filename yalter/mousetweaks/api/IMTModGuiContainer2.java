/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 */
package yalter.mousetweaks.api;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public interface IMTModGuiContainer2 {
    public boolean MT_isMouseTweaksDisabled();

    public boolean MT_isWheelTweakDisabled();

    public Container MT_getContainer();

    public Slot MT_getSlotUnderMouse();

    public boolean MT_isCraftingOutput(Slot var1);

    public boolean MT_isIgnored(Slot var1);

    public boolean MT_disableRMBDraggingFunctionality();
}

