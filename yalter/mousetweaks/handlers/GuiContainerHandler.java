/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ContainerRepair
 *  net.minecraft.inventory.Slot
 *  net.minecraft.inventory.SlotCrafting
 *  net.minecraft.inventory.SlotFurnaceOutput
 *  net.minecraft.inventory.SlotMerchantResult
 *  net.minecraft.util.ReportedException
 *  org.lwjgl.input.Mouse
 */
package yalter.mousetweaks.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.util.ReportedException;
import org.lwjgl.input.Mouse;
import yalter.mousetweaks.Constants;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.MouseButton;
import yalter.mousetweaks.Reflection;

public class GuiContainerHandler
implements IGuiScreenHandler {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected GuiContainer guiContainer;

    public GuiContainerHandler(GuiContainer guiContainer) {
        this.guiContainer = guiContainer;
    }

    private int getDisplayWidth() {
        return this.mc.displayWidth;
    }

    private int getDisplayHeight() {
        return this.mc.displayHeight;
    }

    private int getRequiredMouseX() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        return Mouse.getX() * scaledResolution.getScaledWidth() / this.getDisplayWidth();
    }

    private int getRequiredMouseY() {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int scaledHeight = scaledResolution.getScaledHeight();
        return scaledHeight - Mouse.getY() * scaledHeight / this.getDisplayHeight() - 1;
    }

    @Override
    public boolean isMouseTweaksDisabled() {
        return Reflection.guiContainerClass == null;
    }

    @Override
    public boolean isWheelTweakDisabled() {
        return false;
    }

    @Override
    public List<Slot> getSlots() {
        return this.guiContainer.inventorySlots.inventorySlots;
    }

    @Override
    public Slot getSlotUnderMouse() {
        try {
            return (Slot)Reflection.guiContainerClass.invokeMethod((Object)this.guiContainer, Constants.GETSLOTATPOSITION_NAME.forgeName, this.getRequiredMouseX(), this.getRequiredMouseY());
        }
        catch (InvocationTargetException e) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)e, (String)"GuiContainer.getSlotAtPosition() threw an exception when called from MouseTweaks");
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public boolean disableRMBDraggingFunctionality() {
        Reflection.guiContainerClass.setFieldValue((Object)this.guiContainer, Constants.IGNOREMOUSEUP_NAME.forgeName, true);
        if (((Boolean)Reflection.guiContainerClass.getFieldValue((Object)this.guiContainer, Constants.DRAGSPLITTING_NAME.forgeName)).booleanValue() && (Integer)Reflection.guiContainerClass.getFieldValue((Object)this.guiContainer, Constants.DRAGSPLITTINGBUTTON_NAME.forgeName) == 1) {
            Reflection.guiContainerClass.setFieldValue((Object)this.guiContainer, Constants.DRAGSPLITTING_NAME.forgeName, false);
            return true;
        }
        return false;
    }

    @Override
    public void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed) {
        this.mc.playerController.windowClick(this.guiContainer.inventorySlots.windowId, slot.slotNumber, mouseButton.getValue(), shiftPressed ? 1 : 0, (EntityPlayer)this.mc.thePlayer);
    }

    @Override
    public boolean isCraftingOutput(Slot slot) {
        return slot instanceof SlotCrafting || slot instanceof SlotFurnaceOutput || slot instanceof SlotMerchantResult || this.guiContainer.inventorySlots instanceof ContainerRepair && slot.slotNumber == 2;
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return false;
    }
}

