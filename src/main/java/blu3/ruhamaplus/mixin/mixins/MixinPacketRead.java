package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.event.events.EventReadPacket;
import blu3.ruhamaplus.module.ModuleManager;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin({NetworkManager.class})
public class MixinPacketRead
{
    @Inject(
            method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo info)
    {
        if (ModuleManager.onPacketRead(p_channelRead0_2_))
        {
            info.cancel();
        }
        EventReadPacket event = new EventReadPacket(p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) info.cancel();
    }
}
