package blu3.ruhamaplus.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class Rainbow {
    public static int rgb;
    public static float hue = 0.0F;
    public static int speed = 1;

    @SubscribeEvent
    public void onTick(TickEvent event) {
        this.rgb = Color.HSBtoRGB(this.hue, 1.0F, 1.0F);
        this.hue += this.speed / 2000.0F;
    }

    public static int getInt(){
        return blu3.ruhamaplus.utils.Rainbow.rgb;
    }

    public static float getHue() {return blu3.ruhamaplus.utils.Rainbow.hue;}
}