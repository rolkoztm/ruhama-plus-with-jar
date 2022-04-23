package blu3.ruhamaplus.mixin.mixins;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public LoadingPlugin()
    {
        System.out.println("Initializing RuhamaPlus coremod. \n\n\n\n\n\n\n\n");

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.blu3.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        Display.setTitle("Ruhama+ Coremod Initializing");
    }

    // no idea what any of this does LOL

    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    public String getModContainerClass()
    {
        return null;
    }

    public String getSetupClass()
    {
        return null;
    }

    public void injectData(Map<String, Object> data)
    {
    }

    public String getAccessTransformerClass()
    {
        return null;
    }
}
