/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Slot
 */
package yalter.mousetweaks;

import java.util.List;
import net.minecraft.inventory.Slot;
import yalter.mousetweaks.MouseButton;

public interface IGuiScreenHandler {
    public boolean isMouseTweaksDisabled();

    public boolean isWheelTweakDisabled();

    public List<Slot> getSlots();

    public Slot getSlotUnderMouse();

    public boolean disableRMBDraggingFunctionality();

    public void clickSlot(Slot var1, MouseButton var2, boolean var3);

    public boolean isCraftingOutput(Slot var1);

    public boolean isIgnored(Slot var1);
}

