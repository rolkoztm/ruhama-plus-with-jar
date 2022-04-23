package blu3.ruhamaplus.module.modules.misc;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.List;


public class Strafe extends Module {
    public Strafe() { super("Strafe", Keyboard.KEY_NONE, Category.MISC, "speedi", settings); }

    private static final List<SettingBase> settings = Collections.singletonList(new SettingToggle(true, "Jump"));

    int waitCounter;
    double forward = 1;

    public void onUpdate() {
        if(mc.player == null || mc.world == null) return;
        boolean boost = Math.abs(mc.player.rotationYawHead - mc.player.rotationYaw) < 90;

        if(mc.player.moveForward != 0 || mc.player.moveStrafing != 0) mc.player.setSprinting(true);

        if(mc.player.moveForward != 0) {
            float yaw = mc.player.rotationYaw;
            if(mc.player.moveForward > 0) {
                if(mc.player.movementInput.moveStrafe != 0) {
                    yaw += (mc.player.movementInput.moveStrafe > 0) ? -45 : 45;
                }
                forward = 1.1;
                mc.player.moveForward = 1.1f;
                mc.player.moveStrafing = 0;
            }else if(mc.player.moveForward < 0) {
                if(mc.player.movementInput.moveStrafe != 0) {
                    yaw += (mc.player.movementInput.moveStrafe > 0) ? 45 : -45;
                }
                forward = -1.1;
                mc.player.moveForward = -1.1f;
                mc.player.moveStrafing = 0;
            }
            if (mc.player.onGround) {
                mc.player.setJumping(false);
                if (waitCounter < 1) {
                    waitCounter++;
                    return;
                } else {
                    waitCounter = 0;
                }
                float f = (float)Math.toRadians(yaw);
                if(this.getBoolean("jump")) {
                    mc.player.motionY = 0.405;
                    mc.player.motionX -= (double) (MathHelper.sin(f) * 0.055f) * forward;
                    mc.player.motionZ += (double) (MathHelper.cos(f) * 0.055f) * forward;
                } else {
                    if(mc.gameSettings.keyBindJump.isPressed() && !this.getBoolean("jump")){
                        mc.player.motionY = 0.405;
                        mc.player.motionX -= (double) (MathHelper.sin(f) * 0.055f) * forward;
                        mc.player.motionZ += (double) (MathHelper.cos(f) * 0.055f) * forward;
                    }
                }
            } else {
                if (waitCounter < 1) {
                    waitCounter++;
                    return;
                } else {
                    waitCounter = 0;
                }
                double currentSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
                double speed = boost ? 1.0064 : 1.001;
                if(mc.player.motionY < 0) speed = 1;

                double direction = Math.toRadians(yaw);
                mc.player.motionX = (-Math.sin(direction) * speed * currentSpeed) * forward;
                mc.player.motionZ = (Math.cos(direction) * speed * currentSpeed) * forward;
            }
        }
    }


//mc.player.motionY = 0.405f;
}
