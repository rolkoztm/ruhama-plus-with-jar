package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.event.events.*;
import blu3.ruhamaplus.module.*;
import blu3.ruhamaplus.settings.*;
import blu3.ruhamaplus.utils.*;
import blu3.ruhamaplus.utils.friendutils.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.client.*;
import net.minecraft.client.audio.ElytraSound;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.eventhandler.*;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.*;

/**
 * * Tired of optimizing imports? Simply use `import *;` to instantly fix all your life's problems!
 **/

public class AutoCrystal extends Module {

    public static String
            breakPlace = "BreakPlace",
            placeBreak = "PlaceBreak",
            never = "Never",
            placeS = "Place",
            breakS = "Break",
            always = "Always";

    public static SettingMode
            logic = new SettingMode("Logic: ", breakPlace, placeBreak),
            rotate = new SettingMode("Rotate: ", never, placeS, breakS, always),
            breakHand = new SettingMode("BreakHand: ", "Mainhand", "Offhand", "BothHands"),
            placeMode = new SettingMode("PlaceMode: ", "1.12", "1.13+"),
            breakMode = new SettingMode("BreakMode: ", "All", "Smart");

    public static SettingSlider
            range = new SettingSlider(0.0D, 6.0D, 5.0D, 2, "Range: "),
            hitDelay = new SettingSlider(0.0D, 20.0D, 0, 0, "Hit Delay: "),
            placeDelay = new SettingSlider(0.0D, 20.0D, 0, 0, "Place Delay: "),
            minDmgS = new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MinDMG: "),
            maxSelfDmg = new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MaxSelfDMG: "),
            facePlaceHp = new SettingSlider(1.0D, 36.0D, 10.0D, 0, "FacePlace HP: ");

    public static SettingToggle
            multiplace = new SettingToggle(false, "Multiplace"),
            renderPlayer = new SettingToggle(false, "RenderTarget"),
            autoSwitch = new SettingToggle(true, "AutoSwitch"),
            chatAlert = new SettingToggle(true, "Chat Alert"),
            speedi = new SettingToggle(true, "Fast"),
            damageText = new SettingToggle(false, "DamageText"),
            place = new SettingToggle(true, "Place"),
            explode = new SettingToggle(true, "Explode");

    private static final List<SettingBase> settings = Arrays.asList(
            logic, rotate, breakHand, placeMode, breakMode, range, hitDelay, placeDelay, minDmgS, maxSelfDmg, facePlaceHp, multiplace, renderPlayer, autoSwitch, chatAlert, speedi, damageText, place, explode
    );
    private final TimeUtils placeTimer, breakTimer;
    private double renderDamage = 0;

    private boolean isSpoofingAngles, togglePitch, switchCooldown = false;

    private BlockPos renderTarget = null;

    private final List<EntityPlayer> ezplayers = new ArrayList<>(), targetPlayers = new ArrayList<>();

    public AutoCrystal() {
        super("blu3CA", 0, Category.EXPERIMENTAL, "absolute shit i say", settings);
        placeTimer = new TimeUtils();
        breakTimer = new TimeUtils();
    }

    @Override
    public void onDisable() {
        if (getBoolean("Chat Alert")) ChatUtils.log("blu3CA: " + ChatFormatting.RED + "OFF");
        renderTarget = null;
        ezplayers.clear();
        targetPlayers.clear();
        resetRotation();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (getBoolean("Chat Alert")) ChatUtils.log("blu3CA: " + ChatFormatting.AQUA + "ON");
        super.onEnable();
    }


    public void onUpdate() {
        if (nullCheck()) return;
        doStuff();
    }

    public void fastUpdate() {
        if (isSpoofingAngles) { // constantly sending packets so rotation spoofing works properly.
            if (togglePitch) {
                mc.player.rotationPitch += (float) 4.0E-4;
                togglePitch = false;
            } else {
                mc.player.rotationPitch -= (float) 4.0E-4;
                togglePitch = true;
            }
        }
    }

