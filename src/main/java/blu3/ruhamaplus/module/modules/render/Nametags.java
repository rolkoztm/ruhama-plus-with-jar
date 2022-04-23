package blu3.ruhamaplus.module.modules.render;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import java.util.List;
public class Nametags extends Module {
    public Nametags() { super("Nametags", 0, Category.RENDER, "now THAAAATS a lotta players", null); }

    //please for the love of god dont use this
    public void onRender() {
        List<EntityPlayer> players = new ArrayList<>(this.mc.world.playerEntities);
        players.remove(this.mc.player);
        for (EntityPlayer e : players){
            RenderUtils.drawNametag(e);
        }
    }
}
