/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiContainerCreative
 *  net.minecraft.inventory.Slot
 */
package yalter.mousetweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.inventory.Slot;
import yalter.mousetweaks.Constants;
import yalter.mousetweaks.Logger;
import yalter.mousetweaks.ObfuscatedName;
import yalter.mousetweaks.Obfuscation;
import yalter.mousetweaks.ReflectionCache;

public class Reflection {
    private static Obfuscation obfuscation;
    private static boolean checkObfuscation;
    public static ReflectionCache guiContainerClass;
    public static ReflectionCache guiContainerCreative;

    static void reflectGuiContainer() {
        Field f;
        Logger.Log("Reflecting GuiContainer...");
        guiContainerClass = new ReflectionCache();
        try {
            f = Reflection.getField(GuiContainer.class, Reflection.getObfuscatedName(Constants.IGNOREMOUSEUP_NAME));
            guiContainerClass.storeField(Constants.IGNOREMOUSEUP_NAME.forgeName, f);
        }
        catch (NoSuchFieldException e) {
            Logger.Log("Could not retrieve GuiContainer.ignoreMouseUp.");
            guiContainerClass = null;
            return;
        }
        try {
            f = Reflection.getField(GuiContainer.class, Reflection.getObfuscatedName(Constants.DRAGSPLITTING_NAME));
            guiContainerClass.storeField(Constants.DRAGSPLITTING_NAME.forgeName, f);
        }
        catch (NoSuchFieldException e) {
            Logger.Log("Could not retrieve GuiContainer.dragSplitting.");
            guiContainerClass = null;
            return;
        }
        try {
            f = Reflection.getField(GuiContainer.class, Reflection.getObfuscatedName(Constants.DRAGSPLITTINGBUTTON_NAME));
            guiContainerClass.storeField(Constants.DRAGSPLITTINGBUTTON_NAME.forgeName, f);
        }
        catch (NoSuchFieldException e) {
            Logger.Log("Could not retrieve GuiContainer.dragSplittingButton.");
            guiContainerClass = null;
            return;
        }
        try {
            Method m = Reflection.getMethod(GuiContainer.class, Reflection.getObfuscatedName(Constants.GETSLOTATPOSITION_NAME), Integer.TYPE, Integer.TYPE);
            guiContainerClass.storeMethod(Constants.GETSLOTATPOSITION_NAME.forgeName, m);
        }
        catch (NoSuchMethodException e) {
            Logger.Log("Could not retrieve GuiContainer.getSlotAtPosition().");
            guiContainerClass = null;
            return;
        }
        Logger.Log("Success.");
    }

    static void reflectGuiContainerCreative() {
        Logger.Log("Reflecting GuiContainerCreative...");
        guiContainerCreative = new ReflectionCache();
        try {
            Method m = Reflection.getMethod(GuiContainerCreative.class, Reflection.getObfuscatedName(Constants.HANDLEMOUSECLICK_NAME), Slot.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            guiContainerCreative.storeMethod(Constants.HANDLEMOUSECLICK_NAME.forgeName, m);
        }
        catch (NoSuchMethodException e) {
            Logger.Log("Could not retrieve GuiContainerCreative.handleMouseClick().");
            guiContainerCreative = null;
            return;
        }
        Logger.Log("Success.");
    }

    static boolean doesClassExist(String name) {
        try {
            Class.forName(name);
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Field getField(Class clazz, String name) throws NoSuchFieldException {
        Field field;
        try {
            field = clazz.getField(name);
        }
        catch (NoSuchFieldException e) {
            field = clazz.getDeclaredField(name);
        }
        field.setAccessible(true);
        return field;
    }

    private static Method getMethod(Class<?> clazz, String name, Class ... args) throws NoSuchMethodException {
        Method method;
        try {
            method = clazz.getMethod(name, args);
        }
        catch (NoSuchMethodException e) {
            method = clazz.getDeclaredMethod(name, args);
        }
        method.setAccessible(true);
        return method;
    }

    private static String getObfuscatedName(ObfuscatedName obfuscatedName) {
        if (checkObfuscation) {
            Reflection.checkObfuscation();
        }
        return obfuscatedName.get(obfuscation);
    }

    private static void checkObfuscation() {
        checkObfuscation = false;
        try {
            Reflection.getField(GuiContainer.class, Constants.IGNOREMOUSEUP_NAME.mcpName);
            obfuscation = Obfuscation.MCP;
        }
        catch (NoSuchFieldException e) {
            try {
                Reflection.getField(GuiContainer.class, Constants.IGNOREMOUSEUP_NAME.forgeName);
                obfuscation = Obfuscation.FORGE;
            }
            catch (NoSuchFieldException ex) {
                obfuscation = Obfuscation.VANILLA;
            }
        }
        Logger.Log("Detected obfuscation: " + (Object)((Object)obfuscation) + ".");
    }

    static {
        checkObfuscation = true;
    }
}

