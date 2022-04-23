package blu3.ruhamaplus.module.modules.misc;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.Objects;

public class AirBedPlace extends Module {
    public AirBedPlace() {
        super("AirBedPlace", 0, Category.MISC, "place bed in air", null);
    }

    @Override
    public void onEnable() {
        if (mc.currentScreen instanceof GuiContainer) return;

        int slot = -1;

        if (this.mc.player.inventory.getItemStack().isEmpty()) {
            for (int i = 0; i < 9; i++) if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            setToggled(false);
            return;
        }
        RayTraceResult ray = this.mc.player.rayTrace(5.0D, this.mc.getRenderPartialTicks());
        BlockPos pos = Objects.requireNonNull(ray).getBlockPos().up();

        WorldUtils.placeBlock(pos, slot, true, false);
        WorldUtils.openBlock(pos);

        setToggled(false);
    }
}
