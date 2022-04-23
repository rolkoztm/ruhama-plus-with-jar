package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

@Mixin({NettyCompressionDecoder.class})
public class MixinNettyDecoder
{
    @Final
    @Shadow
    private Inflater inflater;

    @Inject(
            method = {"decode(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_, CallbackInfo info) throws Exception
    {
        Module m = ModuleManager.getModuleByName("AntiChunkBan");

        if (Objects.requireNonNull(m).isToggled() && m.getSetting(0).asMode().mode == 0)
        {
            info.cancel();

            if (p_decode_2_.readableBytes() != 0)
            {
                PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
                int l = packetbuffer.readVarInt();

                if (l == 0)
                {
                    p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
                } else
                {
                    byte[] abyte = new byte[packetbuffer.readableBytes()];
                    packetbuffer.readBytes(abyte);
                    this.inflater.setInput(abyte);

                    byte[] abyte1 = new byte[l];
                    this.inflater.inflate(abyte1);
                    p_decode_3_.add(Unpooled.wrappedBuffer(abyte1));

                    this.inflater.reset();
                }
            }
        }
    }
}
