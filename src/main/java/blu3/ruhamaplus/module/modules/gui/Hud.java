package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.Rainbow;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Hud extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "Watermark"),
            new SettingToggle(true, "ServerIP"),
            new SettingToggle(true, "Ping"),
            new SettingToggle(true, "Username"),
            new SettingToggle(true, "Dimension "));

    public Hud() {super("Hud", Keyboard.KEY_NONE, Category.GUI, "Show stuff on screen. ", settings);}

    public int getPing() {
        int p = -1;
        if (this.mc.player == null || this.mc.getConnection() == null || this.mc.getConnection().getPlayerInfo(this.mc.player.getName()) == null) {
            p = -1;
        }
        else {
            p = this.mc.getConnection().getPlayerInfo(this.mc.player.getName()).getResponseTime();
        }
        return p;
    }


    public int height = 2;
    @Override
    public void onEnable() {MinecraftForge.EVENT_BUS.register(this);}

    @Override
    public void onDisable() {MinecraftForge.EVENT_BUS.unregister(this);}


    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        final Entity entity = this.mc.getRenderViewEntity();

        height = 2;

        if (this.getSetting(0).asToggle().state) {
            mc.fontRenderer.drawStringWithShadow("Ruhama+ " + RuhamaPlus.version, 2, height, Rainbow.getInt());
            height+=10;
        }

        if(this.getSetting(1).asToggle().state) {

            if (mc.getCurrentServerData() != null && !this.mc.getCurrentServerData().serverIP.equals("")) {
                mc.fontRenderer.drawStringWithShadow("IP: " + TextFormatting.WHITE + this.mc.getCurrentServerData().serverIP, 2, height, Rainbow.getInt());
            }
            else if (this.mc.isIntegratedServerRunning()) {
                mc.fontRenderer.drawStringWithShadow("IP: " + TextFormatting.WHITE + "Singleplayer", 2, height, Rainbow.getInt());
            }
            else if (this.mc.getCurrentServerData() == null) {
                mc.fontRenderer.drawStringWithShadow("IP: " + TextFormatting.WHITE + "ERROR", 2, height, Rainbow.getInt());
            }
            height+=10;
        }

        if (this.getSetting(2).asToggle().state) {
            mc.fontRenderer.drawStringWithShadow("Ping: "+ TextFormatting.WHITE + this.getPing() + "ms", 2, height, Rainbow.getInt());
            height+=10;
        }

        if (this.getSetting(3).asToggle().state) {
            mc.fontRenderer.drawStringWithShadow("Hello, " + TextFormatting.WHITE + this.mc.player.getName(), 2, height, Rainbow.getInt());
            height+=10;
        }
        if (this.getSetting(4).asToggle().state) {

            final int s = this.mc.player.dimension;
            String biom = "Dimension:" + TextFormatting.WHITE + " Unknown";

            if (s == 0) {
                biom = "Dimension: " + TextFormatting.WHITE + "Overworld";
            }
            else if (s == -1) {
                biom = "Dimension: " + TextFormatting.WHITE + "Nether";
            }
            else if (s == 1) {
                biom = "Dimension: " + TextFormatting.WHITE + "End";
            }
            mc.fontRenderer.drawStringWithShadow(biom, 2, height, Rainbow.getInt());
            height+=10;
        }
    }
}
