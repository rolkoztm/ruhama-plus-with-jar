package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Surround extends Module
{
    public Surround()
    {
        super("Surround", 0, Category.COMBAT, "Build obsidian around you to protect you from crystals", Arrays.asList(new SettingMode("Mode: ", "1x1", "2x2", "Smart"), new SettingToggle(true, "Switch Back"), new SettingToggle(false, "2 High"), new SettingToggle(true, "2b Bypass"), new SettingToggle(true, "Center ")));
    }

    private Vec3d Center = Vec3d.ZERO;

    @Override
    public void onEnable() {
        Center = GetCenter(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ);

        if (this.getSetting(4).asToggle().state)
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
            mc.player.connection.sendPacket(new CPacketPlayer.Position(Center.x, Center.y, Center.z, true));
            mc.player.setPosition(Center.x, Center.y, Center.z);
        }
    }

    public void onUpdate()
    {
        int obsidian = -1;

        int cap;

        for (cap = 0; cap < 9; ++cap)
        {
            if (this.mc.player.inventory.getStackInSlot(cap).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN))
            {
                obsidian = cap;

                break;
            }
        }

        cap = 0;
        List<BlockPos> poses = new ArrayList<>();

        boolean rotate = this.getSetting(3).asToggle().state;

        if (this.getSetting(0).asMode().mode == 0)
        {
            poses.addAll(Arrays.asList((new BlockPos(this.mc.player.getPositionVector())).add(0, 0, 1), (new BlockPos(this.mc.player.getPositionVector())).add(1, 0, 0), (new BlockPos(this.mc.player.getPositionVector())).add(0, 0, -1), (new BlockPos(this.mc.player.getPositionVector())).add(-1, 0, 0)));
        } else if (this.getSetting(0).asMode().mode == 1)
        {
            poses.addAll(Arrays.asList((new BlockPos(this.mc.player.getPositionVector())).add(0, 0, 2), (new BlockPos(this.mc.player.getPositionVector())).add(2, 0, 0), (new BlockPos(this.mc.player.getPositionVector())).add(0, 0, -2), (new BlockPos(this.mc.player.getPositionVector())).add(-2, 0, 0)));
        } else if (this.getSetting(0).asMode().mode == 2)
        {
            poses.addAll(Arrays.asList((new BlockPos(this.mc.player.getPositionVector().add(0.0D, 0.0D, -this.mc.player.width))).add(0, 0, -1), (new BlockPos(this.mc.player.getPositionVector().add(-this.mc.player.width, 0.0D, 0.0D))).add(-1, 0, 0), (new BlockPos(this.mc.player.getPositionVector().add(0.0D, 0.0D, this.mc.player.width))).add(0, 0, 1), (new BlockPos(this.mc.player.getPositionVector().add(this.mc.player.width, 0.0D, 0.0D))).add(1, 0, 0)));
        }

        for (Object o : new ArrayList<>(poses))
        {
            BlockPos b = (BlockPos) o;
            poses.add(0, b.down());

            if (this.getSetting(2).asToggle().state)
            {
                poses.add(0, b.up());
            }
        }

        if (obsidian != -1)
        {
            int hand = this.mc.player.inventory.currentItem;

            for (BlockPos b : poses)
            {
                if (WorldUtils.placeBlock(b, obsidian, rotate, false) == 1)
                {
                    this.mc.player.swingArm(EnumHand.MAIN_HAND);
                    ++cap;
                }

                if (cap > 2)
                {
                    break;
                }
            }

            if (this.getSetting(1).asToggle().state)
            {
                this.mc.player.inventory.currentItem = hand;
            }
        }
    }

    public Vec3d GetCenter(double posX, double posY, double posZ)
    {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }

}
