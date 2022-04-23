package blu3.ruhamaplus.command;

import blu3.ruhamaplus.utils.ChatUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.ArrayList;
import java.util.List;

public class FriendCommand extends CommandBase implements IClientCommand {
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".friend";
    }

    private List<String> aliases = new ArrayList<>();

    public FriendCommand(){
        aliases.add(".f");
    }


    public List<String> getAliases(){
        return aliases;
    }


    public String getUsage(ICommandSender sender) { return null; }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 0) {
            ChatUtils.warn("/.friend [name]");
        }

        if(args.length == 1){
            FriendManager.get().addFriend(args[0]);
        }
    }
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
