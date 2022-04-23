package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VisualRange extends Module {
    public VisualRange() {
        super("VisualRange", 0, Category.CHAT, "Alerts you when someone enters your render distance", null);
    }

    List<Entity> knownPlayers = new ArrayList<>();
    ;
    List<Entity> players;

    public void onUpdate() {
        if (mc.player == null) return;
        players = mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList());
        try {
            for (Entity e : players) {
                if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
                    if (!knownPlayers.contains(e)) {
                        knownPlayers.add(e);
                        ChatUtils.log(e.getName() + " entered visual range.");
                    }
                }
            }
        } catch (Exception e) {
        }
        try {
            for (Entity e : knownPlayers) {
                if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
                    if (!players.contains(e)) {
                        knownPlayers.remove(e);
                        ;
                        ChatUtils.log(e.getName() + " left visual range.");
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void onDisable() {
        knownPlayers.clear();
    }
}
