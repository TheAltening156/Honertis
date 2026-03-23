package yalter.mousetweaks.handlers;

import java.lang.reflect.InvocationTargetException;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.crash.CrashReport;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ReportedException;
import yalter.mousetweaks.Constants;
import yalter.mousetweaks.MouseButton;
import yalter.mousetweaks.Reflection;

public class GuiContainerCreativeHandler
extends GuiContainerHandler {
    protected GuiContainerCreative guiContainerCreative;

    public GuiContainerCreativeHandler(GuiContainerCreative guiContainerCreative) {
        super(guiContainerCreative);
        this.guiContainerCreative = guiContainerCreative;
    }

    @Override
    public boolean isMouseTweaksDisabled() {
        return super.isMouseTweaksDisabled() || Reflection.guiContainerCreative == null;
    }

    @Override
    public void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed) {
        try {
            Reflection.guiContainerCreative.invokeMethod((Object)this.guiContainerCreative, Constants.HANDLEMOUSECLICK_NAME.forgeName, new Object[]{slot, slot.slotNumber, mouseButton.getValue(), shiftPressed ? 1 : 0});
        }
        catch (InvocationTargetException e) {
            CrashReport crashreport = CrashReport.makeCrashReport(e, "GuiContainerCreative.handleMouseClick() threw an exception when called from MouseTweaks");
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return super.isIgnored(slot) || slot.inventory != this.mc.thePlayer.inventory;
    }
}

