package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;

public class FeetXp extends Module {
    public FeetXp() { super ("FootXP", Keyboard.KEY_NONE, Category.PLAYER, "make xp go down honhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhon", null); }

    private boolean isSpoofingAngles;
    private boolean togglePitch;

    public boolean onPacketSend(Packet<?> packet) {
        if (packet instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
            }
        }
        return false;
    }

    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle){
            lookAtPacket(mc.player.posX, mc.player.posY - 2, mc.player.posZ, mc.player);
        } else {
            resetRotation();
        }
        if (isSpoofingAngles) {
            if (togglePitch) {
                mc.player.rotationPitch += (float) 4.0E-4;
                togglePitch = false;
            } else {
                mc.player.rotationPitch -= (float) 4.0E-4;
                togglePitch = true;
            }
        }
    }

    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    private static double yaw;
    private static double pitch;

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