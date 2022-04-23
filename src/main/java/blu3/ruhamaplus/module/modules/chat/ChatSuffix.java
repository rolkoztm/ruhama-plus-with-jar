package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.Objects;

public class ChatSuffix extends Module {

    public static SettingMode suffix = new SettingMode("Suffix: ", "Ruhama+", "Guinness");

    public ChatSuffix() {
        super("ChatSuffix", 0, Category.CHAT, "FATASS", Collections.singletonList(suffix));
    }

    public static String getSuffix() {
        if (suffix.is("Ruhama+")) return ChatUtils.toUnicode("ruhama+");
        else return ChatUtils.toUnicode("Guinness");
    }


    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if (!event.getMessage().contains(ChatSuffix.getSuffix()) && !event.getMessage().startsWith("/") && !event.getMessage().startsWith("!")) {
            event.setCanceled(true);

           if (ModuleManager.getModuleByName("GreenText").isToggled()) {
               mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
               mc.player.sendChatMessage("> " + event.getMessage() + " \u23D0 " + ChatSuffix.getSuffix());
           } else {
               mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
               mc.player.sendChatMessage(event.getMessage() + " \u23D0 " + ChatSuffix.getSuffix());
           }


        }
    }

}

