package blu3.ruhamaplus.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CapeUtils {
    List<String> thing = new ArrayList<>();

    public CapeUtils(){
        try {
            URL pastebin = new URL("https://pastebin.com/raw/NEwURTmB");
            BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                thing.add(inputLine);
            }
        } catch(Exception e){}
    }

    public boolean isCape(String e) {
        return thing.contains(e);
    }
}
