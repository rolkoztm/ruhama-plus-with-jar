package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;

import java.util.Collections;
import java.util.List;

public class FOVSlider extends Module {
    private static final List<SettingBase> settings = Collections.singletonList(new SettingSlider(0, 360, 140, 10, "FOV: "));
    public FOVSlider()
    {
        super("FOVSlider", 0, Category.RENDER, "chanje eff oh vee", settings);
    }
    private int fov;

    public void onEnable() {
        this.fov = (int) this.mc.gameSettings.fovSetting;
    }

    public void onUpdate(){
     this.mc.gameSettings.fovSetting = (float) this.getSetting(0).asSlider().getValue();
    }

    public void onDisable() {
        this.mc.gameSettings.fovSetting = this.fov;
    }
}
