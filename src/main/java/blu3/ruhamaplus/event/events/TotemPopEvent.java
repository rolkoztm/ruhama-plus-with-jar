package blu3.ruhamaplus.event.events;


import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TotemPopEvent extends Event {

    private Entity entity;

    public TotemPopEvent(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}