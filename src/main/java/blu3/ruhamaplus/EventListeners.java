package blu3.ruhamaplus;

import blu3.ruhamaplus.event.events.*;
import blu3.ruhamaplus.utils.Util;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * anything that would be useful to always listen for goes here. better than constant register/unregister shit
 */
public class EventListeners implements Util {
    @SubscribeEvent
    public void totemListener(EventReadPacket event) {
        if (!(mc.player == null) && !(mc.world == null)) {
            if (event.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
                if (packet.getOpCode() == 35) {
                    Entity entity = packet.getEntity(mc.world);
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent(entity));
                }
            }
        }
    }
}
