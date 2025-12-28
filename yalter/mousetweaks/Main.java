/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiContainerCreative
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package yalter.mousetweaks;

import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import yalter.mousetweaks.Config;
import yalter.mousetweaks.Constants;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.Logger;
import yalter.mousetweaks.MouseButton;
import yalter.mousetweaks.OnTickMethod;
import yalter.mousetweaks.Reflection;
import yalter.mousetweaks.WheelScrollDirection;
import yalter.mousetweaks.WheelSearchOrder;
import yalter.mousetweaks.api.IMTModGuiContainer2;
import yalter.mousetweaks.api.IMTModGuiContainer2Ex;
import yalter.mousetweaks.handlers.GuiContainerCreativeHandler;
import yalter.mousetweaks.handlers.GuiContainerHandler;
import yalter.mousetweaks.handlers.IMTModGuiContainer2ExHandler;
import yalter.mousetweaks.handlers.IMTModGuiContainer2Handler;

public class Main {
    private static boolean liteLoader = false;
    private static boolean forge = false;
    public static Config config;
    public static OnTickMethod onTickMethod;
    private static Minecraft mc;
    private static GuiScreen oldGuiScreen;
    private static Slot oldSelectedSlot;
    private static Slot firstRightClickedSlot;
    private static boolean oldRMBDown;
    private static boolean disableForThisContainer;
    private static boolean disableWheelForThisContainer;
    private static IGuiScreenHandler handler;
    private static boolean readConfig;
    private static boolean initialized;
    private static boolean disabled;

    public static boolean initialize(Constants.EntryPoint entryPoint) {
        Logger.Log("A call to initialize, entry point: " + entryPoint.toString() + ".");
        if (disabled) {
            return false;
        }
        if (initialized) {
            return true;
        }
        initialized = true;
        mc = Minecraft.getMinecraft();
        config = new Config(Main.mc.mcDataDir + File.separator + "config" + File.separator + "MouseTweaks.cfg");
        config.read();
        Reflection.reflectGuiContainer();
        Reflection.reflectGuiContainerCreative();
        boolean bl = forge = entryPoint == Constants.EntryPoint.FORGE || Reflection.doesClassExist("net.minecraftforge.client.MinecraftForgeClient");
        if (forge) {
            Logger.Log("Minecraft Forge is installed.");
        } else {
            Logger.Log("Minecraft Forge is not installed.");
        }
        boolean bl2 = liteLoader = entryPoint == Constants.EntryPoint.LITELOADER || Reflection.doesClassExist("com.mumfrey.liteloader.core.LiteLoader");
        if (liteLoader) {
            Logger.Log("LiteLoader is installed.");
        } else {
            Logger.Log("LiteLoader is not installed.");
        }
        if (!Main.findOnTickMethod(true)) {
            disabled = true;
            return false;
        }
        Logger.Log("Mouse Tweaks has been initialized.");
        return true;
    }

