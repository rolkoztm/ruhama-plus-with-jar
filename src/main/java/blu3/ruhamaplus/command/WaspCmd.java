package blu3.ruhamaplus.command;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.combat.LongRangeAim;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.Objects;

/**
 **  @author blu3
 **/

//  holy shit this actually works

public class
WaspCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".aim";
    }

    public String getUsage(ICommandSender sender) { return null; }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length != 1)
        {
            ChatUtils.error(this,"one word retard");
        } else
        {
            try
            {
                ((LongRangeAim) Objects.requireNonNull(ModuleManager.getModuleByName("LongRangeAim"))).cmdTarget = args[0];

                if (Objects.requireNonNull(ModuleManager.getModuleByName("LongRangeAim")).getSetting(1).asMode().mode == 1) {
                    ModuleManager.getModuleByName("LongRangeAim").setToggled(true);
                    ChatUtils.log("Set target as " + args[0]);
                }
                else if (Objects.requireNonNull(ModuleManager.getModuleByName("WaspAim")).getSetting(1).asMode().mode == 0) {
                    ChatUtils.warn("Set target as " + args[0] + ", however LongRangeAim is set to Closest.");
                }

            } catch (Exception e)
            {
                ChatUtils.error(this, "incorrect, /.aim [target]");
            }
        }
    }
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
