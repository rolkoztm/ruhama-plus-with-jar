package blu3.ruhamaplus.command;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.exploits.FakePlayer;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 **  @author blu3
 **/

public class FakePlayerCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".fakeplayer";
    }

    public String getUsage(ICommandSender sender) { return null; }

    private List<String> aliases = new ArrayList<>();

    public FakePlayerCmd(){
        aliases.add(".fp");
    }

    public List<String> getAliases(){
        return aliases;
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

        if (!(args.length == 1)) ChatUtils.warn("/.fakeplayer [name]");


        else try {
            if (args.length == 1) {
                (((FakePlayer) Objects.requireNonNull(ModuleManager.getModuleByName("FakePlayer")))).name = args[0];
                ChatUtils.log("FakePlayer set to " + args[0]);
            }
        } catch (Exception e){
            ChatUtils.error(this, "no");
        }


    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
