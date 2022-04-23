package blu3.ruhamaplus.module;

import blu3.ruhamaplus.errors.NoModuleFoundException;
import blu3.ruhamaplus.module.modules.chat.*;
import blu3.ruhamaplus.module.modules.combat.*;
import blu3.ruhamaplus.module.modules.experimental.*;
import blu3.ruhamaplus.module.modules.exploits.*;
import blu3.ruhamaplus.module.modules.gui.*;
import blu3.ruhamaplus.module.modules.misc.*;
import blu3.ruhamaplus.module.modules.player.*;
import blu3.ruhamaplus.module.modules.render.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import org.lwjgl.input.*;

import java.util.*;


public class ModuleManager
{
    private static final List<Module> mods = Arrays.asList(
            new Annoyer(),
            new Anti32k(),
            new AntiChunkBan(),
            new AnvilFucker(),
            new Aura(),
            new AutoCrystal(),
            new AutoLog(),
            new AutoWither(),
            new BedCityESP(),
            new BedAura(),
            new BedAuraECME(),
            new BedObsidianTrap(),
            new BetterChat(),
            new Capes(),
            new ChatSuffix(),
            new ChestSwap(),
            new ClickGui(),
            new Closest(),
            new CoordHud(),
            new Crasher(),
            new Criticals(),
        /*    new DiscordRPC(),*/
            new DispenserAura(),
            new ElytraFly(),
            new ElytraReplace(),
            new EnhancedMovement(),
            new FakePlayer(),
            new FastUse(),
            new FeetXp(),
            new FOVSlider(),
            new FogColour(),
            new GreenText(),
            new Gui(),
            new HoleFiller(),
            new HoleFinderESP(),
            new HopperNuker(),
            new HopperRadius(),
            new Hud(),
            new InvSorter(),
            new LongRangeAim(),
            new Nametags(),
            new NBTViewer(),
            new NewChunks(),
            //new NewHoleESP(),
            new NoRender(),
            new ObsidianTrap(),
            new PacketMine(),
            new PearlViewer(),
            new Peek(),
            new PlayerFrame(),
            new PlayerRadar(),
            new PvpInfo(),
            new Reach(),
            new SelfTrap(),
            new ShulkerAura(),
            new StashFinder(),
            new Strafe(),
            new StrengthESP(),
            new Surround(),
            new SwingArm(),
            new TabNames(),
            new TestAutoTotem(),
            new ThunderHack(),
            new TotemPopCounter(),
            new TreeAura(),
            new TunnelESP(),
            new VisualRange());

    public static List<Module> getModules()
    {
        return new ArrayList<>(mods);
    }

    public static Module getModuleByName(String name)
    {
        Iterator<Module> modsIterator = mods.iterator();
        Module m;

        do
        {
            if (!modsIterator.hasNext())
            {
                throw new NoModuleFoundException();
            }

            m = modsIterator.next();
        } while (!name.equalsIgnoreCase(m.getName()));

        return m;
    }

    public static List<Module> getModulesInCat(Category cat)
    {
        List<Module> ms = new ArrayList<>();

        for (Module m : mods)
        {
            if (m.getCategory().equals(cat))
            {
                ms.add(m);
            }
        }

        return ms;
    }

    public static void fastUpdate(){
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.fastUpdate();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void onUpdate()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.onUpdate();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void onRender()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.onRender();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void onOverlay()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.onOverlay();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean onPacketRead(Packet<?> packet)
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled() && m.onPacketRead(packet))
                {
                    return true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean onPacketSend(Packet<?> packet)
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled() && m.onPacketSend(packet))
                {
                    return true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void updateKeys()
    {
        if (Minecraft.getMinecraft().currentScreen == null)
        {
            for (Module m : mods)
            {
                try
                {
                    if (Keyboard.isKeyDown(m.getKey().getKeyCode()) && !m.keyActive)
                    {
                        m.keyActive = true;
                        m.toggle();
                    } else if (!Keyboard.isKeyDown(m.getKey().getKeyCode()))
                    {
                        m.keyActive = false;
                    }
                } catch (Exception ignored) {
                    System.out.println("fuck fuck fuck shit that didn't work oh god");
                }
            }
        }
    }

    public static void onBind(int key) {
        if (key == Keyboard.KEY_NONE) return;
        mods.forEach(module -> {
            if(module.getBind() == key){
                module.toggle();
            }
        });
    }
}
