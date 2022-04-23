package blu3.ruhamaplus.utils;

import blu3.ruhamaplus.gui.ruhama.ModuleWindow;
import blu3.ruhamaplus.gui.ruhama.NewRuhamaGui;
import blu3.ruhamaplus.gui.ruhama.TextWindow;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.gui.ClickGui;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileMang {
    private static Path dir;

    public static void init() {
        dir = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "blu3", "ruhama+/");

        if (!dir.toFile().exists()) dir.toFile().mkdirs();
    }

    public static void loadFriends() {
        FriendManager.get().load();
    }

    public static List<String> readFileLines(String... file) {
        try {
            return Files.readAllLines(stringsToPath(file));
        } catch (IOException e) {
            System.out.println("Error Reading File: " + stringsToPath(file));
            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    public static void createFile(String... file) {
        try {
            if (fileExists(file)) {
                return;
            }

            dir.toFile().mkdirs();
            Files.createFile(stringsToPath(file));
        } catch (IOException e) {
            System.out.println("Error Creating File: " + Arrays.toString(file));
            e.printStackTrace();
        }
    }

    public static void createEmptyFile(String... file) {
        try {
            dir.toFile().mkdirs();
            if (!fileExists(file)) {
                Files.createFile(stringsToPath(file));
            }

            FileWriter writer = new FileWriter(stringsToPath(file).toFile());
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error Clearing/Creating File: " + Arrays.toString(file));
            e.printStackTrace();
        }

    }

    public static void appendFile(String content, String... file) {
        try {
            FileWriter writer = new FileWriter(stringsToPath(file).toFile(), true);
            writer.write(content + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error Appending File: " + Arrays.toString(file));
            e.printStackTrace();
        }
    }

    public static boolean fileExists(String... file) {
        try {
            return stringsToPath(file).toFile().exists();
        } catch (Exception e) {
            return false;
        }
    }

    public static Path stringsToPath(String... strings) {
        Path path = dir;

        for (String s : strings) {
            path = path.resolve(s);
        }

        return path;
    }

    public static void saveSettings() {
        createEmptyFile("settings.blu3");
        String lines = "";

        StringBuilder line;

        for (Iterator<?> iter = ModuleManager.getModules().iterator(); iter.hasNext(); lines = lines + line + "\n") {
            Module m = (Module) iter.next();
            line = new StringBuilder(m.getName());
            int count = 0;

            for (Iterator<?> settingsIter = m.getSettings().iterator(); settingsIter.hasNext(); ++count) {
                SettingBase set = (SettingBase) settingsIter.next();
                if (set instanceof SettingSlider) {
                    line.append(":").append(m.getSettings().get(count).asSlider().getValue());
                }

                if (set instanceof SettingMode) {
                    line.append(":").append(m.getSettings().get(count).asMode().mode);
                }

                if (set instanceof SettingToggle) {
                    line.append(":").append(m.getSettings().get(count).asToggle().state);
                }
            }
        }

        appendFile(lines, "settings.blu3");
    }

    public static void readSettings() {
        List<String> lines = readFileLines("settings.blu3");
        Iterator<?> modulesIter = ModuleManager.getModules().iterator();

        fern:
        while (modulesIter.hasNext()) {
            Module m = (Module) modulesIter.next();
            Iterator<?> linesIter = lines.iterator();

            while (true) {
                String[] line;

                do {
                    if (!linesIter.hasNext()) {
                        continue fern;
                    }

                    String s = (String) linesIter.next();
                    line = s.split(":");
                } while (!line[0].startsWith(m.getName()));

                int count = 0;

                for (Iterator<?> anotherIter = m.getSettings().iterator(); anotherIter.hasNext(); ++count) {
                    SettingBase set = (SettingBase) anotherIter.next();

                    try {
                        if (set instanceof SettingSlider) {
                            m.getSettings().get(count).asSlider().value = Double.parseDouble(line[count + 1]);
                        }

                        if (set instanceof SettingMode) {
                            m.getSettings().get(count).asMode().mode = Integer.parseInt(line[count + 1]);
                        }

                        if (set instanceof SettingToggle) {
                            m.getSettings().get(count).asToggle().state = Boolean.parseBoolean(line[count + 1]);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    public static void saveClickGui() {
        createEmptyFile("clickgui.blu3");
        String text = "";

        ModuleWindow w;

        for (Iterator<?> neverAgainFernFlower = ClickGui.clickGui.tabs.iterator(); neverAgainFernFlower.hasNext(); text = text + w.getPos()[0] + ":" + w.getPos()[1] + "\n") {
            w = (ModuleWindow) neverAgainFernFlower.next();
        }

        appendFile(text, "clickgui.blu3");
        createEmptyFile("clickguitext.blu3");
        String text2 = "";

        MutableTriple<?, ?, ?> e;

        for (Iterator<?> textIter = NewRuhamaGui.textWins.iterator(); textIter.hasNext(); text2 = text2 + ((Module) e.left).getName() + ":" + e.middle + ":" + ((TextWindow) e.getRight()).posX + ":" + ((TextWindow) e.getRight()).posY + "\n") {
            e = (MutableTriple<?, ?, ?>) textIter.next();
        }

        appendFile(text2, "clickguitext.blu3");
    }

    public static void readClickGui() {
        List<String> lines = readFileLines("clickgui.blu3");

        try {
            int c = 0;

            for (Iterator<?> iter = ClickGui.clickGui.tabs.iterator(); iter.hasNext(); ++c) {
                ModuleWindow w = (ModuleWindow) iter.next();
                w.setPos(Integer.parseInt((lines.get(c)).split(":")[0]), Integer.parseInt((lines.get(c)).split(":")[1]));
            }
        } catch (Exception ignored) {
        }

        for (String s : readFileLines("clickguitext.blu3")) {
            String[] split = s.split(":");

            for (MutableTriple<Module, Integer, TextWindow> moduleIntegerTextWindowMutableTriple : NewRuhamaGui.textWins) {
                try {
                    if (moduleIntegerTextWindowMutableTriple.left.getName().equals(split[0]) && moduleIntegerTextWindowMutableTriple.middle.equals(Integer.parseInt(split[1]))) {
                        moduleIntegerTextWindowMutableTriple.right.posX = Integer.parseInt(split[2]);
                        (moduleIntegerTextWindowMutableTriple.right).posY = Integer.parseInt(split[3]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveModules() {
        createEmptyFile("modules.blu3");
        StringBuilder lines = new StringBuilder();

        for (Module m : ModuleManager.getModules()) {
            if (!m.getName().equals("ClickGui")) {
                lines.append(m.getName()).append(":").append(m.isToggled()).append("\n");
            }
        }

        appendFile(lines.toString(), "modules.blu3");
    }


    public static void readModules() {
        List<String> lines = readFileLines("modules.blu3");

        for (Module m : ModuleManager.getModules()) {
            for (String s : lines) {
                String[] line = s.split(":");

                try {
                    if (line[0].contains(m.getName()) && line[1].contains("true")) {
                        m.toggle();
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }


    public static void saveBinds() {
        createEmptyFile("binds.blu3");
        String lines = "";

        Module m;

        for (Iterator<?> iter = ModuleManager.getModules().iterator(); iter.hasNext(); lines = lines + m.getName() + ":" + m.getKey().getKeyCode() + "\n") {
            m = (Module) iter.next();
        }

        appendFile(lines, "binds.blu3");
    }

    public static void readBinds() {
        List<String> lines = readFileLines("binds.blu3");

        for (Module m : ModuleManager.getModules()) {
            for (String s : lines) {
                String[] line = s.split(":");
                if (line[0].startsWith(m.getName())) {
                    try {
                        m.getKey().setKeyCode(Integer.parseInt(line[line.length - 1]));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

}
