package fr.honertis;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordRPCInit {
	public static String id = "1299778561762398209";
	public static void init() {
		DiscordRPC rpc = DiscordRPC.INSTANCE;
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
		}, "RPC-Callback").start();
	}
}