    public void doStuff() {
        if (logic.is(breakPlace)) {
            if (breakTimer.passedDms(getSlider("Hit Delay: ") * 5)) {
                if (explode.state) breakCrystal();
                breakTimer.reset();
            }
            if (placeTimer.passedDms(getSlider("Place Delay: ") * 5)) {
                if (place.state) placeCrystal();
                placeTimer.reset();
            }
        } else if (logic.is(placeBreak)) {
            if (placeTimer.passedDms(getSlider("Place Delay: ") * 5)) {
                if (place.state) placeCrystal();
                placeTimer.reset();
            }
            if (breakTimer.passedDms(getSlider("Hit Delay: ") * 5)) {
                if (explode.state) breakCrystal();
                breakTimer.reset();
            }
        }
    }

    private void placeCrystal() {
        ezplayers.clear();
        int crystalSlot;
        crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        int minDmg;
        minDmg = (int) minDmgS.getValue();
        renderTarget = null;
        List<BlockPos> blocks = findCrystalBlocks();
        ezplayers.addAll(mc.world.playerEntities);
        ezplayers.remove(mc.player);
        ezplayers.removeIf(o -> FriendManager.get().isFriend(o.getName().toLowerCase()) || o.getHealth() <= 0); // god im happy i found this
        BlockPos placeTarget = null;
        float highDamage = 0.5f;
        for (BlockPos pos : blocks) {
            final float selfDmg = DamageUtil.calculateDamage(pos, mc.player);
            if (selfDmg + 0.5D >= mc.player.getHealth() + mc.player.getAbsorptionAmount() || selfDmg > maxSelfDmg.getValue())
                continue;
            for (EntityPlayer player : ezplayers) {
                if (player.getHealth() <= facePlaceHp.getValue()) minDmg = 2;
                targetPlayers.remove(player);
                if (player.getDistanceSq(pos) < 169) {
                    if (!targetPlayers.contains(player)) targetPlayers.add(player);
                    float damage = DamageUtil.calculateDamage(pos, player);
                    if (damage <= selfDmg) continue;
                    if (damage < minDmg || damage <= highDamage) continue;
                    renderDamage = damage;
                    placeTarget = pos;
                    renderTarget = pos;
                }
            }
            break;
        }
        if (placeTarget != null) {
            if (getBoolean("AutoSwitch") && mc.player.inventory.currentItem != crystalSlot && !eatingGap() && !offhand) {
                mc.player.inventory.currentItem = crystalSlot;
                switchCooldown = true;
                return;
            }
            if (switchCooldown) {
                switchCooldown = false;
                return;
            }
            if (getMode("Rotate: ") == 1 || getMode("Rotate: ") == 3)
                lookAtPacket(placeTarget.getX() + 0.5, placeTarget.getY() - 0.5, placeTarget.getZ() + 0.5, mc.player);
            mc.playerController.processRightClickBlock(mc.player, mc.world, placeTarget, EnumFacing.UP, new Vec3d(placeTarget), offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        } else resetRotation();
    }

    private void breakCrystal() {
        List<EntityEnderCrystal> crystals = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityEnderCrystal) {
                crystals.add((EntityEnderCrystal) e);
            }
        }
        List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
        players.remove(mc.player);
        for (EntityPlayer o : new ArrayList<>(players)) {
            if (FriendManager.get().isFriend(o.getName().toLowerCase())) {
                players.remove(o);
            }
            if (o.getHealth() <= 0) {
                players.remove(o);
            }
        }
        switch (getMode("BreakMode: ")) {
            case 0: {
                EntityEnderCrystal crystal = mc.world.loadedEntityList.stream().filter((entityx) -> entityx instanceof EntityEnderCrystal).map((entityx) -> (EntityEnderCrystal) entityx).min(Comparator.comparing((c) -> mc.player.getDistance(c))).orElse(null);
                if (crystal != null && (double) mc.player.getDistance(crystal) <= getSlider("Range: ")) {
                    if (getMode("Rotate: ") == 2 || getMode("Rotate: ") == 3)
                        lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
                    mc.playerController.attackEntity(mc.player, crystal);
                    swing();
                } else {
                    resetRotation();
                }
                break;
            }
            case 1: {
                int minDmg;
                float damage = 0.5f;
                minDmg = (int) minDmgS.getValue();
                for (EntityPlayer player : players) {
                    for (EntityEnderCrystal crystal : crystals) {
                        if (crystal.isDead) continue;
                        if (player.getDistance(mc.player) >= 12) continue;


                        damage = DamageUtil.calculateDamage(crystal, player);
                        final float selfDmg = DamageUtil.calculateDamage(crystal, mc.player);
                        if (player.getHealth() <= getSlider("FacePlace HP: ")) {
                            minDmg = 2;
                        }
                        if (selfDmg + 0.5D >= mc.player.getHealth() || selfDmg > maxSelfDmg.getValue()) {
                            continue;
                        }
                        if (damage >= minDmg && (double) mc.player.getDistance(crystal) <= getSlider("Range: ")) {
                            if (getMode("Rotate: ") == 2 || getMode("Rotate: ") == 3)
                                lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
                            mc.playerController.attackEntity(mc.player, crystal);
                            swing();
                        }
                    }
                    if (damage == .5) {
                        resetRotation();
                    }
                }
                break;
            }
        }
    }

    public void swing() {
        switch (getMode("BreakHand: ")) {
            case 0: {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            }
            case 1: {
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            }
            case 2: {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            }
        }
    }

    public static double square(final double input) {
        return input * input;
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(getPlayerPos(), (float) getSlider("Range: "), (int) getSlider("Range: "), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));

        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z) {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    private boolean eatingGap() {
        return mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && mc.player.isHandActive();
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private boolean getIsEmpty(boolean multiPlace, BlockPos pos) {
        if (multiPlace) {
            return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty();
        } else
            return mc.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos)).isEmpty()
                    && mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos)).isEmpty()
                    && mc.world.getEntitiesWithinAABB(EntityPigZombie.class, new AxisAlignedBB(pos)).isEmpty();
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        if (getMode("PlaceMode: ") == 0) {
            BlockPos boost = blockPos.add(0, 1, 0);
            BlockPos boost2 = blockPos.add(0, 2, 0);
            return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK
                    || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                    && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                    && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR
                    && getIsEmpty(getBoolean("MultiPlace"), boost);
        } else {
            BlockPos boost = blockPos.add(0, 1, 0);
            return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK
                    || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                    && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                    && getIsEmpty(getBoolean("MultiPlace"), boost);
        }
    }

    public void onRender() {

        if (renderTarget != null) {
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(renderTarget), 0, 1, 1, 0.3F);
            if (getBoolean("DamageText"))
                RenderUtils.drawText(renderTarget, ((Math.floor(renderDamage) == renderDamage) ? Integer.valueOf((int) renderDamage) : String.format("%.1f", renderDamage)) + "");
        }

        if (getBoolean("RenderTarget")) {
            for (EntityPlayer e : targetPlayers)
                RenderUtils.drawBlockBox(e.getEntityBoundingBox(), 0.0F, 1.0F, 1.0F, 0.3F);
        }
    }

    @SubscribeEvent
    public void removeExploded(EventReadPacket event) {
        if (event.getPacket() instanceof SPacketSoundEffect && speedi.getValue()) {
            final SPacketSoundEffect explosion = (SPacketSoundEffect) event.getPacket();
            if (explosion.getCategory() == SoundCategory.BLOCKS && explosion.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void spoofRotations(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) event.getPacket()).yaw = (float) yaw;
                ((CPacketPlayer) event.getPacket()).pitch = (float) pitch; //this is sexy
            }
        }
    }

    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    private static double yaw, pitch;

    private void setYawAndPitch(final float yaw1, final float pitch1) {
        isSpoofingAngles = true;
        yaw = yaw1;
        pitch = pitch1;
    }

    private void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    public double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[]{yaw, pitch};
    }

}