package blu3.ruhamaplus.command;

import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

public class FOVCmd extends CommandBase implements IClientCommand {
        public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
        {
            return false;
        }

        public String getName()
        {
            return ".fov";
        }

        public String getUsage(ICommandSender sender) { return null; }

        public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
            if (args.length == 0) {
                ChatUtils.warn("/.fov [number]");
            }

            if(args.length == 1){
                try {
                    Minecraft.getMinecraft().gameSettings.fovSetting = Integer.parseInt(args[0]);
                } catch (Exception youRetard) {
                    ChatUtils.error(this, "dont fucking use letters you buffoon");
                }
            }
        }
        public boolean checkPermission(MinecraftServer server, ICommandSender sender)
        {
            return true;
        }
    }
