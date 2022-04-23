package blu3.ruhamaplus.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Wrapper
{
    final static Minecraft mc = Minecraft.getMinecraft();
    
    public static Minecraft getMc()
    {
        return mc;
    }

    public static EntityPlayerSP getPlayer()
    {
        return mc.player;
    }
}
