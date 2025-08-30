package net.minecraft.util;

import fr.honertis.Honertis;
import fr.honertis.module.modules.ToggleSneak;
import fr.honertis.utils.MC;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.isKeyDown())
        {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown())
        {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown())
        {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.isKeyDown())
        {
            --this.moveStrafe;
        }

        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        ToggleSneak toggleSneak = (ToggleSneak) Honertis.INSTANCE.modulesManager.getModuleByName("ToggleSneak");
        boolean hold = toggleSneak.isEnabled() ? toggleSneak.holdActive : false;
        if (hold) {
        	hold = MC.mc.currentScreen != null? false : hold;
        }
        this.sneak = hold ? hold : this.gameSettings.keyBindSneak.isKeyDown();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