    public static boolean findOnTickMethod(boolean print_always) {
        OnTickMethod previous_method = onTickMethod;
        for (OnTickMethod method : Main.config.onTickMethodOrder) {
            switch (method) {
                case FORGE: {
                    if (!forge) break;
                    onTickMethod = OnTickMethod.FORGE;
                    if (print_always || onTickMethod != previous_method) {
                        Logger.Log("Using Forge for the mod operation.");
                    }
                    return true;
                }
                case LITELOADER: {
                    if (!liteLoader) break;
                    onTickMethod = OnTickMethod.LITELOADER;
                    if (print_always || onTickMethod != previous_method) {
                        Logger.Log("Using LiteLoader for the mod operation.");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void onUpdateInGame() {
        GuiScreen currentScreen = Main.mc.currentScreen;
        if (currentScreen == null) {
            oldGuiScreen = null;
            oldSelectedSlot = null;
            firstRightClickedSlot = null;
            disableForThisContainer = false;
            disableWheelForThisContainer = false;
            readConfig = true;
            handler = null;
        } else {
            if (readConfig) {
                readConfig = false;
                config.read();
                Main.findOnTickMethod(false);
            }
            Main.onUpdateInGui(currentScreen);
        }
        oldRMBDown = Mouse.isButtonDown((int)1);
    }

    private static void onUpdateInGui(GuiScreen currentScreen) {
        ItemStack stackOnMouse;
        ItemStack targetStack;
        if (oldGuiScreen != currentScreen) {
            oldGuiScreen = currentScreen;
            Logger.DebugLog("You have just opened " + currentScreen.getClass().getSimpleName() + ".");
            handler = Main.findHandler(currentScreen);
            if (handler == null) {
                disableForThisContainer = true;
                Logger.DebugLog("No valid handler found; MT is disabled.");
                return;
            }
            disableForThisContainer = handler.isMouseTweaksDisabled();
            disableWheelForThisContainer = handler.isWheelTweakDisabled();
            Logger.DebugLog("Handler: " + handler.getClass().getSimpleName() + "; MT is " + (disableForThisContainer ? "disabled" : "enabled") + "; wheel tweak is " + (disableWheelForThisContainer ? "disabled" : "enabled") + ".");
        }
        if (!(Main.config.rmbTweak || Main.config.lmbTweakWithItem || Main.config.lmbTweakWithoutItem || Main.config.wheelTweak)) {
            return;
        }
        if (disableForThisContainer) {
            return;
        }
        Slot selectedSlot = handler.getSlotUnderMouse();
        if (Mouse.isButtonDown((int)1)) {
            if (!oldRMBDown) {
                firstRightClickedSlot = selectedSlot;
            }
            if (Main.config.rmbTweak && handler.disableRMBDraggingFunctionality() && firstRightClickedSlot != null && (firstRightClickedSlot != selectedSlot || oldSelectedSlot == selectedSlot) && !handler.isIgnored(firstRightClickedSlot) && !handler.isCraftingOutput(firstRightClickedSlot)) {
                targetStack = firstRightClickedSlot.getStack();
                stackOnMouse = Main.mc.thePlayer.inventory.getItemStack();
                if (stackOnMouse != null && Main.areStacksCompatible(stackOnMouse, targetStack) && firstRightClickedSlot.isItemValid(stackOnMouse)) {
                    handler.clickSlot(firstRightClickedSlot, MouseButton.RIGHT, false);
                }
            }
        } else {
            firstRightClickedSlot = null;
        }
        if (oldSelectedSlot != selectedSlot) {
            boolean shiftIsDown;
            oldSelectedSlot = selectedSlot;
            if (selectedSlot == null) {
                return;
            }
            if (firstRightClickedSlot == selectedSlot) {
                firstRightClickedSlot = null;
            }
            Logger.DebugLog("You have selected a new slot, it's slot number is " + selectedSlot.slotNumber);
            targetStack = Main.copyStack(selectedSlot.getStack());
            stackOnMouse = Main.copyStack(Main.mc.thePlayer.inventory.getItemStack());
            boolean bl = shiftIsDown = Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54);
            if (Mouse.isButtonDown((int)1)) {
                if (Main.config.rmbTweak && !handler.isIgnored(selectedSlot) && !handler.isCraftingOutput(selectedSlot) && stackOnMouse != null && Main.areStacksCompatible(stackOnMouse, targetStack) && selectedSlot.isItemValid(stackOnMouse)) {
                    handler.clickSlot(selectedSlot, MouseButton.RIGHT, false);
                }
            } else if (Mouse.isButtonDown((int)0)) {
                if (stackOnMouse != null) {
                    if (Main.config.lmbTweakWithItem && !handler.isIgnored(selectedSlot) && targetStack != null && Main.areStacksCompatible(stackOnMouse, targetStack)) {
                        if (shiftIsDown) {
                            handler.clickSlot(selectedSlot, MouseButton.LEFT, true);
                        } else if (stackOnMouse.stackSize + targetStack.stackSize <= stackOnMouse.getMaxStackSize()) {
                            handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                            if (!handler.isCraftingOutput(selectedSlot)) {
                                handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                            }
                        }
                    }
                } else if (Main.config.lmbTweakWithoutItem && targetStack != null && shiftIsDown && !handler.isIgnored(selectedSlot)) {
                    handler.clickSlot(selectedSlot, MouseButton.LEFT, true);
                }
            }
        }
        Main.handleWheel(selectedSlot);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void handleWheel(Slot selectedSlot) {
        int numItemsToMove;
        int wheel;
        int n = wheel = Main.config.wheelTweak && !disableWheelForThisContainer ? Mouse.getDWheel() / 120 : 0;
        if (Main.config.wheelScrollDirection == WheelScrollDirection.INVERTED) {
            wheel = -wheel;
        }
        if ((numItemsToMove = Math.abs(wheel)) == 0) return;
        if (selectedSlot == null) return;
        if (handler.isIgnored(selectedSlot)) {
            return;
        }
        boolean pushItems = wheel < 0;
        ItemStack stackOnMouse = Main.copyStack(Main.mc.thePlayer.inventory.getItemStack());
        ItemStack originalStack = Main.copyStack(selectedSlot.getStack());
        boolean isCraftingOutput = handler.isCraftingOutput(selectedSlot);
        if (originalStack == null) return;
        if (stackOnMouse != null && (isCraftingOutput ? !Main.areStacksCompatible(originalStack, stackOnMouse) : Main.areStacksCompatible(originalStack, stackOnMouse))) {
            return;
        }
        List<Slot> slots = handler.getSlots();
        if (isCraftingOutput) {
            if (!pushItems) return;
            Slot applicableSlot = Main.findWheelApplicableSlot(slots, selectedSlot, pushItems);
            int i = 0;
            while (true) {
                if (i >= numItemsToMove) {
                    if (applicableSlot == null) return;
                    if (stackOnMouse != null) return;
                    handler.clickSlot(applicableSlot, MouseButton.LEFT, false);
                    return;
                }
                handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                ++i;
            }
        }
        do {
            int i;
            ItemStack stackFrom;
            ItemStack stackTo;
            Slot slotFrom;
            Slot slotTo;
            Slot applicableSlot;
            if ((applicableSlot = Main.findWheelApplicableSlot(slots, selectedSlot, pushItems)) == null) {
                return;
            }
            if (pushItems) {
                slotTo = applicableSlot;
                slotFrom = selectedSlot;
                stackTo = Main.copyStack(slotTo.getStack());
                stackFrom = Main.copyStack(slotFrom.getStack());
                numItemsToMove = Math.min(numItemsToMove, stackFrom.stackSize);
                if (stackTo != null && stackTo.getMaxStackSize() - stackTo.stackSize <= numItemsToMove) {
                    handler.clickSlot(slotFrom, MouseButton.LEFT, false);
                    handler.clickSlot(slotTo, MouseButton.LEFT, false);
                    handler.clickSlot(slotFrom, MouseButton.LEFT, false);
                    numItemsToMove -= stackTo.getMaxStackSize() - stackTo.stackSize;
                    continue;
                }
                handler.clickSlot(slotFrom, MouseButton.LEFT, false);
                if (stackFrom.stackSize <= numItemsToMove) {
                    handler.clickSlot(slotTo, MouseButton.LEFT, false);
                } else {
                    for (i = 0; i < numItemsToMove; ++i) {
                        handler.clickSlot(slotTo, MouseButton.RIGHT, false);
                    }
                }
                handler.clickSlot(slotFrom, MouseButton.LEFT, false);
                return;
            }
            slotTo = selectedSlot;
            slotFrom = applicableSlot;
            stackTo = Main.copyStack(slotTo.getStack());
            stackFrom = Main.copyStack(slotFrom.getStack());
            if (stackTo.stackSize == stackTo.getMaxStackSize()) {
                return;
            }
            if (stackTo.getMaxStackSize() - stackTo.stackSize <= numItemsToMove) {
                handler.clickSlot(slotFrom, MouseButton.LEFT, false);
                handler.clickSlot(slotTo, MouseButton.LEFT, false);
                if (handler.isCraftingOutput(slotFrom)) continue;
                handler.clickSlot(slotFrom, MouseButton.LEFT, false);
                continue;
            }
            handler.clickSlot(slotFrom, MouseButton.LEFT, false);
            if (handler.isCraftingOutput(slotFrom)) {
                handler.clickSlot(slotTo, MouseButton.LEFT, false);
                --numItemsToMove;
            } else if (stackFrom.stackSize <= numItemsToMove) {
                handler.clickSlot(slotTo, MouseButton.LEFT, false);
                numItemsToMove -= stackFrom.stackSize;
            } else {
                for (i = 0; i < numItemsToMove; ++i) {
                    handler.clickSlot(slotTo, MouseButton.RIGHT, false);
                }
                numItemsToMove = 0;
            }
            if (handler.isCraftingOutput(slotFrom)) continue;
            handler.clickSlot(slotFrom, MouseButton.LEFT, false);
        } while (numItemsToMove > 0);
    }

    private static IGuiScreenHandler findHandler(GuiScreen currentScreen) {
        if (currentScreen instanceof IMTModGuiContainer2Ex) {
            return new IMTModGuiContainer2ExHandler((IMTModGuiContainer2Ex)currentScreen);
        }
        if (currentScreen instanceof IMTModGuiContainer2) {
            return new IMTModGuiContainer2Handler((IMTModGuiContainer2)currentScreen);
        }
        if (currentScreen instanceof GuiContainerCreative) {
            return new GuiContainerCreativeHandler((GuiContainerCreative)currentScreen);
        }
        if (currentScreen instanceof GuiContainer) {
            return new GuiContainerHandler((GuiContainer)currentScreen);
        }
        return null;
    }

    private static boolean areStacksCompatible(ItemStack a, ItemStack b) {
        return a == null || b == null || a.isItemEqual(b);
    }

    private static ItemStack copyStack(ItemStack s) {
        return s == null ? null : s.copy();
    }

    private static Slot findWheelApplicableSlot(List<Slot> slots, Slot selectedSlot, boolean pushItems) {
        int direction;
        int endIndex;
        int startIndex;
        if (pushItems || Main.config.wheelSearchOrder == WheelSearchOrder.FIRST_TO_LAST) {
            startIndex = 0;
            endIndex = slots.size();
            direction = 1;
        } else {
            startIndex = slots.size() - 1;
            endIndex = -1;
            direction = -1;
        }
        ItemStack originalStack = selectedSlot.getStack();
        boolean findInPlayerInventory = selectedSlot.inventory != Main.mc.thePlayer.inventory;
        Slot rv = null;
        for (int i = startIndex; i != endIndex; i += direction) {
            Slot slot = slots.get(i);
            if (handler.isIgnored(slot) || (!findInPlayerInventory ? slot.inventory == Main.mc.thePlayer.inventory : slot.inventory != Main.mc.thePlayer.inventory)) continue;
            ItemStack stack = slot.getStack();
            if (stack == null) {
                if (rv != null || !pushItems || !slot.isItemValid(originalStack) || handler.isCraftingOutput(slot)) continue;
                rv = slot;
                continue;
            }
            if (!Main.areStacksCompatible(originalStack, stack)) continue;
            if (pushItems) {
                if (handler.isCraftingOutput(slot) || stack.stackSize >= stack.getMaxStackSize()) continue;
                return slot;
            }
            return slot;
        }
        return rv;
    }

    static {
        oldGuiScreen = null;
        oldSelectedSlot = null;
        firstRightClickedSlot = null;
        oldRMBDown = false;
        disableForThisContainer = false;
        disableWheelForThisContainer = false;
        handler = null;
        readConfig = false;
        initialized = false;
        disabled = false;
    }
}

