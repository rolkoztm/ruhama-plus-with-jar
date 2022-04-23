package blu3.ruhamaplus.settings;

public class SettingBase
{

    public SettingMode asMode()
    {
        try
        {
            return (SettingMode) this;
        } catch (Exception e)
        {
            System.out.println("Unable To Parse Setting");

            return new SettingMode("PARSING ERROR");
        }
    }

    public SettingToggle asToggle()
    {
        try
        {
            return (SettingToggle) this;
        } catch (Exception e)
        {
            System.out.println("Unable To Parse Setting");

            return new SettingToggle(false, "PARSING ERROR");
        }
    }

    public SettingSlider asSlider()
    {
        try
        {
            return (SettingSlider) this;
        } catch (Exception e)
        {
            System.out.println("Unable To Parse Setting");

            return new SettingSlider(0.0D, 1.0D, 0.0D, 0, "PARSING ERROR");
        }
    }
}
