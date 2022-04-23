package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ShulkerAura extends Module
{
    public boolean inShulker = false;
    public HashMap<BlockPos, Integer> openedShulkers = new HashMap<>();

    public ShulkerAura()
    {
        super("ShulkerAura", 0, Category.COMBAT, "Automatically opens shulkers", null);
    }

    public void onUpdate()
    {
        HashMap<BlockPos, Integer> tempShulkers = new HashMap<>(this.openedShulkers);

        Entry e;

        for (Iterator shulkerIter = this.openedShulkers.entrySet().iterator(); shulkerIter.hasNext(); tempShulkers.replace((BlockPos) e.getKey(), (Integer) e.getValue() - 1))
        {
            e = (Entry) shulkerIter.next();

            if ((Integer) e.getValue() <= 0)
            {
                tempShulkers.remove(e.getKey());
            }
        }

        this.openedShulkers.clear();
        this.openedShulkers.putAll(tempShulkers);

        if (!(this.mc.currentScreen instanceof GuiContainer) || this.mc.currentScreen instanceof GuiShulkerBox)
        {
            if (this.mc.currentScreen instanceof GuiShulkerBox)
            {
                if (this.inShulker)
                {
                    this.mc.displayGuiScreen(null);
                }

                this.inShulker = false;
            } else
            {
                for (int x = -4; x <= 4; ++x)
                {
                    for (int y = -4; y <= 4; ++y)
                    {
                        for (int z = -4; z <= 4; ++z)
                        {
                            BlockPos pos = this.mc.player.getPosition().add(x, y, z);

                            if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox && !RuhamaPlus.friendBlocks.containsKey(pos) && !this.openedShulkers.containsKey(pos) && this.mc.player.getPositionVector().distanceTo((new Vec3d(pos)).add(0.5D, 0.5D, 0.5D)) <= 5.25D)
                            {
                                WorldUtils.openBlock(pos);

                                this.openedShulkers.put(pos, 300);
                                this.inShulker = true;

                                return;
                            }
                        }
                    }
                }

            }
        }
    }
}
