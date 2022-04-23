package blu3.ruhamaplus.module.modules.misc;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.ChatUtils;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.Objects;

public class AutoBedCrafter extends Module {
    public AutoBedCrafter() {
        super("AutoBedCrafter", 0, Category.MISC, "Automatically places a crafting table, and crafts beds", null);
    }

    private int stage;

    @Override
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer) return;

        int tableSlot = -1;
        int woodSlot = -1;
        int woolSlot = -1;

        if (this.stage == 0) {

            if (this.mc.player.inventory.getItemStack().isEmpty()) {
                for (int i = 0; i < 9; i++)
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE)) {
                        tableSlot = i;
                        break;
                    }
                for (int i = 0; i < 9; i++)
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(Blocks.PLANKS)) {
                        if (mc.player.inventory.getStackInSlot(i).getCount() >= 3) woodSlot = i;
                        break;
                    }
                for (int i = 0; i < 9; i++)
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(Blocks.WOOL)) {
                        if (mc.player.inventory.getStackInSlot(i).getCount() >= 3) woolSlot = i;
                        break;
                    }
            }

            if (tableSlot != -1) ChatUtils.log("Crafting table slot: " + tableSlot);
            if (woodSlot != -1) ChatUtils.log("Wooden planks slot: " + woodSlot);
            if (woolSlot != -1) ChatUtils.log("Wool slot: " + woolSlot);

            RayTraceResult ray = this.mc.player.rayTrace(5.0D, this.mc.getRenderPartialTicks());
            BlockPos pos = Objects.requireNonNull(ray).getBlockPos().up();

            WorldUtils.placeBlock(pos, tableSlot, true, false);
            WorldUtils.openBlock(pos);
            this.stage = 1;
            return;
        }

        if (this.stage == 1) {

            this.mc.playerController.windowClick(mc.player.openContainer.windowId, woodSlot + 37, 0, ClickType.SWAP, mc.player);
            this.mc.player.closeScreen();
            this.setToggled(false);
        }

    }

    public void onEnable() {
        this.stage = 0;
    }
}