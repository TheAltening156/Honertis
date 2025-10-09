/*
 * Decompiled with CFR 0.150.
 */
package yalter.mousetweaks;

public enum WheelScrollDirection {
    NORMAL(0),
    INVERTED(1);

    private final int id;

    private WheelScrollDirection(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.id;
    }

    public static WheelScrollDirection fromId(int id) {
        return id == WheelScrollDirection.NORMAL.id ? NORMAL : INVERTED;
    }
}

