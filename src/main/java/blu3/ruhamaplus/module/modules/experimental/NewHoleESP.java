package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.HoleUtils;
import blu3.ruhamaplus.utils.ReflectUtils;
import blu3.ruhamaplus.utils.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewHoleESP extends Module {

    public NewHoleESP() {
        super("NewHoleESP", Keyboard.KEY_NONE, Category.EXPERIMENTAL, "gyufiseda ", null);
    }

    private double[] rPos;
    private final List<BlockPos> greenHoles = new ArrayList<>();
    private final List<BlockPos> yellowHoles = new ArrayList<>();
    private final List<BlockPos> redHoles = new ArrayList<>();


    public void onUpdate(){
        if (nullCheck()) return;
        if (mc.player.ticksExisted % 20 == 0) {

            greenHoles.clear();
            yellowHoles.clear();
            redHoles.clear();

            for (BlockPos p : findHoles()) {
                if (HoleUtils.isGreenHole(p)) greenHoles.add(p);
                if (HoleUtils.isYellowHole(p)) yellowHoles.add(p);
                if (HoleUtils.isRedHole(p)) redHoles.add(p);
            }
        }
    }

    public void onRender() {
        try {
            this.rPos = new double[] { (double) ReflectUtils.getField(RenderManager.class, "renderPosX", "field_78725_b").get(this.mc.getRenderManager()), (double)ReflectUtils.getField(RenderManager.class, "renderPosY", "field_78726_c").get(this.mc.getRenderManager()), (double)ReflectUtils.getField(RenderManager.class, "renderPosZ", "field_78723_d").get(this.mc.getRenderManager()) };
        }
        catch (Exception e) {
            this.rPos = new double[] { 0.0, 0.0, 0.0 };
        }

        if (!greenHoles.isEmpty())
        for (BlockPos pos : greenHoles) {
            final double x = pos.getX() - this.rPos[0];
            final double y = pos.getY() - this.rPos[1];
            final double z = pos.getZ() - this.rPos[2];
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), 0, 0, 255, 0.3f);
        }

        if (!yellowHoles.isEmpty())
        for (BlockPos pos : yellowHoles) {
            final double x = pos.getX() - this.rPos[0];
            final double y = pos.getY() - this.rPos[1];
            final double z = pos.getZ() - this.rPos[2];
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), 222, 235, 52, 0.3f);
        }

        if (!redHoles.isEmpty())
        for (BlockPos pos : redHoles) {
            final double x = pos.getX() - this.rPos[0];
            final double y = pos.getY() - this.rPos[1];
            final double z = pos.getZ() - this.rPos[2];
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), 255, 0, 0, 0.3f);
        }


    }

    private List<BlockPos> findHoles()
    {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(this.getSphere(this.getPlayerPos(), (float) 10, 10, false, true, 0).stream().filter(HoleUtils::isHole).collect(Collectors.toList()));

        return positions;
    }

    public BlockPos getPlayerPos()
    {
        return new BlockPos(Math.floor(this.mc.player.posX), Math.floor(this.mc.player.posY), Math.floor(this.mc.player.posZ));
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y)
    {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x)
        {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z)
            {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y)
                {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F))))
                    {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
}
