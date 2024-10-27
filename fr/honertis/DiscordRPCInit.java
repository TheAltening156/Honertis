package fr.honertis;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRPCInit {
	public static String id = "1299778561762398209";
	public static Thread rpcThread;
	
	
	public static void init() {
		 DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder("https://bit.ly/honertis");            
            presence.setSmallImage("png", "hehe");
            presence.setDetails(Honertis.name + " v" + Honertis.version);
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
		/*DiscordRPC rpc = DiscordRPC.INSTANCE;
		DiscordEventHandlers h = new DiscordEventHandlers();
		h.ready = (user) -> System.out.println("Ready !");
		rpc.Discord_Initialize(id, h, true, "");
		DiscordRichPresence drpc = new DiscordRichPresence();
		drpc.startTimestamp = System.currentTimeMillis() / 1000;
		drpc.details = "Test";
		drpc.state = "Test 2";
		rpc.Discord_UpdatePresence(drpc);
		
		new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				rpc.Discord_RunCallbacks();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
			}
		}, "RPC-Callback").start();*/
	}
}
