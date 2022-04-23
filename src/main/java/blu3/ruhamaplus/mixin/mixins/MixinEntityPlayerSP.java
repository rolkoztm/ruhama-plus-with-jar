/*
package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.event.events.OnUpdateWalkingPlayerEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityPlayerSP.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends EntityPlayer {
    public MixinEntityPlayerSP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Shadow
    private boolean serverSprintState;
    @Shadow @Final public NetHandlerPlayClient connection;
    @Shadow private boolean serverSneakState;
    @Shadow protected abstract boolean isCurrentViewEntity();
    @Shadow private double lastReportedPosX;
    @Shadow private double lastReportedPosY;
    @Shadow private double lastReportedPosZ;
    @Shadow private float lastReportedYaw;
    @Shadow private float lastReportedPitch;
    @Shadow private int positionUpdateTicks;
    @Shadow private boolean prevOnGround;
    @Shadow private boolean autoJumpEnabled;
    @Shadow protected Minecraft mc;

    @Shadow public abstract boolean isSneaking();

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V"))
    private void onUpdateWalkingPlayer(EntityPlayerSP player) {

        // Setup flags
        ++this.positionUpdateTicks;
        boolean moving = isMoving();
        boolean rotating = isRotating();
        boolean sprinting = this.isSprinting();
        boolean sneaking = this.isSneaking();
        boolean onGround = this.onGround;
        Vec3d pos = new Vec3d(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        Vec2f rotation = new Vec2f(rotationYaw, rotationPitch);

        OnUpdateWalkingPlayerEvent event = new OnUpdateWalkingPlayerEvent(moving, rotating, sprinting, sneaking, onGround, pos, rotation);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            return;
        }

        // Copy flags from event
        moving = event.getMoving();
        rotating = event.getRotating();
        sprinting = event.getSprinting();
        sneaking = event.getSneaking();
        onGround = event.getOnGround();
        pos = event.getPos();
        rotation = event.getRotation();

        // Sprinting Packet
        if (sprinting != this.serverSprintState) {
            if (sprinting) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = sprinting;
        }

        // Sneaking Packet
        if (sneaking != this.serverSneakState) {
            if (sneaking) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = sneaking;
        }

        // Position & Rotation Packet
        if (this.isCurrentViewEntity()) {

            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.motionX, -999.0D, this.motionZ, rotation.x, rotation.y, onGround));
                moving = false;
            } else if (moving && rotating) {
                this.connection.sendPacket(new CPacketPlayer.PositionRotation(pos.x, pos.y, pos.z, rotation.x, rotation.y, onGround));
            } else if (moving) {
                this.connection.sendPacket(new CPacketPlayer.Position(pos.x, pos.y, pos.z, onGround));
            } else if (rotating) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(rotation.x, rotation.y, onGround));
            } else if (this.prevOnGround != onGround) {
                this.connection.sendPacket(new CPacketPlayer(onGround));
            }

            if (moving) {
                this.lastReportedPosX = pos.x;
                this.lastReportedPosY = pos.y;
                this.lastReportedPosZ = pos.z;
                this.positionUpdateTicks = 0;
            }

            if (rotating) {
                this.lastReportedYaw = rotation.x;
                this.lastReportedPitch = rotation.y;
            }

            this.prevOnGround = onGround;
            this.autoJumpEnabled = this.mc.gameSettings.autoJump;

        }
    }


    private boolean isMoving() {
        double xDiff = this.posX - this.lastReportedPosX;
        double yDiff = this.getEntityBoundingBox().minY - this.lastReportedPosY;
        double zDiff = this.posZ - this.lastReportedPosZ;

        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff > 9.0E-4D || this.positionUpdateTicks >= 20;
    }

    private boolean isRotating() {
        double yawDiff = this.rotationYaw - this.lastReportedYaw;
        double pitchDiff = this.rotationPitch - this.lastReportedPitch;

        return yawDiff != 0.0D || pitchDiff != 0.0D;
    }

}*/
