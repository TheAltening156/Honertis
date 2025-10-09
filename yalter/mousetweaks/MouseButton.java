/*
 * Decompiled with CFR 0.150.
 */
package yalter.mousetweaks;

public enum MouseButton {
    LEFT(0),
    RIGHT(1);

    private final int id;

    private MouseButton(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.id;
    }
}

