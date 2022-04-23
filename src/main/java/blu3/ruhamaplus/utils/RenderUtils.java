package blu3.ruhamaplus.utils;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RenderUtils implements Util
{

    public static double[] rPos() {
        try {
            return new double[] {

                    (double)ReflectUtils.getField(RenderManager.class, "renderPosX", "field_78725_b").get(RenderUtils.mc.getRenderManager()),
                    (double)ReflectUtils.getField(RenderManager.class, "renderPosY", "field_78726_c").get(RenderUtils.mc.getRenderManager()),
                    (double)ReflectUtils.getField(RenderManager.class, "renderPosZ", "field_78723_d").get(RenderUtils.mc.getRenderManager())
            };
        }
        catch (Exception e) {
            return new double[] { 0.0, 0.0, 0.0 };
        }
    }

    public static void drawFilledBlockBox(AxisAlignedBB box, final float r, final float g, final float b, final float a) {
        try {
            glSetup();
            final double[] rPos = rPos();
            box = new AxisAlignedBB(box.minX - rPos[0], box.minY - rPos[1], box.minZ - rPos[2], box.maxX - rPos[0], box.maxY - rPos[1], box.maxZ - rPos[2]);
            RenderGlobal.renderFilledBox(box, r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5f);
            glCleanup();
        }
        catch (Exception ex) {}
    }


    public static void drawBlockBox(AxisAlignedBB box, final float r, final float g, final float b, final float a) {
        try {
            glSetup();
            final double[] rPos = rPos();
            box = new AxisAlignedBB(box.minX - rPos[0], box.minY - rPos[1], box.minZ - rPos[2], box.maxX - rPos[0], box.maxY - rPos[1], box.maxZ - rPos[2]);
            RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5f);
            glCleanup();
        }
        catch (Exception ex) {}
    }

    public static void drawText(final BlockPos pos, final String text) {

        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (EntityPlayer)mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        mc.fontRenderer.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        GlStateManager.popMatrix();
    }

    public static void drawNametag(final EntityPlayer player) {
        final float x = (float)player.posX;
        final float y = (float)player.posY;
        final float z = (float)player.posZ;

        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(x,  y + 0.5f, z, mc.player, 1.0f); // + 0.5f
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(player.getName()) / 2.0), 0.0, 0.0);
        drawDetailedNametag(0.0f, 0.0f, player);
        GlStateManager.popMatrix();
    }

    public static void glBillboard(final float x, final float y, final float z) {
        final float scale = 0.02666667f;
        final double[] rPos = rPos();
        try {
            GlStateManager.translate(
                    x - rPos[0],
                    y - rPos[1],
                    z - rPos[2]);
        } catch (Exception e) {}
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        glBillboard(x, y, z);
        final int distance = (int)player.getDistance(x, y, z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glSetup() {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0f);
    }

    public static void glCleanup() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    public static void drawDetailedNametag(final float xxxx, final float yyyy, EntityPlayer l_Player){

        final float x = xxxx - 20;
        final float y = yyyy - 20;

        int p;
        if (mc.getConnection() == null || Objects.requireNonNull(ModuleManager.getModuleByName("FakePlayer")).isToggled()) {
            p = -1;
        } else {
            p = mc.getConnection().getPlayerInfo(l_Player.getName()).getResponseTime();
        }
        final String Name = l_Player.getName();
        final int Healt = Math.round(l_Player.getHealth() + l_Player.getAbsorptionAmount());
        final String Health = "" + Healt;
        final ItemStack inHand = l_Player.getHeldItemMainhand();
        final String Ping = "" + p;
        int colour = 0;
        try {
            colour = l_Player.getHealth() + l_Player.getAbsorptionAmount() > 20.0F ? 2158832 : MathHelper.hsvToRGB((l_Player.getHealth() + l_Player.getAbsorptionAmount()) / 20.0F / 3.0F, 1.0F, 1.0F);
        } catch (Exception ignored) {}

        if (FriendManager.get().isFriend(l_Player.getName().toLowerCase())) {
            mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + Name + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + ChatFormatting.RESET + Health, x + 20, y - 45, colour);
        } else {
            mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + Name + " | " + Ping + "ms" + " | " + ChatFormatting.RESET + Health, x + 20, y - 45, colour);
        }


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
            mc.getRenderItem().renderItemAndEffectIntoGUI(is, xx, yy);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, is, xx, yy);
            RenderHelper.disableStandardItemLighting();
            i += 18;
        }
        final int yy2 = (int) (y - 35.0);
        final int xx2 = (int) (x + 90.0);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(inHand, xx2, yy2);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, inHand, xx2, yy2);
        RenderHelper.disableStandardItemLighting();
        final ItemStack inOffHand = l_Player.getHeldItemOffhand();
        final int yyy = (int) (y + -35.0);
        final int xxx = (int) (x + 110.0);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(inOffHand, xxx, yyy);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, inOffHand, xxx, yyy);
        RenderHelper.disableStandardItemLighting();

    }
}
