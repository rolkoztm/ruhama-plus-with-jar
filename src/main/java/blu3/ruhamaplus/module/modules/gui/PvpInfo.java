package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.gui.ruhama.AdvancedText;
import blu3.ruhamaplus.gui.ruhama.TextWindow;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PvpInfo extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "BedAura"),
            new SettingToggle(true, "AutoTrap"),
            new SettingToggle(true, "BedCount"),
            new SettingToggle(true, "TotemCount"));

    public PvpInfo()
    {
        super("PVPInfo", 0, Category.GUI, "peeveepee infourmasiyon", settings);

        this.getWindows().add(new TextWindow(2, 150, "BedAura"));
        this.getWindows().add(new TextWindow(32, 150, "AutoTrap"));
        this.getWindows().add(new TextWindow(92, 150, "BedCount"));
        this.getWindows().add(new TextWindow(62, 150, "TotemCount"));
    }


    public void onOverlay(){

        int t = this.getTotems();
        int b = this.getBeds();

        this.getWindows().get(0).clearText();
        this.getWindows().get(1).clearText();
        this.getWindows().get(2).clearText();
        this.getWindows().get(3).clearText();


        int age = (int) (System.currentTimeMillis() / 20L % 510L);
        int color = (new Color(255, MathHelper.clamp(age > 255 ? 510 - age : age, 0, 255), MathHelper.clamp(255 - (age > 255 ? 510 - age : age), 0, 255))).getRGB();



        if (this.getSetting(0).asToggle().state){
            if (ModuleManager.getModuleByName("BedAura").isToggled() && !(ModuleManager.getModuleByName("1.12 BedAura").isToggled())){
                this.getWindows().get(0).addText(new AdvancedText("BedAura: ECME", true, color));
            } else if (ModuleManager.getModuleByName("BedAura").isToggled() && ModuleManager.getModuleByName("1.12 BedAura").isToggled()){
                this.getWindows().get(0).addText(new AdvancedText("BedAura: what the fuck be careful you retard", true, color));
            } else { this.getWindows().get(0).addText(new AdvancedText("BedAura: OFF", true, color)); }





            }
        if (this.getSetting(1).asToggle().state){
            if (ModuleManager.getModuleByName("BedObsidianTrap").isToggled()){
            this.getWindows().get(1).addText(new AdvancedText("AutoTrap: ON", true, color));
        } else {
            this.getWindows().get(1).addText(new AdvancedText("AutoTrap: OFF", true, color));
        }
        }


        if (this.getSetting(2).asToggle().state) this.getWindows().get(2).addText(new AdvancedText("Totems: " + t, true, color));
        if (this.getSetting(3).asToggle().state) this.getWindows().get(3).addText(new AdvancedText("Beds: " + b, true, color));

    }

    private int getTotems()
    {
        int c = 0;

        for (int i = 0; i < 45; ++i)
        {
            if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING)
            {
                ++c;
            }
        }

        return c;
    }

    private int getBeds()
    {
        int c = 0;

        for (int i = 0; i < 45; ++i)
        {
            if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED)
            {
                ++c;
            }
        }

        return c;
    }
}
