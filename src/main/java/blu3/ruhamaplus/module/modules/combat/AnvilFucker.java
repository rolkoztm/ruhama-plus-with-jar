package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.TimeUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;


import java.util.Arrays;
import java.util.List;

/**
 * *  @author blu3
 **/

public class AnvilFucker extends Module {

    public AnvilFucker() {
        super("AnvilFucker", 0, Category.COMBAT, "traps players in holes and packetmines their anvil", settings);
        this.manualTimer = new TimeUtils();
    }

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "Trap"),
            new SettingSlider(0, 7, 5, 0, "Range: ")
    );
    private BlockPos mining = null;
    private final TimeUtils manualTimer;
    public String m;

    private boolean hit = false;
    private boolean switchCooldown = false;

    public void onEnable() {
        hit = false;
    }

    public void onUpdate() {
        if (mc.player == null) {
            return;
        }
        this.findClosestTarget();
        this.doPacketMineShit();
        if (closestTarget != null) {
            BlockPos blockpos1 = new BlockPos(closestTarget.posX, closestTarget.posY + 2.0D, closestTarget.posZ);
            BlockPos checkPos = new BlockPos(closestTarget.posX, closestTarget.posY, closestTarget.posZ);

            if (!(this.mc.world.getBlockState(checkPos).getBlock() == Blocks.ANVIL)) {
                hit = false;
            }

            int obbySlot;
            obbySlot = this.mc.player.getHeldItemMainhand().getItem() == Item.getItemById(49) ? this.mc.player.inventory.currentItem : -1;
            if (obbySlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (this.mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemById(49)) {
                        obbySlot = l;
                        break;
                    }
                }
            }
            if (obbySlot == -1) {
                return;
            }
            if (mc.player.getDistance(closestTarget) > this.getSlider("Range: ")) {
                return;
            }

            if (inHole(closestTarget)) {
                if (this.mc.world.getBlockState(blockpos1).getMaterial().isReplaceable()) {

                    if (this.getBoolean("Trap") && this.mc.player.inventory.currentItem != obbySlot) {

                        if (!doesHotbarHaveObby()) return;

                        this.mc.player.inventory.currentItem = obbySlot;
                        this.switchCooldown = true;
                        return;
                    }
                    if (this.switchCooldown) {
                        this.switchCooldown = false;
                        return;
                    }
                    if ((this.getBoolean("Trap") && this.mc.player.getHeldItemMainhand().getItem() == Item.getItemById(49) && this.mc.world.getBlockState(blockpos1).getMaterial().isReplaceable()))
                        this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockpos1, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                    if (this.mc.world.getBlockState(checkPos).getBlock() == Blocks.ANVIL) {
                        if (!hit) {
                            //this.mc.player.inventory.currentItem = InventoryUtil.findHotbarItem(ItemPickaxe.class);
                            this.mc.player.swingArm(EnumHand.MAIN_HAND);
                            if (mining == null) this.mining = checkPos;
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, checkPos, EnumFacing.DOWN));
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, checkPos, EnumFacing.DOWN));
                            hit = true;
                        }
                    }
                }
            }
        }
    }

    public void doPacketMineShit() {
        if (mining == null) {
            return;
        }
        if (this.mc.world.getBlockState(mining).getBlock() == Blocks.AIR) {
            mining = null;
        }

        if (manualTimer.passedMs(500)) {
            m = "Mining";
        }
        if (manualTimer.passedMs(1000)) {
            m = "Mining.";
        }
        if (manualTimer.passedMs(1500)) {
            m = "Mining..";
        }
        if (manualTimer.passedMs(2000)) {
            m = "Mining...";
            manualTimer.reset();
        }
    }

    private boolean inHole(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        return isBlockBlastResistant(pos.west()) && isBlockBlastResistant(pos.east()) && isBlockBlastResistant(pos.north()) && isBlockBlastResistant(pos.south());
    }

    private boolean isBlockBlastResistant(BlockPos pos) {
        return this.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }


    private EntityPlayer closestTarget;

    private void findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;
        closestTarget = null;
        if (playerList.isEmpty()) return;
        for (EntityPlayer target : playerList) {
            if (target == mc.player) {
                continue;
            }
            if (FriendManager.get().isFriend(target.getName().toLowerCase())) {
                continue;
            }
            if (!isLiving(target)) {
                continue;
            }
            if ((target).getHealth() <= 0) {
                continue;
            }
            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }
            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }
        }
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }

    private boolean isStackObby(ItemStack stack) {
        return stack != null && stack.getItem() == Item.getItemById(49);
    }

    private boolean doesHotbarHaveObby() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = this.mc.player.inventoryContainer.getSlot(i).getStack();

            if (this.isStackObby(stack)) {
                return true;
            }
        }

        return false;
    }
}