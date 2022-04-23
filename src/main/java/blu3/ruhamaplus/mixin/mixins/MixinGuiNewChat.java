package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.utils.ChatUtils;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Objects;

@Mixin(value = GuiNewChat.class, priority = 2147483647)
public abstract class MixinGuiNewChat {

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(int left, int top, int right, int bottom, int color) {
        if(!(Objects.requireNonNull(ModuleManager.getModuleByName("BetterChat")).isToggled()) || !(Objects.requireNonNull(ModuleManager.getModuleByName("BetterChat"))).getBoolean("NoBackground")) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}