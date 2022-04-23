package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.MapPeek;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiContainer.class})
public class MixinContainerGui extends GuiScreen
{
    MapPeek peek = new MapPeek();

    @Inject(
            method = {"drawScreen(IIF)V"},
            at = {@At("RETURN")}
    )
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo info)
    {
        try
        {
            this.peek.draw(mouseX, mouseY, (GuiContainer) this.mc.currentScreen);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
