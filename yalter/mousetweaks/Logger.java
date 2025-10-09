/*
 * Decompiled with CFR 0.150.
 */
package yalter.mousetweaks;

import yalter.mousetweaks.Config;

public class Logger {
    public static void Log(String text) {
        System.out.println("[Mouse Tweaks] " + text);
    }

    public static void DebugLog(String text) {
        if (Config.debug) {
            System.out.println("[Mouse Tweaks] " + text);
        }
    }
}

