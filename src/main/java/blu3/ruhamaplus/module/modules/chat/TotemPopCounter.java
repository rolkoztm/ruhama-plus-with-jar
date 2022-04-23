package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.event.events.TotemPopEvent;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TotemPopCounter extends Module {

    private static final List<SettingBase> settings = Collections.singletonList(new SettingMode("Mode: ", "Client Side", "Public Chat"));

    public TotemPopCounter() {
        super("TotemPopCounter", 0, Category.CHAT, "ezzz pops keep running ni-", settings);
    }

    private HashMap<String, Integer> popList = new HashMap();

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void throwNewPoppedTotem(TotemPopEvent event) {
        if (popList == null) {
            popList = new HashMap<>();
        }
        Entity e = event.getEntity();

        if (popList.get(e.getName()) == null) {
            popList.put(e.getName(), 1);

            if (this.getSettings().get(0).asMode().mode == 0) {
                ChatUtils.log(e.getName() + " popped " + 1 + " totem");
            } else {
                this.mc.player.sendChatMessage(e.getName() + " popped " + 1 + " totem");
            }
        } else if (!(popList.get(e.getName()) == null)) {
            int popCounter = popList.get(e.getName());
            int newPopCounter = popCounter += 1;
            popList.put(e.getName(), newPopCounter);
            if (this.getSettings().get(0).asMode().mode == 0) {
                ChatUtils.log(e.getName() + " popped " + newPopCounter + " totems");
            } else {
                this.mc.player.sendChatMessage(e.getName() + " popped " + newPopCounter + " totems");
            }
        }
    }

    public void onUpdate() {
        if (!(this.mc.player == null) && !(this.mc.world == null)) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.getHealth() <= 0) {
                    if (popList.containsKey(player.getName())) {
                        if (this.getSettings().get(0).asMode().mode == 0) {
                            ChatUtils.log(player.getName() + " died after popping " + popList.get(player.getName()) + " totems");
                        } else {
                            this.mc.player.sendChatMessage(player.getName() + " died after popping " + popList.get(player.getName()) + " totems");
                        }
                        popList.remove(player.getName(), popList.get(player.getName()));
                    }
                }
            }
        }
    }
}
