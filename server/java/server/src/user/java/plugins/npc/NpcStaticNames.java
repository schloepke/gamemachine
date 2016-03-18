package plugins.npc;

import io.gamemachine.core.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chris on 3/4/2016.
 */
public class NpcStaticNames {

    public static List<String> lastNames = new ArrayList<>();
    public static List<String> firstNames = new ArrayList<>();
    private static Random rand = new Random();

    static {
        loadNames();
    }

    private static void loadNames() {
        String lastNamesFile = Plugin.pluginPath+File.separator+"npc"+File.separator+"lastNames.txt";
        loadFile("lastNames",lastNamesFile);

        String firstNamesFile = Plugin.pluginPath+File.separator+"npc"+File.separator+"firstNames.txt";
        loadFile("firstNames",firstNamesFile);
    }

    public static String getFirstname() {
        return NpcStaticNames.firstNames.get(rand.nextInt(NpcStaticNames.firstNames.size()));
    }

    public static String getLastname() {
        return NpcStaticNames.lastNames.get(rand.nextInt(NpcStaticNames.lastNames.size()));
    }

    private static void loadFile(String name, String path) {
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line.replace("\n", "").replace("\r", "");

                if(name.equals("firstNames")) {
                    firstNames.add(line);
                } else {
                    lastNames.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
