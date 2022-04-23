package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.event.events.EventEntityAdded;
import blu3.ruhamaplus.event.events.EventEntityRemoved;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, priority = 2147483647)
public class MixinWorld {

    @Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity eventPacket, CallbackInfo info)
    {
        EventEntityRemoved event = new EventEntityRemoved(eventPacket);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "onEntityAdded", at = @At("HEAD"), cancellable = true)
    public void onEntityAdded(Entity eventPacket, CallbackInfo info)
    {
        EventEntityAdded event = new EventEntityAdded(eventPacket);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) info.cancel();
    }

}