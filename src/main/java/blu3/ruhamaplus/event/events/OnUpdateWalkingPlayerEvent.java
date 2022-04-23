package blu3.ruhamaplus.event.events;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnUpdateWalkingPlayerEvent extends Event {
    public boolean moving;
    public boolean rotating;
    public boolean sprinting;
    public boolean sneaking;
    public boolean onGround;
    public Vec3d pos;
    public Vec2f rotation;

    public OnUpdateWalkingPlayerEvent(boolean moving, boolean rotating, boolean sprinting, boolean sneaking, boolean onGround, Vec3d pos, Vec2f rotation) {
        this.moving = moving;
        this.rotating = rotating;
        this.sprinting = sprinting;
        this.sneaking = sneaking;
        this.onGround = onGround;
        this.pos = pos;
        this.rotation = rotation;
    }


    public boolean getMoving() {
        return moving;
    }

    public boolean getRotating() {
        return rotating;
    }

    public boolean getSprinting() {
        return sprinting;
    }

    public boolean getSneaking() {
        return sneaking;
    }

    public boolean getOnGround() {
        return onGround;
    }

    public Vec3d getPos() {
        return pos;
    }

    public Vec2f getRotation() {
        return rotation;
    }

}
