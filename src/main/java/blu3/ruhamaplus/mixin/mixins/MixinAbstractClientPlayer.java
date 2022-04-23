package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.RuhamaPlus;

import blu3.ruhamaplus.module.ModuleManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;




import javax.annotation.Nullable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
    @Shadow @Nullable protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir){
        String uuid = getPlayerInfo().getGameProfile().getId().toString();

        if(RuhamaPlus.getInstance().capeUtils.isCape(uuid) && ModuleManager.getModuleByName("Capes").isToggled()) {
            cir.setReturnValue(new ResourceLocation("ruhamaplus:textures/cape.png"));
        }
    }
}
