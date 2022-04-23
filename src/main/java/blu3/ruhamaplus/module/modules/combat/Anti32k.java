package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Anti32k extends Module {

    public Anti32k() {
        super("Anti32k", Keyboard.KEY_NONE, Category.COMBAT, "d dsxvgedgf gsfz", settings);
    }

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingMode("Mode: ", "Logout", "Counter")
    );

    private boolean wasHNEnabled;

    private final List<EntityPlayer> ezplayers = new ArrayList<>();
    @Override
    public void onUpdate() {
        ezplayers.clear();
        ezplayers.addAll(this.mc.world.playerEntities);
        ezplayers.remove(this.mc.player);
        for (Object o : new ArrayList<>(ezplayers)) {
            Entity e = (EntityPlayer) o;
            if (FriendManager.get().isFriend(e.getName().toLowerCase())){
                ezplayers.remove(e);
            }
        }

        for (EntityPlayer e : ezplayers) {
            if (mc.player.getDistance(e) < 10) {
                if (!check32k(e)) continue;
            }
        }
    }

    private boolean is32k(ItemStack stack) {
        if (stack.getItem() instanceof net.minecraft.item.ItemSword) {
            NBTTagList enchants = stack.getEnchantmentTagList();
            if (enchants != null)
                for (int i = 0; i < enchants.tagCount(); i++) {
                    if (enchants.getCompoundTagAt(i).getShort("lvl") >= Short.MAX_VALUE)
                        return true;
                }
        }
        return false ;
    }

    private boolean check32k(EntityPlayer e) {

        if (!FriendManager.get().isFriend(e.getName().toLowerCase())) {
            if (is32k(e.getHeldItemMainhand())) {



                switch (this.getMode("Mode: ")){
                    case 0: {
                        mc.player.sendChatMessage("> " + e.getName() + " Tried to 32k me! Disconnecting...");
                        logOut(e.getName() + " tried to 32k you!");
                    }
                    case 1: {
                        boolean wasCAEnabled = ModuleManager.getModuleByName("blu3CA").isToggled();
                        boolean wasEMEnabled = ModuleManager.getModuleByName("EnhancedMovement").isToggled();
                        wasHNEnabled = ModuleManager.getModuleByName("HopperNuker").isToggled();
                        boolean wasSAEnabled = ModuleManager.getModuleByName("ShulkerAura").isToggled();

                        /*ModuleManager.getModuleByName("EnhancedMovement").disable();*/
                        ModuleManager.getModuleByName("blu3CA").enable();
                        /*ModuleManager.getModuleByName("HopperNuker").enable();
                        ModuleManager.getModuleByName("ShulkerAura").enable();*/
                        mc.player.inventory.currentItem = 1;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void logOut(String reason) {
        this.mc.player.connection.getNetworkManager().closeChannel(new TextComponentString(reason));
        this.setToggled(false);
    }
}
