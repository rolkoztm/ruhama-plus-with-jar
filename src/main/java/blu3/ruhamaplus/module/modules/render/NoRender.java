package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.event.events.EventEntityAdded;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class NoRender extends Module {

    public NoRender() {
        super("NoRender", Category.RENDER, "basically a bunch of completely different modules all in one", Arrays.asList(
                witherSkulls,
                cameraClip
        ));
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static SettingToggle witherSkulls = new SettingToggle(true, "Wither Skulls");
    public static SettingToggle cameraClip = new SettingToggle(false, "CameraClip");
    public static SettingMode witherSkullsMode = new SettingMode("WitherSkulls Mode", "");

    @SubscribeEvent
    public void onEntityAdded(EventEntityAdded event){
        if (event.getEntity() instanceof EntityWitherSkull && witherSkulls.getValue()) event.setCanceled(true);
    }
}
