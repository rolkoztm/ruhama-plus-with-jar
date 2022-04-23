/*
package blu3.ruhamaplus.utils;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.misc.StashFinder;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.math.ChunkPos;

import java.util.Objects;


public class Discord {
        private Thread _thread = null;
        public void enable()
        {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "759623728970596364";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            handlers.ready = (user) -> System.out.println("Ready!");
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            presence.largeImageKey = "bruh";
            presence.largeImageText = "allah haram";
            _thread = new Thread(() ->
            {
                while (!Thread.currentThread().isInterrupted())
                {
                    lib.Discord_RunCallbacks();
                    presence.details = ((blu3.ruhamaplus.module.modules.misc.DiscordRPC) Objects.requireNonNull(ModuleManager.getModuleByName("DiscordRPC"))).generateDetails();
                    presence.state = ((blu3.ruhamaplus.module.modules.misc.DiscordRPC) Objects.requireNonNull(ModuleManager.getModuleByName("DiscordRPC"))).generateState();
                    lib.Discord_UpdatePresence(presence);
                    try
                    {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored)
                    {
                    }
                }
            }, "RPC-Callback-Handler");

            _thread.start();
        }

        public void disable() throws InterruptedException
        {
            if (_thread != null)
                _thread.interrupt();
        }

        public static Discord Get()
        {
            return RuhamaPlus.GetDiscord();
        }
    }

*/
