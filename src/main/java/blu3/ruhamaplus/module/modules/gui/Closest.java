package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.event.events.TotemPopEvent;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Closest extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0.0D, 1920.0D, 400.0D, 10, "X"),
            new SettingSlider(0.0D, 1080.0D, 400.0D, 10, "Y"),
            new SettingToggle(false, "Background"),
            new SettingToggle(false, "RenderBox"));

    public Closest()
    {
        super("Closest", 0, Category.GUI, "bruhhh", settings);
    }


    List<Entity> knownPlayers = new ArrayList<>();
    List<Entity> players;

    private HashMap<String, Integer> popList = new HashMap();



    public void onUpdate(){
        if(mc.player == null) return;
        players = mc.world.loadedEntityList.stream().filter(e-> e instanceof EntityPlayer).collect(Collectors.toList());
        try {
            for (Entity e : players) {
                if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
                    if (!knownPlayers.contains(e)) {
                        knownPlayers.add(e);
                    }
                }
            }
        } catch(Exception e){}
        try {
            for (Entity e : knownPlayers) {
                if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
                    if (!players.contains(e)) {
                        knownPlayers.remove(e);;
                    }
                }
            }
        } catch(Exception e){}

        if (!(this.mc.player == null) && !(this.mc.world == null)) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.getHealth() <= 0) {
                    if (popList.containsKey(player.getName())) {
                        popList.remove(player.getName(), popList.get(player.getName()));
                    }
                }
            }
        }
    }


    public void onOverlay() {
        int x = (int) this.getSlider("X");
        int y = (int) this.getSlider("Y");
        
        if (!knownPlayers.isEmpty()) {

            EntityPlayer l_Player = null;

            try {
                List<Entity> players = new ArrayList<>(this.mc.world.playerEntities);
                players.remove(this.mc.player);
                players.sort((a, b) -> Float.compare(a.getDistance(this.mc.player), b.getDistance(this.mc.player)));
                l_Player = (EntityPlayer) players.get(0);

            } catch (Exception ignored) {
            }

            //l_Player = this.mc.player;
            int p;
            if (this.mc.getConnection() == null || Objects.requireNonNull(ModuleManager.getModuleByName("FakePlayer")).isToggled()) {
                p = -1;
            } else {
                p = this.mc.getConnection().getPlayerInfo(l_Player.getName()).getResponseTime();
            }
            final String Name = l_Player.getName();
            final int Healt = Math.round(l_Player.getHealth() + l_Player.getAbsorptionAmount());
            final String Health = "" + Healt;
            final int Distanc = (int) this.mc.player.getDistance((Entity) l_Player);
            final InventoryPlayer items = l_Player.inventory;
            final ItemStack inHand = l_Player.getHeldItemMainhand();
            final ItemStack boots = items.armorItemInSlot(0);
            final ItemStack leggings = items.armorItemInSlot(1);
            final ItemStack body = items.armorItemInSlot(2);
            final ItemStack helm = items.armorItemInSlot(3);
            final String Distance = "" + Distanc;
            final String Ping = "" + p;
            final String helm2 = helm.getItem().getItemStackDisplayName(helm);
            final String body2 = body.getItem().getItemStackDisplayName(body);
            final String leggings2 = leggings.getItem().getItemStackDisplayName(leggings);
            final String boots2 = boots.getItem().getItemStackDisplayName(boots);



            mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + Name, x + 20, y - 55, 0);
            if (FriendManager.get().isFriend(l_Player.getName().toLowerCase())) {
                if(this.getBoolean("Background")) GuiScreen.drawRect(x - 20, y - 65, x + 165, y + 25, 1879048192);
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + "Verified Cool" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Diamond Helmet") && body2.equals("Diamond Chestplate") && leggings2.equals("Diamond Leggings") && boots2.equals("Diamond Boots")) {
                if(this.getBoolean("Background")) GuiScreen.drawRect(x - 20, y - 65, x + 180, y + 25, 1879048192);
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + "Potential Threat" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Chain Helmet") && body2.equals("Chain Chestplate") && leggings2.equals("Chain Leggings") && boots2.equals("Chain Boots") && this.mc.getCurrentServerData().serverIP.contains("endcrystal")) {
                if(this.getBoolean("Background")) GuiScreen.drawRect(x - 20, y - 65, x + 155, y + 25, 1879048192);
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + "Netherite Retard" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Diamond Helmet") && body2.equals("Elytra") && leggings2.equals("Diamond Leggings") && boots2.equals("Diamond Boots")) {
                if(this.getBoolean("Background")) GuiScreen.drawRect(x - 20, y - 65, x + 135, y + 25, 1879048192);
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.YELLOW + "Wasp" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Air") && body2.equals("Air") && leggings2.equals("Air") && boots2.equals("Air")) {
                if(this.getBoolean("Background")) GuiScreen.drawRect(x - 20, y - 65, x + 135, y + 25, 1879048192);
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.GREEN + "Naked" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else {
                if(this.getBoolean("Background")) GuiScreen.drawRect(x - 20, y - 65, x + 135, y + 25, 1879048192);
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.LIGHT_PURPLE + "Fuckable" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            }
            int colour = 0;
            try {
                colour = l_Player.getHealth() + l_Player.getAbsorptionAmount() > 20.0F ? 2158832 : MathHelper.hsvToRGB((l_Player.getHealth() + l_Player.getAbsorptionAmount()) / 20.0F / 3.0F, 1.0F, 1.0F);
            } catch (Exception ignored) {
            }
            mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + "Health: " + ChatFormatting.RESET + Health, x + 20, y - 15, colour);


        if(popList.get(l_Player.getName()) == null) {
            mc.fontRenderer.drawStringWithShadow("Pops: 0" , x + 80, y - 15, colour);
        } else if(!(popList.get(l_Player.getName()) == null)) {
            mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + "Pops: " + ChatFormatting.RED + popList.get(l_Player.getName()), x + 80, y - 15, 0);
        }

            GuiInventory.drawEntityOnScreen(x, y - 55 + 62, 30, (float)x + (float)51.0 - ((float)x +(float) 50.0), (float)y + (float)75.0 - (float)50.0 - (float)y + (float)12.0, (EntityLivingBase)l_Player);
            GuiInventory.drawEntityOnScreen(x, y - 55 + 6200, 30, (float)x + (float)51.0 - ((float)x +(float) 50.0), (float)y + (float)75.0 - (float)50.0 - (float)y + (float)12.0, (EntityLivingBase)l_Player);

            int i = 0;
            final List<ItemStack> armor = new ArrayList<ItemStack>();
            for (final ItemStack is : l_Player.getArmorInventoryList()) {
                armor.add(is);
            }
            Collections.reverse(armor);
            for (final ItemStack is : armor) {
                final int yy = (int) (y - 35.0);
                final int xx = (int) (x + i + 16.0);
                RenderHelper.enableGUIStandardItemLighting();
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, xx, yy);
                this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, is, xx, yy);
                RenderHelper.disableStandardItemLighting();
                i += 18;
            }
            final int yy2 = (int) (y - 35.0);
            final int xx2 = (int) (x + 90.0);
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(inHand, xx2, yy2);
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, inHand, xx2, yy2);
            RenderHelper.disableStandardItemLighting();
            final ItemStack inOffHand = l_Player.getHeldItemOffhand();
            final int yyy = (int) (y + -35.0);
            final int xxx = (int) (x + 110.0);
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(inOffHand, xxx, yyy);
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, inOffHand, xxx, yyy);
            RenderHelper.disableStandardItemLighting();


            if (l_Player.isPotionActive(MobEffects.STRENGTH)) {
                final DecimalFormat format1 = new DecimalFormat("0");
                final DecimalFormat format2 = new DecimalFormat("00");
                final int duration = l_Player.getActivePotionEffect(MobEffects.STRENGTH).getDuration() / 20;
                final int amplifier = l_Player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() + 1;
                final double p2 = duration % 60;
                final double p3 = duration / 60;
                final double p4 = p3 % 60.0;
                final String minutes = format1.format(p4);
                final String seconds = format2.format(p2);
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.RED + "Strength " + amplifier + TextFormatting.WHITE + " " + minutes + ":" + seconds, x + 20, y, 0);
            } else {
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.RED + "Strength" + TextFormatting.WHITE + " None", x + 20, y, 0);
            }

            if (l_Player.isPotionActive(MobEffects.WEAKNESS)) {
                final DecimalFormat format1 = new DecimalFormat("0");
                final DecimalFormat format2 = new DecimalFormat("00");
                final int duration = l_Player.getActivePotionEffect(MobEffects.WEAKNESS).getDuration() / 20;
                final int amplifier = l_Player.getActivePotionEffect(MobEffects.WEAKNESS).getAmplifier() + 1;
                final double p2 = duration % 60;
                final double p3 = duration / 60;
                final double p4 = p3 % 60.0;
                final String minutes = format1.format(p4);
                final String seconds = format2.format(p2);
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + "Weakness " + amplifier + TextFormatting.WHITE + " " + minutes + ":" + seconds, x + 20, y + 10, 0);
            } else {
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + "Weakness" + TextFormatting.WHITE + " None", x + 20, y + 10, 0);
            }
        }
    }


    public void onRender() {
        EntityPlayer l_Player = null;
        try {
            List<Entity> players = new ArrayList<>(this.mc.world.playerEntities);
            players.remove(this.mc.player);
            players.sort((a, b) -> Float.compare(a.getDistance(this.mc.player), b.getDistance(this.mc.player)));
            l_Player = (EntityPlayer) players.get(0);

        } catch (Exception ignored) {}

        if (this.getBoolean("RenderBox") && l_Player != null) RenderUtils.drawFilledBlockBox(l_Player.getEntityBoundingBox(), 1.0F, 0.0F, 0.0F, 0.3F);
    }

    public boolean onPacketRead(Packet<?> packet)
    {
        if (!(this.mc.player == null) && !(this.mc.world == null)){
            if (packet instanceof SPacketEntityStatus){
                SPacketEntityStatus racket = (SPacketEntityStatus) packet; // had to change the name LMAO
                if (racket.getOpCode() == 35) {
                    Entity entity = racket.getEntity(mc.world);
                    TotemPopEvent event = new TotemPopEvent(entity);
                    MinecraftForge.EVENT_BUS.post(event);
                    return false;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {

        Entity e = event.getEntity();

        if(popList == null) {
            popList = new HashMap<>();
        }

        if(popList.get(e.getName()) == null) {
            popList.put(e.getName(), 1);

        } else if(!(popList.get(e.getName()) == null)) {
            int popCounter = popList.get(e.getName());
            int newPopCounter = popCounter += 1;

            popList.put(e.getName(), newPopCounter);
        }
    }


}