package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;

import java.util.Arrays;
import java.util.List;

public class TestAutoTotem extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "Offhand"),
            new SettingToggle(false, "Hotbar"),
            new SettingToggle(true, "Delay"),
            new SettingSlider(0.0D, 2.0D, 0.25D, 3, "Delay: "),
            new SettingToggle(false, "2b Bypass"));
    private long time = 0L;

    public TestAutoTotem()
    {
        super("AutoTotem", 0, Category.COMBAT, "Automatically places totems in your offhand, test for 2b bypass", settings);
    }


    public void onUpdate()
    {
        if (!this.getSettings().get(2).asToggle().state || (double) (System.currentTimeMillis() - this.time) >= this.getSettings().get(3).asSlider().getValue() * 1000.0D)
        {
            this.time = System.currentTimeMillis();

            if (this.mc.currentScreen == null || !(this.mc.currentScreen instanceof GuiHopper))
            {
                int i;

                if (this.getSettings().get(0).asToggle().state && this.mc.player.getHeldItemOffhand().getItem() == Items.AIR)
                {
                    for (i = 9; i <= 44; ++i)
                    {
                        if (this.mc.player.inventoryContainer.getSlot(i).getStack().getItem() == Items.TOTEM_OF_UNDYING)
                        {

                            if (this.getSetting(4).asToggle().state) {
                                this.mc.player.motionX = 0;
                                this.mc.player.motionY = 0;
                                this.mc.player.motionZ = 0;
                            }
                            this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 0, ClickType.PICKUP, this.mc.player);
                            this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, this.mc.player);
                            break;
                        }
                    }

                }

                if (this.getSettings().get(1).asToggle().state) {

                    if (this.mc.player.inventory.getStackInSlot(0).getItem() == Items.TOTEM_OF_UNDYING || this.mc.player.inventory.getStackInSlot(0).getItem() == Items.DIAMOND_SWORD) {
                        return;
                    }

                    for (i = 9; i < 35; ++i) {
                        if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            if (this.getSetting(4).asToggle().state) {
                                this.mc.player.motionX = 0;
                                this.mc.player.motionY = 0;
                                this.mc.player.motionZ = 0;
                            }
                            this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, this.mc.player);
                            break;
                        }
                    }

                }
            }
        }
    }
}

