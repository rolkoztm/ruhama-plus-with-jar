package blu3.ruhamaplus.module;

import blu3.ruhamaplus.gui.ruhama.TextWindow;
import blu3.ruhamaplus.settings.*;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class Module
{
    private final Category category;
    private final String desc;
    
    private final List<TextWindow> windows = new ArrayList<>();
    
    public boolean keyActive = false;
    
    protected Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private KeyBinding key;

    int bind;
    
    private boolean toggled;
    private List<SettingBase> settings = new ArrayList<>();

    public Module(String name, int bind, Category cat, String desc, List<SettingBase> settings)
    {
        this.name = name;
        this.registerBind(name, bind);
        this.category = cat;
        this.desc = desc;
        
        if (settings != null)
        {
            this.settings = settings;
        }

        this.toggled = false;
    }

    public Module(String name, Category cat, String desc, List<SettingBase> settings)
    {
        this.name = name;
        this.registerBind(name, 0);
        this.category = cat;
        this.desc = desc;

        if (settings != null)
        {
            this.settings = settings;
        }

        this.toggled = false;
    }

    public void log(String text) {
        ChatUtils.log(text);
    }

    public boolean nullCheck() {
        return (mc.player == null || mc.world == null);
    }

    public int getBind(){
        return bind;
    }

    public void setBind(int b){
        bind = b;
    }

    public void toggle()
    {
        this.toggled = !this.toggled;
        
        if (this.toggled)
        {
            try
            {
                this.onEnable();
                MinecraftForge.EVENT_BUS.register(this);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            try
            {
                this.onDisable();
                MinecraftForge.EVENT_BUS.unregister(this);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onEnable()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable()
    {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void onUpdate()
    {
    }

    public void onRender()
    {
    }

    public void onOverlay()
    {
    }

    public void fastUpdate()
    {
    }

    public void disable(){
        this.setToggled(false);
    }
    public void enable(){
        this.setToggled(true);
    }


    public boolean onPacketRead(Packet<?> packet)
    {
        return false;
    }

    public boolean onPacketSend(Packet<?> packet)
    {
        return false;
    }

    public String getName()
    {
        return this.name;
    }
    
    public Category getCategory()
    {
        return this.category;
    }

    public String getDesc()
    {
        return this.desc;
    }

    public KeyBinding getKey()
    {
        return this.key;
    }

    public Boolean getBoolean(String name){
        SettingBase b = new SettingBase();
        for (SettingBase m : getSettings()){
            if (m instanceof SettingToggle) {
                if (((SettingToggle) m).text.toLowerCase().contains(name.toLowerCase())) {
                    b = m;
                    break;
                }
            }
        }
        return b.asToggle().state;
    }

    public int getMode(String name) {
        SettingBase b = new SettingBase();
        for (SettingBase m : getSettings()){
            if (m instanceof SettingMode) {
                if (((SettingMode) m).text.toLowerCase().contains(name.toLowerCase())) {
                    b = m;
                    break;
                }
            }
        }

         return b.asMode().mode;
    }

    public double getSlider (String name) {
        SettingBase b = new SettingBase();
        for (SettingBase m : getSettings()){
            if (m instanceof SettingSlider) {
                if (((SettingSlider) m).text.toLowerCase().contains(name.toLowerCase())) {
                    b = m;
                    break;
                }
            }
        }
        return b.asSlider().getValue();
    }
    
    public List<SettingBase> getSettings()
    {
        return this.settings;
    }

    public SettingBase getSetting(int s) { return this.getSettings().get(s); }

    public List<TextWindow> getWindows()
    {
        return this.windows;
    }

    public boolean isToggled()
    {
        return this.toggled;
    }

    public void setToggled(boolean toggled)
    {
        this.toggled = toggled;
    }

    public void registerBind(String name, int keycode)
    {
        this.key = new KeyBinding(name, keycode, "Ruhama+");

        ClientRegistry.registerKeyBinding(this.key);
    }
}
