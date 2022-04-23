package blu3.ruhamaplus.utils.friendutils;

public class Friend {
    public Friend(String p_Name, String p_Alias)
    {
        Name = p_Name;
        Alias = p_Alias;
    }

    private String Name;
    private String Alias;

    public void SetAlias(String p_Alias)
    {
        Alias = p_Alias;
    }

    public String GetName()
    {
        return Name;
    }

    public String GetAlias()
    {
        return Alias;
    }
}
