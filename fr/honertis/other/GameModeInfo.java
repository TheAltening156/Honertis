package fr.honertis.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import fr.honertis.Honertis;
import fr.honertis.utils.ChatUtils;
import fr.honertis.utils.LangManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class GameModeInfo {
	public ItemStack is;
	public String gameModeName;
	public String command;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean isVisible;
	public static int selectedIndex = 0;
	public static String lastGameMode;
	
	public static List<GameModeInfo> gameModes;
	
	public GameModeInfo(String gameModeName, String command, ItemStack is) {
		this.gameModeName = gameModeName;
		this.command = command;
		this.is = is;
	}
	
	public static void init() {
		List<GameModeInfo> list = new ArrayList<GameModeInfo>();
		list.addAll(Arrays.asList(
				new GameModeInfo("gameMode.creative", "creative", new ItemStack(Blocks.grass)),
				new GameModeInfo("gameMode.survival", "survival", new ItemStack(Items.iron_sword)),
				new GameModeInfo("gameMode.adventure", "adventure", new ItemStack(Items.map)),
				new GameModeInfo("gameMode.spectator", "spectator", new ItemStack(Items.ender_eye))));
		gameModes = list;
	}
	
	public static class GuiIngameSwitcher extends GuiScreen{
		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			if (keyCode == 62) {
				nextItem();
				return;
			} 
			if (keyCode == 1) {
				isVisible = false;
				mc.displayGuiScreen(null);
				mc.setIngameFocus();
			}   
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			ScaledResolution res = new ScaledResolution(mc);
			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			int renderSlotSize = 26;
			int count = gameModes.size();
			int windowWidth = 125;
			int windowHeight = 75;
			int centerX = width / 2;
			int centerY = height / 2;
			int windowX = centerX - windowWidth / 2;
			int windowY = centerY - windowHeight / 2 - 30;
			int slotsStartX = centerX - windowWidth / 2 + 3;
			int slotsY = windowY + 27;
			for (int i = 0; i < count; i++) {
				int x = slotsStartX + i * renderSlotSize + i * 5;
				if (mouseX >= x && mouseX < x + renderSlotSize && mouseY >= slotsY && mouseY < slotsY + renderSlotSize)
					selectedIndex = i;
			}
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			this.mc.getTextureManager().bindTexture(new ResourceLocation("honertis/gamemode_switcher.png"));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawScaledCustomSizeModalRect(windowX, windowY, 0.0F, 0.0F, 125, 75, windowWidth, windowHeight, 256.0F, 256.0F);
			String modeName = I18n.format((gameModes.get(selectedIndex)).gameModeName, new Object[0]);
			drawCenteredString(this.mc.fontRendererObj, modeName, centerX, windowY + 7, 16777215);
			for (int j = 0; j < count; j++) {
				int x = slotsStartX + j * renderSlotSize + j * 5;
				boolean isSelected = (j == selectedIndex);
				this.mc.getTextureManager()
						.bindTexture(new ResourceLocation("honertis/gamemode_switcher.png"));
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				drawScaledCustomSizeModalRect(x, slotsY, 0.0F, 75.0F, 26, 26, renderSlotSize, renderSlotSize, 256.0F,
						256.0F);
				if (isSelected)
					drawScaledCustomSizeModalRect(x, slotsY, 26.0F, 75.0F, 26, 26, renderSlotSize, renderSlotSize, 256.0F,
							256.0F);
				ItemStack icon = (gameModes.get(j)).is;
				RenderHelper.enableGUIStandardItemLighting();
				int iconOffset = (renderSlotSize - 16) / 2;
				this.mc.getRenderItem().renderItemAndEffectIntoGUI(icon, x + iconOffset, slotsY + iconOffset);
				RenderHelper.disableStandardItemLighting();
			}
			drawCenteredString(this.mc.fontRendererObj,
					"§b[ F4 ] §r - " + LangManager.format("debug.gamemodes.select_next"),
					centerX, windowY + windowHeight - 12, 11184810);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
		
		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			ScaledResolution res = new ScaledResolution(mc);
			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			int renderSlotSize = 26;
			int count = gameModes.size();
			int windowWidth = 125;
			int windowHeight = 75;
			int centerX = width / 2;
			int centerY = height / 2;
			int windowY = centerY - windowHeight / 2 - 30;
			int slotsStartX = centerX - windowWidth / 2 + 3;
			int slotsY = windowY + 27;
			for (int i = 0; i < count; i++) {
				int x = slotsStartX + i * renderSlotSize + i * 5;
				if (mouseX >= x && mouseX < x + renderSlotSize && mouseY >= slotsY && mouseY < slotsY + renderSlotSize) {
					selectedIndex = i;
					applyGameMode();
					isVisible = false;
					mc.setIngameFocus();
				}
			}
		}
	}
	
	public static void update() {
		if (isVisible && !Keyboard.isKeyDown(61)) {
			applyGameMode();
			isVisible = false;
			mc.setIngameFocus();
		}
	}
	
	public static void keyListener(int key) {
		if (key == 62 && Keyboard.getEventKeyState() && Keyboard.isKeyDown(61)) {
			if (!isVisible) {
				openMenu();
				if (!(mc.currentScreen instanceof GuiIngameSwitcher))
					mc.displayGuiScreen(new GuiIngameSwitcher());
				mc.setIngameNotInFocus();

			} else {
				nextItem();
			}
		}
	}
	
	private static void applyGameMode() {
		if (selectedIndex >= 0 && selectedIndex < gameModes.size()) {
			GameModeInfo target = gameModes.get(selectedIndex);
			if (mc.playerController != null)
				lastGameMode = mc.playerController.getCurrentGameType().getName();
			if (lastGameMode == null || !lastGameMode.equalsIgnoreCase(target.command)) {
				mc.thePlayer.sendChatMessage("/gamemode " + target.command);
				closeMenu();
			}
		}
	}

	private static void openMenu() {
		isVisible = true;
		if (mc.playerController != null) {
			String currentMode = mc.playerController.getCurrentGameType().getName();
			int currentModeIndex = 0;
			for (int i = 0; i < gameModes.size(); i++) {
				if (gameModes.get(i).command.equalsIgnoreCase(currentMode)) {
					currentModeIndex = i;
					break;
				}
			}
			if (lastGameMode != null && !lastGameMode.equals(currentMode)) {
				for (int i = 0; i < gameModes.size(); i++) {
					if (gameModes.get(i).command.equals(lastGameMode)) {
						selectedIndex = i;
						break;
					}
				}
				selectedIndex = (currentModeIndex + 1) % gameModes.size();
			}
		}
	}
	
	private static void nextItem() {
		selectedIndex++;
		if (selectedIndex >= gameModes.size()) selectedIndex = 0;
	}
	
	private static void closeMenu() {
		mc.displayGuiScreen(null);
		isVisible = false;
	}
	
	private static boolean hasPermission() {
		System.out.println(mc.thePlayer.canCommandSenderUseCommand(2, "gamemode"));
    	return mc.thePlayer != null && mc.thePlayer.canCommandSenderUseCommand(2, "gamemode");
    }
}
