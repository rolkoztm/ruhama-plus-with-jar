package blu3.ruhamaplus.command;

import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

/**
 **  @author blu3
 **/

public class HelpCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".help";
    }

    public String getUsage(ICommandSender sender) { return null; }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

        if (args.length == 0) {
            ChatUtils.log("/.help [wasp/entitydesync/invsorter/peek/login/stashfinder/setyaw/help]");
        }

        if (args.length == 1 && (args[0].equalsIgnoreCase("aim")
                || args[0].equalsIgnoreCase("entitydesync")
                || args[0].equalsIgnoreCase("invsorter")
                || args[0].equalsIgnoreCase("peek"))
                || args[0].equalsIgnoreCase("login")
                || args[0].equalsIgnoreCase("stashfinder")
                || args[0].equalsIgnoreCase("setyaw")
                || args[0].equalsIgnoreCase("saveconfig")
                || args[0].equalsIgnoreCase("help")) {
            // -------------------------------------------------------
            if (args[0].equalsIgnoreCase("aim")) {
               ChatUtils.log("long range aimbot. set LongRangeAim to command, then do /.aim [target] (not case sensitive)");
            } else if (args[0].equalsIgnoreCase("entitydesync")) {
                ChatUtils.log("allows you to do the old godmode exploit. /.entitydesync (dismount/remount)");
            } else if (args[0].equalsIgnoreCase("invsorter")) {
                ChatUtils.log("sorts your hotbar. use /.invorter to save your hotbar and toggle the module to fix it. (buggy on most servers)");
            } else if (args[0].equalsIgnoreCase("peek")) {
                ChatUtils.log("peeks shulkers and maps. /.peek");
            } else if (args[0].equalsIgnoreCase("login")) {
                ChatUtils.log("a more painful form of account manager, only really useful in a development environment. /.login [MC email] [MC password] (only use this if you trust me kek)");
            } else if (args[0].equalsIgnoreCase("stashfinder")) {
                ChatUtils.log("sets stashfinder to start at a specific location. /.stashfinder [x] [z]");
            } else if (args[0].equalsIgnoreCase("setyaw")) {
                ChatUtils.log("rotates your client to face a specific location. useful for travelling directly to bases. /.setyaw [x] [z]");
            } else if (args[0].equalsIgnoreCase("saveconfig")) {
                ChatUtils.log("Saves your ClickGUI, enabled modules, and new binds. this is done automatically from time to time. /.saveconfig");
            } else if (args[0].equalsIgnoreCase("help")) {
                ChatUtils.warn("you are an idiot");
            }

        } else {
           // ClientChat.error("HelpCmd","/.help [wasp/entitydesync/invsorter/peek/login/stashfinder/setyaw/help]");
            ChatUtils.error(this,"/.help [wasp/entitydesync/invsorter/peek/login/stashfinder/setyaw/help]");
        }
    }
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
