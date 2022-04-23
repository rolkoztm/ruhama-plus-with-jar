package blu3.ruhamaplus.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils implements Util
{

    public static void setWords(){
        sialydf.addAll(FileMang.readFileLines("cleanchat.blu3"));
    }
    public static void log(String text)
    {
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.AQUA + "[Ruhama+]: " + TextFormatting.RESET + text));
    }

    public static String toUnicode(String s){
        return s.toLowerCase()
                .replace("a", "\u1d00")
                .replace("b", "\u0299")
                .replace("c", "\u1d04")
                .replace("d", "\u1d05")
                .replace("e", "\u1d07")
                .replace("f", "\ua730")
                .replace("g", "\u0262")
                .replace("h", "\u029c")
                .replace("i", "\u026a")
                .replace("j", "\u1d0a")
                .replace("k", "\u1d0b")
                .replace("l", "\u029f")
                .replace("m", "\u1d0d")
                .replace("n", "\u0274")
                .replace("o", "\u1d0f")
                .replace("p", "\u1d18")
                .replace("q", "\u01eb")
                .replace("r", "\u0280")
                .replace("s", "\ua731")
                .replace("t", "\u1d1b")
                .replace("u", "\u1d1c")
                .replace("v", "\u1d20")
                .replace("w", "\u1d21")
                .replace("x", "\u02e3")
                .replace("y", "\u028f")
                .replace("z", "\u1d22");
    }

    public static void warn(String text)
    {
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.YELLOW + "[Ruhama+: WARN]: " + TextFormatting.RESET + text));
    }

    public static void error(Object location, String text){
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.RED + "[Ruhama+: ERROR]: " + TextFormatting.RESET + "at " + TextFormatting.RED + location + ": " + TextFormatting.RESET + text));
    }

    public static List<String> sialydf = new ArrayList<>();

    public static void cleanChatMessage(String message) {

        StringBuilder cleanMessage = new StringBuilder();

        String[] arr = message.split(" ", 256); // char limit for chat
        for (String bruh : arr) {
            if (sialydf.contains(bruh.toLowerCase())) {
                for (int i = 0; i < bruh.length(); i++){
                    cleanMessage.append("#");
                }
                cleanMessage.append(" ");
            } else {
                cleanMessage.append(bruh).append(" ");
            }
        }
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(cleanMessage.toString() + " (cleaned)"));
    }
}
