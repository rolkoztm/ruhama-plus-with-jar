package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.gui.ruhama.AdvancedText;
import blu3.ruhamaplus.gui.ruhama.TextWindow;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class CoordHud extends Module {

    public CoordHud()
    {
        super("Coords", 0, Category.GUI, "ez coord grabber", null);

        this.getWindows().add(new TextWindow(2, 150, "Coordinates"));
    }


    public void onOverlay(){
        final int s = this.mc.player.dimension;

        double x = this.mc.player.posX;
        int y = (int) this.mc.player.posY;
        double z = this.mc.player.posZ;
        this.getWindows().get(0).clearText();

        double roundX = Math.round(x * 100.0) / 100.0;
        double roundZ = Math.round(z * 100.0) / 100.0;

        int age = (int) (System.currentTimeMillis() / 20L % 510L);
        int color = (new Color(255, MathHelper.clamp(age > 255 ? 510 - age : age, 0, 255), MathHelper.clamp(255 - (age > 255 ? 510 - age : age), 0, 255))).getRGB();

        if (s == 0)this.getWindows().get(0).addText(new AdvancedText((int) x + ", " + y + ", " + (int) z + " [" + (int) roundX/8 + ", " + y + ", " + (int) roundZ/8 + "]", true, color));
        else if (s == -1)this.getWindows().get(0).addText(new AdvancedText((int) x + ", " + y + ", " + (int) z + " [" + (int) roundX*8 + ", " + y + ", " + (int) roundZ*8 + "]", true, color));
        else if (s == 1)this.getWindows().get(0).addText(new AdvancedText((int) x + ", " + y + ", " + (int) z, false, color));
        else this.getWindows().get(0).addText(new AdvancedText((int) x + ", " + y + ", " + (int) z, true, color));
    }

}
