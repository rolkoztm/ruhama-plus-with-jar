package blu3.ruhamaplus;

import blu3.ruhamaplus.command.*;
import blu3.ruhamaplus.gui.ruhama.*;
import blu3.ruhamaplus.module.*;
import blu3.ruhamaplus.module.modules.gui.*;
import blu3.ruhamaplus.settings.*;
import blu3.ruhamaplus.utils.*;
import blu3.ruhamaplus.utils.friendutils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.*;
import java.util.Map.Entry;

@Mod(
        modid = "ruhamaplus",
        name = "Ruhama+",
        version = "1.2",
        acceptedMinecraftVersions = "[1.12.2]"
)

/**
 * retarded client, base by bleach and all the changes were made by me. this is stupid go fuck yourself
 */
public class RuhamaPlus {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static HashMap<BlockPos, Integer> friendBlocks = new HashMap<>();

    private long timer = 0L;
    private boolean timerStart = false;

    public CapeUtils capeUtils;

    private static final FriendManager friendManager = new FriendManager();

    public static String version = "1.2";

    public static FriendManager getFriendManager() {
        return friendManager;
    }

    @Mod.Instance
    private static RuhamaPlus INSTANCE;

    public RuhamaPlus() {
        INSTANCE = this;
    }

    public static RuhamaPlus getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("Ruhama+ initializing...");
        System.out.println("Initialization beginning...   ");

        System.out.println("\n\n|\\  | |\\  | |\\  | |\\  | |\\  | |\\  | |\\  | |\\  |\n" +
                "| \\ | | \\ | | \\ | | \\ | | \\ | | \\ | | \\ | | \\ |\n" +
                "|  \\| |  \\| |  \\| |  \\| |  \\| |  \\| |  \\| |  \\|\n\n");


        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventListeners());
        MinecraftForge.EVENT_BUS.register(new Rainbow());

        ClickGui.clickGui.initWindows();

        FileMang.init();

        FileMang.createFile("FriendList.json");
        FileMang.createFile("cleanchat.blu3");

        FileMang.readModules();
        FileMang.readSettings();
        FileMang.readClickGui();
        FileMang.readBinds();
        FileMang.loadFriends();

        for (Module m : ModuleManager.getModules()) {
            for (SettingBase s : m.getSettings()) {
                if (s instanceof SettingMode) {
                    s.asMode().mode = MathHelper.clamp(s.asMode().mode, 0, s.asMode().modes.length - 1);
                } else if (s instanceof SettingSlider) {
                    s.asSlider().value = MathHelper.clamp(s.asSlider().value, s.asSlider().min, s.asSlider().max);
                }
            }
        }

        this.timerStart = true;
        System.out.println("Initialization completed!");
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        Display.setTitle("Ruhama+ Initializing commands...");
        System.out.println("Initializing commands...");
        addCommand(new PeekCmd.PeekCommand());
        addCommand(new InvSorterCmd());
        addCommand(new StashFinderCmd());
        addCommand(new EntityDesyncCmd());
        addCommand(new LoginCmd());
        addCommand(new SetYawCmd());
        addCommand(new WaspCmd());
        addCommand(new HelpCmd());
        addCommand(new SaveConfigCmd());
        addCommand(new FriendCommand());
        addCommand(new UnfriendCommand());
        addCommand(new ToggleCmd());
        addCommand(new FakePlayerCmd());
        addCommand(new FixGuiCmd());
        addCommand(new FOVCmd());

        ChatUtils.setWords();

        MinecraftForge.EVENT_BUS.register(new PeekCmd());
        System.out.println("Commands initialized!");

        System.out.println("Capes loading...");

        capeUtils = new CapeUtils();

        System.out.println("Capes initialized!");
        Display.setTitle("Ruhama+ " + version);
    }

    public void addCommand(IClientCommand o) {
        ClientCommandHandler.instance.registerCommand(o);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (mc.player != null && mc.world != null) {
            if (mc.world.isBlockLoaded(mc.player.getPosition())) {
                ModuleManager.onRender();
            }
        }
    }

    @SubscribeEvent
    public void onText(Text event) {
        if (event.getType().equals(ElementType.TEXT)) {
            if (!(mc.currentScreen instanceof NewRuhamaGui)) {
                Iterator<?> textIter = NewRuhamaGui.textWins.iterator();

                label41:

                while (true) {
                    MutableTriple<?, ?, ?> e;
                    do {
                        if (!textIter.hasNext()) {
                            break label41;
                        }

                        e = (MutableTriple<?, ?, ?> ) textIter.next();
                    } while (!Objects.requireNonNull(ModuleManager.getModuleByName(((Module) e.left).getName())).isToggled());

                    int h = 2;

                    for (Iterator<?> iter = ((TextWindow) e.right).getText().iterator(); iter.hasNext(); h += 10) {
                        AdvancedText s = (AdvancedText) iter.next();
                        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());

                        int x = (double) ((TextWindow) e.right).posX > (double) scale.getScaledWidth() / 1.5D ? ((TextWindow) e.right).posX + ((TextWindow) e.right).len - mc.fontRenderer.getStringWidth(s.text) - 2 : (((TextWindow) e.right).posX < scale.getScaledWidth() / 3 ? ((TextWindow) e.right).posX + 2 : ((TextWindow) e.right).posX + ((TextWindow) e.right).len / 2 - mc.fontRenderer.getStringWidth(s.text) / 2);

                        if (s.shadow) {
                            mc.fontRenderer.drawStringWithShadow(s.text, (float) x, (float) (((TextWindow) e.right).posY + h), s.color);
                        } else {
                            mc.fontRenderer.drawString(s.text, x, ((TextWindow) e.right).posY + h, s.color);
                        }
                    }
                }
            }
            ModuleManager.onOverlay();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == Keyboard.KEY_PAUSE) return;
            ModuleManager.onBind(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (System.currentTimeMillis() - 5000L > this.timer && this.timerStart) {
            this.timer = System.currentTimeMillis();

            FileMang.saveClickGui();
            FileMang.saveSettings();
            FileMang.saveModules();
            FileMang.saveBinds();
        }

        if (event.phase == Phase.START && mc.player != null && mc.world != null) {
            if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, 0.0D, mc.player.posZ))) {
                ModuleManager.onUpdate();
                ModuleManager.updateKeys();

                Entry<?, ?> e;

                try {
                    for (Iterator<?> iter = friendBlocks.entrySet().iterator(); iter.hasNext(); friendBlocks.replace((BlockPos) e.getKey(), (Integer) e.getValue() - 1)) {
                        e = (Entry<?, ?>) iter.next();
                        if ((Integer) e.getValue() <= 0) {
                            friendBlocks.remove(e.getKey());
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    @SubscribeEvent
    public void onFrame(TickEvent event) {
        if (event.phase == Phase.START && mc.player != null && mc.world != null) {
            if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, 0.0D, mc.player.posZ))) {
                ModuleManager.fastUpdate();
            }
        }
    }
}
