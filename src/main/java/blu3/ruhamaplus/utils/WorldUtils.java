package blu3.ruhamaplus.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class WorldUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static List<Block> emptyBlocks;
    public static List<Block> rightclickableBlocks;

    static {
        emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
        rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
    }

    public static void openBlock(BlockPos pos) {
        EnumFacing[] facings = EnumFacing.values();

        for (EnumFacing f : facings) {
            Block neighborBlock = mc.world.getBlockState(pos.offset(f)).getBlock();

            if (emptyBlocks.contains(neighborBlock)) {
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos, f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);

                return;
            }
        }
    }
    public static void openBlockOffhand(BlockPos pos) {
        EnumFacing[] facings = EnumFacing.values();

        for (EnumFacing f : facings) {
            Block neighborBlock = mc.world.getBlockState(pos.offset(f)).getBlock();

            if (emptyBlocks.contains(neighborBlock)) {
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos, f.getOpposite(), new Vec3d(pos), EnumHand.OFF_HAND);

                return;
            }
        }
    }

    public static long placeBlock(BlockPos pos, int slot, boolean rotate, boolean rotateBack) {
        if (isBlockEmpty(pos)) {
            if (slot != mc.player.inventory.currentItem) {
                mc.player.inventory.currentItem = slot;
            }

            EnumFacing[] facings = EnumFacing.values();

            for (EnumFacing f : facings) {
                Block neighborBlock = mc.world.getBlockState(pos.offset(f)).getBlock();
                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) f.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) f.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) f.getZOffset() * 0.5D);

                if (!emptyBlocks.contains(neighborBlock) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec) <= 4.25D) {
                    float[] rot = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};

                    if (rotate) {
                        rotatePacket(vec.x, vec.y, vec.z);
                    }

                    if (rightclickableBlocks.contains(neighborBlock)) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                    }

                    mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(f), f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                    if (rightclickableBlocks.contains(neighborBlock)) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
                    }

                    if (rotateBack) {
                        mc.player.connection.sendPacket(new Rotation(rot[0], rot[1], mc.player.onGround));
                    }

                    return 1;
                }
            }

        }
        return 0;
    }

    public static boolean isBlockEmpty(BlockPos pos) {
        if (emptyBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
            AxisAlignedBB box = new AxisAlignedBB(pos);
            Iterator entityIter = mc.world.loadedEntityList.iterator();

            Entity e;

            do {
                if (!entityIter.hasNext()) {
                    return true;
                }

                e = (Entity) entityIter.next();
            } while (!(e instanceof EntityLivingBase) || !box.intersects(e.getEntityBoundingBox()));

        }
        return false;
    }

    public static boolean canPlaceBlock(BlockPos pos) {
        if (isBlockEmpty(pos)) {
            EnumFacing[] facings = EnumFacing.values();

            for (EnumFacing f : facings) {
                if (!emptyBlocks.contains(mc.world.getBlockState(pos.offset(f)).getBlock()) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(new Vec3d(pos.getX() + 0.5D + (double) f.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) f.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) f.getZOffset() * 0.5D)) <= 4.25D) {
                    return true;
                }
            }

        }
        return false;
    }

    public static void rotateClient(double x, double y, double z) {
        double diffX = x - mc.player.posX;
        double diffY = y - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffZ = z - mc.player.posZ;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        mc.player.rotationYaw += MathHelper.wrapDegrees(yaw - mc.player.rotationYaw);
        mc.player.rotationPitch += MathHelper.wrapDegrees(pitch - mc.player.rotationPitch);
    }

    public static void rotatePacket(double x, double y, double z) {
        double diffX = x - mc.player.posX;
        double diffY = y - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffZ = z - mc.player.posZ;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        mc.player.connection.sendPacket(new Rotation(yaw, pitch, mc.player.onGround));
    }


    /**
     *
     *  rotates from a blockpos instead of the players position
     *
     * @param placeTarget placetarget
     * @param rotateTarget where to rotate to

     */
    public static void rotateBedPacket(BlockPos placeTarget, BlockPos rotateTarget) {
        double diffX = rotateTarget.getX() - placeTarget.getX();
        double diffY = rotateTarget.getY() - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffZ = rotateTarget.getZ() - placeTarget.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        mc.player.connection.sendPacket(new Rotation(yaw, pitch, mc.player.onGround));
    }

    public static BlockPos getNextSmartPos(BlockPos placeTarget){
        BlockPos plusXUp = new BlockPos(placeTarget.getX() + 1.0D, placeTarget.getY() + 1.0D, placeTarget.getZ());

        BlockPos plusZUp = new BlockPos(placeTarget.getX(), placeTarget.getY() + 1.0D, placeTarget.getZ() + 1.0D);

        BlockPos negXUp = new BlockPos(placeTarget.getX() - 1.0D, placeTarget.getY() + 1.0D, placeTarget.getZ());

        BlockPos negZUp = new BlockPos(placeTarget.getX(), placeTarget.getY() + 1.0D, placeTarget.getZ() - 1.0D);

        if (mc.world.getBlockState(plusXUp).getMaterial().isReplaceable()) {
            return plusXUp;
        }

        if (mc.world.getBlockState(plusZUp).getMaterial().isReplaceable()) {
            return plusZUp;
        }

        if (mc.world.getBlockState(negXUp).getMaterial().isReplaceable()) {
            return negXUp;
        }

        if (mc.world.getBlockState(negZUp).getMaterial().isReplaceable()) {
            return negZUp;
        }

        return null;
    }

    public static void smartBedRotation(BlockPos placeTarget){

        BlockPos plusX = new BlockPos(placeTarget.getX() + 1.0D, placeTarget.getY(), placeTarget.getZ());
        BlockPos plusXUp = new BlockPos(placeTarget.getX() + 1.0D, placeTarget.getY() + 1.0D, placeTarget.getZ());

        BlockPos plusZ = new BlockPos(placeTarget.getX(), placeTarget.getY(), placeTarget.getZ() + 1.0D);
        BlockPos plusZUp = new BlockPos(placeTarget.getX(), placeTarget.getY() + 1.0D, placeTarget.getZ() + 1.0D);

        BlockPos negX = new BlockPos(placeTarget.getX() - 1.0D, placeTarget.getY(), placeTarget.getZ());
        BlockPos negXUp = new BlockPos(placeTarget.getX() - 1.0D, placeTarget.getY() + 1.0D, placeTarget.getZ());

        BlockPos negZ = new BlockPos(placeTarget.getX(), placeTarget.getY(), placeTarget.getZ() - 1.0D);
        BlockPos negZUp = new BlockPos(placeTarget.getX(), placeTarget.getY() + 1.0D, placeTarget.getZ() - 1.0D);

        if (!(mc.world.getBlockState(plusX).getMaterial().isReplaceable())) {
            if (mc.world.getBlockState(plusXUp).getMaterial().isReplaceable()) {
                WorldUtils.rotateBedPacket(placeTarget, placeTarget.add(1,0,0));
                return;
            }
        }

        if (!(mc.world.getBlockState(plusZ).getMaterial().isReplaceable())) {
            if (mc.world.getBlockState(plusZUp).getMaterial().isReplaceable()) {
                WorldUtils.rotateBedPacket(placeTarget, placeTarget.add(0,0,1));
                return;
            }
        }

        if (!(mc.world.getBlockState(negX).getMaterial().isReplaceable())) {
            if (mc.world.getBlockState(negXUp).getMaterial().isReplaceable()) {
                WorldUtils.rotateBedPacket(placeTarget, placeTarget.add(-1,0,0));
                return;
            }
        }

        if (!(mc.world.getBlockState(negZ).getMaterial().isReplaceable())) {
            if (mc.world.getBlockState(negZUp).getMaterial().isReplaceable()) {
                WorldUtils.rotateBedPacket(placeTarget, placeTarget.add(0,0,-1));
                return;
            }
        }
    }

    public static void rotateBedPacket(BlockPos placeTarget, Entity rotateTarget) {
        double diffX = rotateTarget.posX - placeTarget.getX();
        double diffY = rotateTarget.posY - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffZ = rotateTarget.posZ - placeTarget.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        mc.player.connection.sendPacket(new Rotation(yaw, pitch, mc.player.onGround));
    }

    public static void facePos(double x, double z)
    {
        double diffX = x - mc.player.posX;
        double diffZ = z - mc.player.posZ;
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;

        mc.player.rotationYaw += MathHelper.wrapDegrees(yaw - mc.player.rotationYaw);
    }

    public static BlockPos getPlayerPos(final EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y)
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
