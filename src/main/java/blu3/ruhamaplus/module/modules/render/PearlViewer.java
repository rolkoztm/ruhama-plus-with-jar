package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.Map.Entry;

public class PearlViewer extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Chat"), new SettingToggle(true, "Render"), new SettingSlider(0.0D, 20.0D, 5.0D, 1, "Render Time: "), new SettingSlider(0.0D, 10.0D, 3.5D, 2, "Thick: "));
    private final HashMap<UUID, List<Vec3d>> poses = new HashMap<>();
    private final HashMap<UUID, Double> time = new HashMap<>();

    public PearlViewer()
    {
        super("PearlViewer", 0, Category.RENDER, "Shows Where Enderpearls Are Going", settings);
    }

    public void onUpdate()
    {
        Iterator iter = (new HashMap(this.time)).entrySet().iterator();

        while (iter.hasNext())
        {
            Entry<UUID, Double> e = (Entry) iter.next();

            if (e.getValue() <= 0.0D)
            {
                this.poses.remove(e.getKey());
                this.time.remove(e.getKey());
            } else
            {
                this.time.replace(e.getKey(), e.getValue() - 0.05D);
            }
        }

        iter = this.mc.world.loadedEntityList.iterator();

        while (true)
        {
            while (true)
            {
                Entity e;
                do
                {
                    if (!iter.hasNext())
                    {
                        return;
                    }

                    e = (Entity) iter.next();
                } while (!(e instanceof EntityEnderPearl));

                if (!this.poses.containsKey(e.getUniqueID()))
                {
                    if (this.getSetting(0).asToggle().state)
                    {
                        for (net.minecraft.entity.player.EntityPlayer entityPlayer : this.mc.world.playerEntities)
                        {
                            if (entityPlayer.getDistance(e) < 4.0F && ((Entity) entityPlayer).getName() != this.mc.player.getName())
                            {
                                ChatUtils.warn(((Entity) entityPlayer).getName() + " Threw a pearl");

                                break;
                            }
                        }
                    }

                    this.poses.put(e.getUniqueID(), new ArrayList<>(Collections.singletonList(e.getPositionVector())));
                    this.time.put(e.getUniqueID(), this.getSetting(2).asSlider().getValue());
                } else
                {
                    this.time.replace(e.getUniqueID(), this.getSetting(2).asSlider().getValue());
                    List<Vec3d> v = this.poses.get(e.getUniqueID());
                    v.add(e.getPositionVector());
                }
            }
        }
    }

    public void onRender()
    {
        if (this.getSetting(1).asToggle().state)
        {
            RenderUtils.glSetup();
            GL11.glLineWidth((float) this.getSetting(3).asSlider().getValue());
            Iterator posIter = this.poses.entrySet().iterator();

            while (true)
            {
                Entry e;
                do
                {
                    if (!posIter.hasNext())
                    {
                        RenderUtils.glCleanup();
                        return;
                    }

                    e = (Entry) posIter.next();
                } while (((List) e.getValue()).size() <= 2);

                GL11.glBegin(1);
                Random rand = new Random(e.getKey().hashCode());
                double r = 0.5D + rand.nextDouble() / 2.0D;
                double g = 0.5D + rand.nextDouble() / 2.0D;
                double b = 0.5D + rand.nextDouble() / 2.0D;
                GL11.glColor3d(r, g, b);
                double[] rPos = RenderUtils.rPos();

                for (int i = 1; i < ((List) e.getValue()).size(); ++i)
                {
                    GL11.glVertex3d(((Vec3d) ((List) e.getValue()).get(i)).x - rPos[0], ((Vec3d) ((List) e.getValue()).get(i)).y - rPos[1], ((Vec3d) ((List) e.getValue()).get(i)).z - rPos[2]);
                    GL11.glVertex3d(((Vec3d) ((List) e.getValue()).get(i - 1)).x - rPos[0], ((Vec3d) ((List) e.getValue()).get(i - 1)).y - rPos[1], ((Vec3d) ((List) e.getValue()).get(i - 1)).z - rPos[2]);
                }

                GL11.glEnd();
            }
        }
    }
}
