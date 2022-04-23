package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BedCityESP extends Module {
    public BedCityESP() {
        super("BedCityESP", 0, Category.RENDER, "i can see you", null);
    }

    private EntityPlayer target;

    private final List <BlockPos> clearSpots = new ArrayList<>();

    public boolean isInBlockRange(Entity target) {
        return target.getDistance(this.mc.player) <= 15.0F;
    }

    public boolean isValid(EntityPlayer entity) {
        return entity != null && this.isInBlockRange(entity) && entity.getHealth() > 0.0F && !entity.isDead;
    }

    public void onUpdate(){
        this.clearSpots.clear();
        if (!this.mc.player.isHandActive()) {
            if (!this.isValid(this.target) || this.target == null) {
                this.updateTarget();
            }

            List<Entity> entities = new ArrayList<>(this.mc.world.playerEntities);
            for (Object o : new ArrayList<>(entities)) {
                Entity e = (EntityPlayer) o;

                if (FriendManager.get().isFriend(e.getName().toLowerCase())){
                    entities.remove(e);
                }
            }
            Iterator<Entity> entityIter = entities.iterator();

            EntityPlayer player;
                do {

                    if (!entityIter.hasNext()) {
                        if (this.isValid(this.target) && this.mc.player.getDistance(this.target) < 160 && inHole(target)) {
                                this.showClearSpots(this.target);
                        }
                        return;
                    }
                    player = (EntityPlayer) entityIter.next();


                } while (player instanceof EntityPlayerSP || !this.isValid(player) || player.getDistance(this.mc.player) >= this.target.getDistance(this.mc.player));

                this.target = player;

        }
    }

    private boolean inHole(EntityPlayer player){
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        return isBlockBlastResistant(pos.west()) && isBlockBlastResistant(pos.east()) && isBlockBlastResistant(pos.north()) && isBlockBlastResistant(pos.south());
    }

    private boolean isBlockBlastResistant(BlockPos pos){
        return this.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    private void showClearSpots(EntityPlayer player) {



        List <BlockPos> poses = new ArrayList<>();

        BlockPos blockpos1 = new BlockPos(player.posX + 1, player.posY + 1.0D, player.posZ);
        poses.add(blockpos1);
        BlockPos blockpos2 = new BlockPos(player.posX - 1, player.posY + 1.0D, player.posZ);
        poses.add(blockpos2);
        BlockPos blockpos3 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ + 1);
        poses.add(blockpos3);
        BlockPos blockpos4 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ - 1);
        poses.add(blockpos4);
        BlockPos blockpos5 = new BlockPos(player.posX + 1, player.posY + 2.0D, player.posZ);
        poses.add(blockpos5);
        BlockPos blockpos6 = new BlockPos(player.posX - 1, player.posY + 2.0D, player.posZ);
        poses.add(blockpos6);
        BlockPos blockpos7 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ + 1);
        poses.add(blockpos7);
        BlockPos blockpos8 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ - 1);
        poses.add(blockpos8);



        for (Object o : new ArrayList<>(poses))
        {
            BlockPos b = (BlockPos) o;
            poses.add(0, b);
        }
        for (BlockPos b : poses)
        {
            if (this.mc.world.getBlockState(b).getMaterial().isReplaceable()){
                        clearSpots.add(b);
            }
        }
    }

    public void onRender() {

        for (BlockPos p : this.clearSpots)
        {
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(p), 1, 0, 0, 0.10F);
        }
    }

    public void updateTarget() {
        for (EntityPlayer player : this.mc.world.playerEntities) {
            if (!(player instanceof EntityPlayerSP) && this.isValid(player)) {
                this.target = player;
            }
        }
    }
}
