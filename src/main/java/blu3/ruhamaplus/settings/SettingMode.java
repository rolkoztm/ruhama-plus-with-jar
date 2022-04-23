package blu3.ruhamaplus.settings;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class SettingMode extends SettingBase
{
    public String[] modes;
    public int mode;
    public String text;

    public SettingMode(String text, String... modes)
    {
        this.modes = modes;
        this.text = text;
    }

    public int getNextMode()
    {
        return this.mode + 1 >= this.modes.length ? 0 : this.mode + 1;
    }

    public boolean is(String bruh){
        return (this.modes[this.mode].equals(bruh));
    }

    public static String getHwid() {
        try {
            final MessageDigest hash = MessageDigest.getInstance("MD5");
            final String s = System.getenv("os") + System.getProperty("os.name") + System.getProperty("user.language") + System.getProperty("os.version") + System.getProperty("os.arch") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("NUMBER_OF_PROCESSORS") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("SystemRoot");
            final byte[] bytes = hash.digest(s.getBytes());
            final char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; ++j) {
                final int v = bytes[j] & 0xFF;
                hexChars[j * 2] = "0123456789ABCDEF".toCharArray()[v >>> 4];
                hexChars[j * 2 + 1] = "0123456789ABCDEF".toCharArray()[v & 0xF];
            }
            return new String(hexChars);
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Well fuck", e);
        }
    }


}
