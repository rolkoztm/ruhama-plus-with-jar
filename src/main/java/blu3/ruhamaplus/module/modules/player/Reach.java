package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.event.events.ReachEvent;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class Reach extends Module {

    public Reach()
    {
        super("Reach", 0, Category.PLAYER, "reach further", settings);
    }

    private static final List<SettingBase> settings = Collections.singletonList(new SettingSlider(0, 3, 1, 0, "Distance: "));

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void reach(ReachEvent event) {
        event.reachDistance = (float) getSlider("Distance: ") + 3;
    }

}

