package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.event.events.EventSendPacket;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.combat.Criticals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin({NetHandlerPlayClient.class})
public class MixinPacketSend {

    @Inject(
            method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void sendPacket(Packet<?> packetIn, CallbackInfo info) {
        if (ModuleManager.onPacketSend(packetIn)) {
            info.cancel();
        }

        EventSendPacket event = new EventSendPacket(packetIn);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) info.cancel();

        if (packetIn instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) packetIn;

            if (Minecraft.getMinecraft().player.getHeldItem(packet.getHand()).getItem() instanceof ItemShulkerBox || Minecraft.getMinecraft().player.getHeldItem(packet.getHand()).getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
                BlockPos pos = packet.getPos().offset(packet.getDirection());
                System.out.println("Rightclicked at: " + System.currentTimeMillis());
                RuhamaPlus.friendBlocks.put(pos, 300);
            }
        }
        if (Objects.requireNonNull(ModuleManager.getModuleByName("Criticals")).isToggled()) {
            try {
                if (!(Minecraft.getMinecraft().player == null) && !(Minecraft.getMinecraft().world == null)) {
                    if (packetIn instanceof CPacketUseEntity) {

                        CPacketUseEntity bruh = (CPacketUseEntity) packetIn;

                        if (bruh.getAction() == CPacketUseEntity.Action.ATTACK && Minecraft.getMinecraft().player.onGround)
                            (((Criticals) Objects.requireNonNull(ModuleManager.getModuleByName("Criticals")))).performCritical();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}