package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.*;

import java.util.*;

/**
 * @see blu3.ruhamaplus.mixin.mixins.MixinGuiNewChat
 */

public class BetterChat extends Module {
    public BetterChat() {
        super("BetterChat", 0, Category.CHAT, "briusdah ul \"better\"", settings);
    }

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(false, "NoBackground"),
            new SettingToggle(false, "NameHighlight"),
            new SettingToggle(false, "CleanChat"));

    public boolean onPacketRead(Packet<?> packet) {
        if (packet instanceof SPacketChat && this.getBoolean("NameHighlight")) {
            SPacketChat sPacketChat = (SPacketChat) packet;
            String message = sPacketChat.getChatComponent().getFormattedText();

            String s = mc.player.getName();

            if (message.contains(s) && !message.startsWith("<" + s + ">")) {
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.BOLD + message));
                return true;
            }
        }
        if (packet instanceof SPacketChat && this.getBoolean("CleanChat")) {
            SPacketChat sPacketChat = (SPacketChat) packet;
            String message = sPacketChat.getChatComponent().getFormattedText();
            ChatUtils.cleanChatMessage(message);
            return true;
        }
        return false;
    }
}



