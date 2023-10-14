package fr.honertis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static void printChat(String text) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
	}
	
}
