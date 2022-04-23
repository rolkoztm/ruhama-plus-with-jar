package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.event.events.EventPlayerDamageBlock;
import blu3.ruhamaplus.event.events.ReachEvent;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.player.PacketMine;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scala.collection.parallel.ParIterableLike;

import java.util.Objects;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> p_Info)
    {
        EventPlayerDamageBlock event = new EventPlayerDamageBlock(posBlock, directionFacing);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isCanceled()) {
            p_Info.setReturnValue(false);
            p_Info.cancel();
        }
    }

    @Inject(
            method = "getBlockReachDistance",
            at = @At("HEAD"),
            cancellable = true)
    public void getBlockReachDistance(CallbackInfoReturnable<Float> ci) {
        ReachEvent event = new ReachEvent();
        MinecraftForge.EVENT_BUS.post(event);
        if(event.reachDistance > 0.0f) {
            ci.setReturnValue(event.reachDistance);
            ci.cancel();
        }
    }
}
