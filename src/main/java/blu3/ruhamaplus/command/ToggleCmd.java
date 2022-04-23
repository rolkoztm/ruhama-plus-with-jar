package blu3.ruhamaplus.command;

import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.utils.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.ArrayList;
import java.util.List;

public class ToggleCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".toggle";
    }

    private List<String> aliases = new ArrayList<>();

    public ToggleCmd(){
        aliases.add(".t");
    }


    public List<String> getAliases(){
        return aliases;
    }

    public String getUsage(ICommandSender sender) { return null; }
    
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

        if (args.length == 0) {
            ChatUtils.warn("module? bruh");
            return;
        }
        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            ChatUtils.warn("what ths fuck is '" + args[0] + "'");
            return;
        }
        m.toggle();
        ChatUtils.log(m.getName() + (m.isToggled() ? ChatFormatting.GREEN + " enabled" : ChatFormatting.RED + " disabled"));
    }
}
