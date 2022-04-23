package blu3.ruhamaplus.event.events;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventPlayerUseItem extends Event {
    private Item item;

    public EventPlayerUseItem(Item item) {
        this.item = item;
    }
}
