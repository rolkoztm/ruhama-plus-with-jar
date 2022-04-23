package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.gui.ruhama.NewRuhamaGui;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;


import java.util.Arrays;
import java.util.List;

public class ClickGui extends Module
{
    public static NewRuhamaGui clickGui = new NewRuhamaGui();

    public ClickGui()
    {
        super("ClickGui", 0, Category.GUI, "Clickgui", settings);
    }

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingMode("GUI: ","Ruhama", "Guinness")
    );

    public void onEnable()
    {
        this.mc.displayGuiScreen(clickGui);
        this.setToggled(false);
    }
}
