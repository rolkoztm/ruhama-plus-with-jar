package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.gui.ruhama.AdvancedText;
import blu3.ruhamaplus.gui.ruhama.TextWindow;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class PlayerRadar extends Module
{
    private static final List<SettingBase> settings = Collections.singletonList(new SettingToggle(false, "Round HP"));

    public PlayerRadar()
    {
        super("PlayerList", 0, Category.GUI, "Shows nearby people", settings);

        this.getWindows().add(new TextWindow(100, 150, "PlayerList"));
    }

    public void onOverlay()
    {
        int c = Gui.arrayListEnd + 10;
        this.getWindows().get(0).clearText();

        for (EntityPlayer e : this.mc.world.playerEntities)
        {
            if (e != this.mc.player)
            {
                int color = 0;

                try
                {
                    color = e.getHealth() + e.getAbsorptionAmount() > 20.0F ? 2158832 : MathHelper.hsvToRGB((e.getHealth() + e.getAbsorptionAmount()) / 20.0F / 3.0F, 1.0F, 1.0F);
                } catch (Exception ignored)
                {
                }

                double health = (BigDecimal.valueOf(e.getHealth() + e.getAbsorptionAmount())).setScale(1, RoundingMode.HALF_UP).doubleValue();
                double dist = (BigDecimal.valueOf(e.getDistance(this.mc.player))).setScale(1, RoundingMode.HALF_UP).doubleValue();

                boolean round = this.getSetting(0).asToggle().state;
                boolean dead = e.getHealth() <= 0.0F;

                if (round)
                {
                    if (dead)
                    {
                        this.getWindows().get(0).addText(new AdvancedText((int) health + " " + e.getName() + " " + (int) dist + "m", true, color));
                    } else
                    {
                        this.getWindows().get(0).addText(new AdvancedText((int) health + " " + (this.mc.objectMouseOver.entityHit == e ? TextFormatting.GOLD.toString() : TextFormatting.GRAY.toString()) + e.getName() + " " + TextFormatting.DARK_GRAY.toString() + (int) dist + "m", true, color));
                    }
                } else if (dead)
                {
                    this.getWindows().get(0).addText(new AdvancedText(health + " " + e.getName() + " " + dist + "m", true, color));
                } else
                {
                    this.getWindows().get(0).addText(new AdvancedText(health + " " + (this.mc.objectMouseOver.entityHit == e ? TextFormatting.GOLD.toString() : TextFormatting.GRAY.toString()) + e.getName() + " " + TextFormatting.DARK_GRAY.toString() + dist + "m", true, color));
                }

                c += 10;
            }
        }

        Gui.arrayListEnd = c;
    }
}
