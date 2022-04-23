package blu3.ruhamaplus.command;


import blu3.ruhamaplus.module.modules.gui.ClickGui;
import blu3.ruhamaplus.utils.ChatUtils;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

/**
 **  @author blu3
 **/

public class FixGuiCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".fixgui";
    }

    public String getUsage(ICommandSender sender) { return null; }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        {
            try
            {
                ClickGui.clickGui.initWindows();
                ChatUtils.log("Reset GUI");
            } catch (Exception e)
            {
                ChatUtils.error(this, "something went wrong");
            }
        }
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

}

