package blu3.ruhamaplus.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventPlayerDamageBlock extends Event {
    private BlockPos pos;
    private EnumFacing facing;

    public EventPlayerDamageBlock(BlockPos pos, EnumFacing facing) {
        super();
        this.pos = pos;
        this.facing = facing;
    }
    public BlockPos getPos(){
        return this.pos;
    }
    public EnumFacing getFacing(){
        return this.facing;
    }
    public boolean isCancelable() {
        return true;
    }
}
