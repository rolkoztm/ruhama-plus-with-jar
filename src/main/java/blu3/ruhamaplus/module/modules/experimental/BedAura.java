package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ChatUtils;
import blu3.ruhamaplus.utils.DamageUtil;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.WorldUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBed;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BedAura extends Module {

    private boolean switchCooldown = false;
    private BlockPos renderTarget = null;
    private final List<EntityPlayer> ezplayers = new ArrayList<>();
    private final List<EntityPlayer> targetPlayers = new ArrayList<>();
    private EntityPlayer targetPlayer = null;

    public static SettingSlider range = new SettingSlider(0.0D, 6.0D, 5.0D, 0, "Range: ");
    public static SettingSlider placeDelay = new SettingSlider(0.0D, 20.0D, 3.0D, 0, "Place Delay: ");
    public static SettingSlider minDmg = new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MinDMG: ");
    public static SettingSlider maxSelfDmg = new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MaxSelfDMG: ");
    public static SettingToggle autoSwitch = new SettingToggle(true, "AutoSwitch");

    private static final List<SettingBase> settings = Arrays.asList(
            range,
            placeDelay,
            minDmg,
            maxSelfDmg,
            autoSwitch
    );

    public BedAura() { super("blu3BA", 0, Category.EXPERIMENTAL, "absolute shit i say part 2", settings); }

    public void onDisable(){
        ChatUtils.log("blu3BA: " + ChatFormatting.RED + "OFF");
        ezplayers.clear();
        targetPlayers.clear();
        renderTarget = null;
        targetPlayer = null;
    }
    public void onEnable(){
        ChatUtils.log("blu3BA: " + ChatFormatting.AQUA + "ON");
    }

    public void onUpdate() {
        if (this.mc.player.ticksExisted % placeDelay.getValue() == 0) {
            this.placeBed();
            this.breakBed();
        }
    }

    private void placeBed(){
        ezplayers.clear();
        int bedSlot;
        bedSlot = this.mc.player.getHeldItemMainhand().getItem() == Items.BED ? this.mc.player.inventory.currentItem : -1;
        if (bedSlot == -1)
        {
            for (int l = 0; l < 9; ++l)
            {
                if (this.mc.player.inventory.getStackInSlot(l).getItem() == Items.BED)
                {
                    bedSlot = l;
                    break;
                }
            }
        }
        if (bedSlot == -1) { return; }
        int leastDmg;
        leastDmg = (int)minDmg.getValue();

        List<BlockPos> blocks = this.findBedBlocks();
        ezplayers.addAll(this.mc.world.playerEntities);
        ezplayers.remove(this.mc.player);
        for (Object o : new ArrayList<>(ezplayers)) {
            Entity e = (EntityPlayer) o;
            if (FriendManager.get().isFriend(e.getName().toLowerCase())){
                ezplayers.remove(e);
            }
        }

        BlockPos placeTarget = null;
        float highDamage = 0.5f;
        for (BlockPos pos : blocks) {
            final float selfDmg = DamageUtil.calculateDamage(pos, this.mc.player);
            if (selfDmg + 0.5D >= this.mc.player.getHealth() || selfDmg > maxSelfDmg.getValue()) {
                continue;
            }
            for (EntityPlayer player : ezplayers) {
                targetPlayers.remove(player);
                if (player.getDistanceSq(pos) < square(range.getValue())){
                    if (!targetPlayers.contains(player)) targetPlayers.add(player);
                    float damage = DamageUtil.calculateDamage(pos, player);
                    if (damage <= selfDmg) continue;
                    if (damage <= leastDmg || damage <= highDamage) continue;
                    placeTarget = pos;
                    renderTarget = pos.up();
                    targetPlayer = player;
                    break;
                }
            }
            break;
        }
        if (placeTarget != null){
            if (autoSwitch.state && this.mc.player.inventory.currentItem != bedSlot && !eatingGap()) {
                this.mc.player.inventory.currentItem = bedSlot;
                this.switchCooldown = true;
                return;
            }
            if (this.switchCooldown)
            {
                this.switchCooldown = false;
                return;
            }
            if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBed) {
                WorldUtils.rotateBedPacket(placeTarget, targetPlayer);
                this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.up(), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
            }
        }
    }

    public void breakBed(){
        for (BlockPos pos : findBeds()){
            WorldUtils.openBlock(pos);
            break;
        }
    }

    public void onRender() {
        if (this.renderTarget != null){
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(renderTarget), 0, 1, 1, 0.3F);
        }
    }

    private List<BlockPos> findBedBlocks()
    {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(this.getSphere(this.getPlayerPos(), (float) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::canPlaceBed).collect(Collectors.toList()));

        return positions;
    }

    private List<BlockPos> findBeds()
    {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(this.getSphere(this.getPlayerPos(), (float) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::blockIsBed).collect(Collectors.toList()));

        return positions;
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
    private boolean eatingGap(){
        return mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && mc.player.isHandActive();
    }
    public static double square(final double input) {
        return input * input;
    }
    public BlockPos getPlayerPos()
    {
        return new BlockPos(Math.floor(this.mc.player.posX), Math.floor(this.mc.player.posY), Math.floor(this.mc.player.posZ));
    }
    private boolean canPlaceBed(BlockPos blockPos){
        BlockPos boost = blockPos.add(0, 1, 0);
        return (this.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && WorldUtils.getNextSmartPos(boost.down()) != null && mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(boost)).isEmpty());
    }
    private boolean blockIsBed(BlockPos blockPos){
        return (this.mc.world.getBlockState(blockPos).getBlock() == Blocks.BED);
    }
}