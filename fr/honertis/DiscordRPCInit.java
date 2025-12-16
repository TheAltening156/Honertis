package fr.honertis;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class DiscordRPCInit {
	public static String id = "1299778561762398209";
	public static Thread rpcThread;
	
	
	public static void init() {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder("https://bit.ly/honertis");            
            presence.setSmallImage("512", Minecraft.getMinecraft().getSession().getUsername());
            presence.setBigImage("1024", Honertis.INSTANCE.name + " v" + Honertis.INSTANCE.version);
            presence.setDetails(Honertis.INSTANCE.name + " v" + Honertis.INSTANCE.version);
            presence.setStartTimestamps(System.currentTimeMillis() / 1000);
            DiscordRPC.discordUpdatePresence(presence.build());
        }).build();
        DiscordRPC.discordInitialize(id, handlers, false);
        DiscordRPC.discordRegister(id, "");
        rpcThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				DiscordRPC.discordRunCallbacks();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
			}
		}, "RPC-Callback");
        rpcThread.start();
	}
}
