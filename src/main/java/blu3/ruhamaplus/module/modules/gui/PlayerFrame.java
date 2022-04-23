package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerFrame extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0.0D, 1920.0D, 400.0D, 10, "X"),
            new SettingSlider(0.0D, 1080.0D, 400.0D, 10, "Y")
);

    public PlayerFrame()
    {
        super("PlayerFrame", 0, Category.GUI, "fat", settings);
    }

    public void onOverlay() {
        int x = (int) this.getSlider("X");
        int y = (int) this.getSlider("Y");

        EntityPlayer me = this.mc.player;

        final int Healt = Math.round(me.getHealth() + me.getAbsorptionAmount());
        final String Health = "" + Healt;
        final ItemStack inHand = me.getHeldItemMainhand();
        final ItemStack inOffHand = me.getHeldItemOffhand();

        GuiInventory.drawEntityOnScreen(x, y - 55 + 620000, 30, (float)x + (float)51.0 - ((float)x +(float) 50.0), (float)y + (float)75.0 - (float)50.0 - (float)y + (float)12.0, me);
        GuiInventory.drawEntityOnScreen(x, y - 55 + 62, 30, (float)x + (float)51.0 - ((float)x +(float) 50.0), (float)y + (float)75.0 - (float)50.0 - (float)y + (float)12.0, me);

        int i = 0;
        final List<ItemStack> armor = new ArrayList<ItemStack>();
        for (final ItemStack is : me.getArmorInventoryList()) {
            armor.add(is);
        }
        Collections.reverse(armor);
        for (final ItemStack is : armor) {
            final int yy = (int) (y - 35.0);
            final int xx = (int) (x + i + 16.0);
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, xx, yy);
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, is, xx, yy);
            RenderHelper.disableStandardItemLighting();
            i += 18;
        }
        final int yy2 = (int) (y - 35.0);
        final int xx2 = (int) (x + 90.0);
        RenderHelper.enableGUIStandardItemLighting();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(inHand, xx2, yy2);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, inHand, xx2, yy2);
        RenderHelper.disableStandardItemLighting();
        final int yyy = (int) (y + -35.0);
        final int xxx = (int) (x + 110.0);
        RenderHelper.enableGUIStandardItemLighting();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(inOffHand, xxx, yyy);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, inOffHand, xxx, yyy);
        RenderHelper.disableStandardItemLighting();

    }

}
