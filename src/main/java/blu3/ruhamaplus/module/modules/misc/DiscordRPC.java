/*
package blu3.ruhamaplus.module.modules.misc;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.Discord;
import blu3.ruhamaplus.utils.Wrapper;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;

public class DiscordRPC extends Module{
    public DiscordRPC() {
        super("DiscordRPC", 0, Category.MISC, "are pee see honhonhon", settings);
    }

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingMode("Details: ","ServerIP", "Username"),
            new SettingMode("Else: ","blu3", "Private LMAO", "Currently Gaming")


    );

    @Override
    public void onEnable()
    {
        super.onEnable();

        Discord.Get().enable();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        try
        {
            Discord.Get().disable();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public String generateDetails()
    {
        String result = null;

        if (result == null)
            result = "";

        if (this.getSetting(0).asMode().mode == 0)
            result = (Wrapper.GetMC().getCurrentServerData() != null ? Wrapper.GetMC().getCurrentServerData().serverIP : "Offline") + " " + result;

        if (this.getSetting(0).asMode().mode == 1)
            result = Wrapper.GetMC().getSession().getUsername();

        return result;
    }
    public String generateState()
    {
        if (mc.player == null)
            return "Currently choosing where to game";

        if (this.getSetting(1).asMode().mode == 0)
        {
            return "blu3 is a shit dev";
        }

        if (this.getSetting(1).asMode().mode == 1)
        {
            return "the client is private LMAO";
        }
        if (this.getSetting(1).asMode().mode == 2)
        {
            return "Currently gaming";
        }

        String result = "";
        return result;
    }
}
*/
