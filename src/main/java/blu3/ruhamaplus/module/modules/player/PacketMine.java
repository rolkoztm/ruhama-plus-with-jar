package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.event.events.EventPlayerDamageBlock;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class PacketMine extends Module {

    public PacketMine() { super("PacketMine", 0, Category.PLAYER, "fysgdtvubfh", settings);
    this.manualTimer = new TimeUtils();
    }

    public static SettingMode render = new SettingMode("Render: ", "Text", "Highlight");

    private static final List<SettingBase> settings = Collections.singletonList(render);

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void listener(EventPlayerDamageBlock event){
        BlockPos pos = event.getPos();
        EnumFacing facing = event.getFacing();
        if (!(this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) && !(this.mc.world.getBlockState(pos).getBlock() == Blocks.BARRIER)&& !(this.mc.world.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME)&& !(this.mc.world.getBlockState(pos).getBlock() == Blocks.END_PORTAL)&& !(this.mc.world.getBlockState(pos).getBlock() == Blocks.PORTAL)) {
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
            if (mining == null) this.mining = pos;
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
            event.setCanceled(true);
        }
    }


    private BlockPos mining = null;
    private final TimeUtils manualTimer;
    public String m;

    public void fastUpdate() {
        if (mining == null) {
            return;
        }
        if (this.mc.world.getBlockState(mining).getMaterial().isReplaceable()){
            mining = null;
        }
        if (manualTimer.passedMs(500)) {
            m = "Mining";
        }
        if (manualTimer.passedMs(1000)) {
            m = "Mining.";
        }
        if (manualTimer.passedMs(1500)) {
            m = "Mining..";
        }
        if (manualTimer.passedMs(2000)) {
            m = "Mining...";
            manualTimer.reset();
        }
    }

    public void onRender() {
        if (mining != null) {
            if (render.is("Text")) RenderUtils.drawText(mining, m);
            if (render.is("Highlight")) RenderUtils.drawFilledBlockBox(new AxisAlignedBB(mining), 0, 1, 1, 0.3f);
        }
    }
}
