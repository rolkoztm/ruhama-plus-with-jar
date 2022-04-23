package blu3.ruhamaplus.module.modules.misc;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * *  Skidded from SalHack
 **/

public class ChestSwap extends Module {

    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Prefer Elytra"), new SettingToggle(true, "Curse"));

    public ChestSwap() {
        super("ChestSwap", 0, Category.MISC, "swap chestplate ok", settings);
    }


    @Override
    public void onEnable() {
        if (this.mc.player == null) return;
        ItemStack l_ChestSlot = mc.player.inventoryContainer.getSlot(6).getStack();
        if (l_ChestSlot.isEmpty()) {
            int l_Slot = FindChestItem(this.getSetting(2).asToggle().state);

            if (!this.getSetting(2).asToggle().state && l_Slot == -1)
                l_Slot = FindChestItem(true);

            if (l_Slot != -1) {
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
            }

            this.setToggled(false);
            return;
        }

        int l_Slot = FindChestItem(l_ChestSlot.getItem() instanceof ItemArmor);

        if (l_Slot != -1) {
            this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
            this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
            ChatUtils.log("ChestSwap: Swapped chest item.");
        }

        this.setToggled(false);
        return;
    }

    private int FindChestItem(boolean p_Elytra) {
        int slot = -1;
        float damage = 0;

        for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
            /// @see: https://wiki.vg/Inventory, 0 is crafting slot, and 5,6,7,8 are Armor slots
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8)
                continue;

            ItemStack s = mc.player.inventoryContainer.getInventory().get(i);
            if (s != null && s.getItem() != Items.AIR) {
                if (s.getItem() instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor) s.getItem();
                    if (armor.armorType == EntityEquipmentSlot.CHEST) {
                        final float currentDamage = (armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, s));

                        final boolean cursed = this.getSetting(1).asToggle().state ? (EnchantmentHelper.hasBindingCurse(s)) : false;

                        if (currentDamage > damage && !cursed) {
                            damage = currentDamage;
                            slot = i;
                        }
                    }
                    break;
                } else if (p_Elytra && s.getItem() instanceof ItemElytra)
                    return i;
            }
        }

        return slot;
    }
}
