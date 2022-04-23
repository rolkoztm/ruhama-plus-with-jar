package blu3.ruhamaplus.event.events;


import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.Packet;


/**
 * @author sl
 */
public class EventReadPacket extends Event {
    private Packet<?> packet;

    public EventReadPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
    public boolean isCancelable() {
        return true;
    }
}