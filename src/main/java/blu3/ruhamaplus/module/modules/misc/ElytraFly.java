package blu3.ruhamaplus.module.modules.misc;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class ElytraFly extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingMode("Mode: ", "Flat", "Boost"), new SettingSlider(0.1D, 4.0D, 1.35D, 2, "Boost Max: "), new SettingToggle(true, "Accelerate"), new SettingSlider(1.0E-5D, 5.0E-4D, 2.0E-4D, 5, "Glide: "), new SettingSlider(0.01D, 10.0D, 1.0D, 2, "Flat Speed: "));

    public ElytraFly()
    {
        super("ElytraFly", 0, Category.MISC, "Elytra booster", settings);
    }

    public void onDisable()
    {
        ChatUtils.log("Elytrafly Disabled");
        this.mc.player.capabilities.isFlying = false;
        this.mc.player.capabilities.setFlySpeed(0.05F);

        if (!this.mc.player.capabilities.isCreativeMode)
        {
            this.mc.player.capabilities.allowFlying = false;
        }
    }

    public void onEnable() {
        ChatUtils.log("Elytrafly Enabled");
    }

    public void onUpdate()
    {
        if (this.getSetting(0).asMode().mode == 0)
        {
            if (this.mc.player.capabilities.isFlying)
            {
                this.mc.player.setVelocity(0.0D, 0.0D, 0.0D);
                this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY - this.getSetting(3).asSlider().getValue(), this.mc.player.posZ);
                this.mc.player.capabilities.setFlySpeed((float) this.getSetting(4).asSlider().getValue());
                this.mc.player.setSprinting(false);
            }

            if (this.mc.player.onGround && !this.mc.player.capabilities.isCreativeMode)
            {
                this.mc.player.capabilities.allowFlying = false;
            }

            if (this.mc.player.isElytraFlying())
            {
                this.mc.player.capabilities.setFlySpeed(0.915F);
                this.mc.player.capabilities.isFlying = true;

                if (!this.mc.player.capabilities.isCreativeMode)
                {
                    this.mc.player.capabilities.allowFlying = true;
                }
            }
        } else if (this.getSetting(0).asMode().mode == 1)
        {
            if (this.mc.player.isElytraFlying() && this.mc.world.getBlockState(this.mc.player.getPosition().add(0.0D, -0.1D, 0.0D)).getMaterial() instanceof MaterialLiquid && this.mc.world.getBlockState(this.mc.player.getPosition().add(0, 1, 0)).getBlock() == Blocks.AIR && this.mc.player.motionY > 0.0D)
            {
                this.mc.player.addVelocity(0.0D, 0.05D, 0.0D);
            }

            if (!this.mc.player.isElytraFlying() || this.mc.player.motionY > -0.09D)
            {
                return;
            }

            double speed;

            // hmmmmmm
            for (speed = Math.abs(this.mc.player.motionX) + Math.abs(this.mc.player.motionY) + Math.abs(this.mc.player.motionZ); speed > this.getSetting(1).asSlider().getValue(); speed = Math.abs(this.mc.player.motionX) + Math.abs(this.mc.player.motionY) + Math.abs(this.mc.player.motionZ))
            {
                EntityPlayerSP player = this.mc.player;

                player.motionX *= 0.95D;
                player = this.mc.player;
                player.motionY *= 0.95D;
                player = this.mc.player;
                player.motionZ *= 0.95D;
            }

            Vec3d vec3d = (new Vec3d(0.0D, 0.0D, 0.23D)).rotatePitch(-((float) Math.toRadians(this.mc.player.rotationPitch))).rotateYaw(-((float) Math.toRadians(this.mc.player.rotationYaw)));

            if (this.getSetting(2).asToggle().state && MathHelper.clamp(speed / 2.0D, 0.0D, this.getSetting(1).asSlider().getValue() - 0.25D) < 0.23D)
            {
                vec3d = vec3d.scale(0.2D);
            }

            this.mc.player.addVelocity(vec3d.x, vec3d.y, vec3d.z);
        }
    }
}
