package blu3.ruhamaplus.command;

import blu3.ruhamaplus.utils.ChatUtils;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

/**
 **  @author blu3
 **/

public class SetYawCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".setyaw";
    }

    public String getUsage(ICommandSender sender) { return null; }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 2)
        {
            ChatUtils.error("SetYawCmd", "Invalid number of arguments, use /.setyaw x z");
        } else
        {
            try
            {
                WorldUtils.facePos(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                ChatUtils.log("Set yaw to look at: " + args[0] + ", " + args[1]);
            } catch (Exception e)
            {
                ChatUtils.error("SetYawCmd", "not letters you retard");
            }
        }
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

}

