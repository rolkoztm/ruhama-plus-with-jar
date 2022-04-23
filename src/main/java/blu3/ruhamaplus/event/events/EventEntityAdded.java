package blu3.ruhamaplus.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventEntityAdded extends Event {
    private final Entity entity;

    public EventEntityAdded(Entity entity) { this.entity = entity; }

    public Entity getEntity() { return this.entity; }

    public boolean isCancelable() { return true; }
}
