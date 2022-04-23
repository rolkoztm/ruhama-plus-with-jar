package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.init.Items;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", Keyboard.KEY_NONE, Category.PLAYER, "fsedfsafdf", settings);
    }

    public static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "XP"),
            new SettingToggle(true, "Crystals"),
            new SettingToggle(true, "Everything")
            );

    public void fastUpdate() {
        if (this.mc.player != null) {
            if (this.getBoolean("XP") && this.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) this.mc.rightClickDelayTimer = 0;
            if (this.getBoolean("Crystals") && this.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) this.mc.rightClickDelayTimer = 0;
            if (this.getBoolean("Everything")) this.mc.rightClickDelayTimer = 0;
        }
    }
}
