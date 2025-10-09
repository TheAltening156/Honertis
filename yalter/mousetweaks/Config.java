/*
 * Decompiled with CFR 0.150.
 */
package yalter.mousetweaks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import yalter.mousetweaks.Logger;
import yalter.mousetweaks.OnTickMethod;
import yalter.mousetweaks.WheelScrollDirection;
import yalter.mousetweaks.WheelSearchOrder;

public class Config {
    protected static final Properties defaultValues = new Properties();
    protected String fileName;
    public boolean rmbTweak = true;
    public boolean lmbTweakWithItem = true;
    public boolean lmbTweakWithoutItem = true;
    public boolean wheelTweak = true;
    public WheelSearchOrder wheelSearchOrder = WheelSearchOrder.LAST_TO_FIRST;
    public WheelScrollDirection wheelScrollDirection = WheelScrollDirection.NORMAL;
    public Set<OnTickMethod> onTickMethodOrder = new LinkedHashSet<OnTickMethod>();
    public static boolean debug = false;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public void read() {
        Properties properties = new Properties(defaultValues);
        try {
            FileReader configReader = new FileReader(this.fileName);
            properties.load(configReader);
            configReader.close();
        }
        catch (FileNotFoundException configReader) {
        }
        catch (IOException e) {
            Logger.Log("Failed to read the config file: " + this.fileName);
            e.printStackTrace();
        }
        this.rmbTweak = Integer.parseInt(properties.getProperty("RMBTweak")) != 0;
        this.lmbTweakWithItem = Integer.parseInt(properties.getProperty("LMBTweakWithItem")) != 0;
        this.lmbTweakWithoutItem = Integer.parseInt(properties.getProperty("LMBTweakWithoutItem")) != 0;
        this.wheelTweak = Integer.parseInt(properties.getProperty("WheelTweak")) != 0;
        this.wheelSearchOrder = WheelSearchOrder.fromId(Integer.parseInt(properties.getProperty("WheelSearchOrder")));
        this.wheelScrollDirection = WheelScrollDirection.fromId(Integer.parseInt(properties.getProperty("WheelScrollDirection")));
        debug = Integer.parseInt(properties.getProperty("Debug")) != 0;
        this.onTickMethodOrderFromString(properties.getProperty("OnTickMethodOrder"));
        this.save();
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty("RMBTweak", this.rmbTweak ? "1" : "0");
        properties.setProperty("LMBTweakWithItem", this.lmbTweakWithItem ? "1" : "0");
        properties.setProperty("LMBTweakWithoutItem", this.lmbTweakWithoutItem ? "1" : "0");
        properties.setProperty("WheelTweak", this.wheelTweak ? "1" : "0");
        properties.setProperty("WheelSearchOrder", String.valueOf(this.wheelSearchOrder.ordinal()));
        properties.setProperty("WheelScrollDirection", String.valueOf(this.wheelScrollDirection.ordinal()));
        properties.setProperty("Debug", debug ? "1" : "0");
        properties.setProperty("OnTickMethodOrder", this.onTickMethodOrderString());
        try {
            File config = new File(this.fileName);
            boolean existed = config.exists();
            File parentDir = config.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            FileWriter configWriter = new FileWriter(config);
            properties.store(configWriter, null);
            configWriter.close();
            if (!existed) {
                Logger.Log("Created the config file.");
            }
        }
        catch (IOException e) {
            Logger.Log("Failed to write the config file: " + this.fileName);
            e.printStackTrace();
        }
    }

    public String onTickMethodOrderString() {
        String result = "";
        for (OnTickMethod method : this.onTickMethodOrder) {
            if (!result.isEmpty()) {
                result = result + ", ";
            }
            switch (method) {
                case FORGE: {
                    result = result + "Forge";
                    break;
                }
                case LITELOADER: {
                    result = result + "LiteLoader";
                }
            }
        }
        return result;
    }

    public void onTickMethodOrderFromString(String string) {
        String[] onTickMethods;
        this.onTickMethodOrder.clear();
        for (String method : onTickMethods = string.trim().split("[\\s]*,[\\s]*")) {
            if ("Forge".equalsIgnoreCase(method)) {
                this.onTickMethodOrder.add(OnTickMethod.FORGE);
                continue;
            }
            if (!"LiteLoader".equalsIgnoreCase(method)) continue;
            this.onTickMethodOrder.add(OnTickMethod.LITELOADER);
        }
        this.onTickMethodOrder.add(OnTickMethod.FORGE);
        this.onTickMethodOrder.add(OnTickMethod.LITELOADER);
    }

    static {
        defaultValues.setProperty("RMBTweak", "1");
        defaultValues.setProperty("LMBTweakWithItem", "1");
        defaultValues.setProperty("LMBTweakWithoutItem", "1");
        defaultValues.setProperty("WheelTweak", "1");
        defaultValues.setProperty("WheelSearchOrder", "1");
        defaultValues.setProperty("WheelScrollDirection", "0");
        defaultValues.setProperty("OnTickMethodOrder", "Forge, LiteLoader");
        defaultValues.setProperty("Debug", "0");
    }
}

