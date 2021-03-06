package blu3.ruhamaplus.settings;

import java.math.RoundingMode;
import java.math.BigDecimal;

public class SettingSlider extends SettingBase
{
    public double min;
    public double max;
    public double value;
    public int round;

    public String text;

    public SettingSlider(double min, double max, double value, int round, String text)
    {
        this.min = min;
        this.max = max;
        this.value = value;
        this.round = round;
        this.text = text;
    }

    public double getValue()
    {
        return this.round(this.value, this.round);
    }

    public double round(double value, int places)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
