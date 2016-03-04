package plugins.npc;

import io.gamemachine.core.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/4/2016.
 */
public class NpcStaticNames {

    public static List<String> syl3 = new ArrayList<>();
    public static List<String> syl2 = new ArrayList<>();

    public static void loadNames() {
        String syl3File = Plugin.pluginPath+File.separator+"npc"+File.separator+"syl3.txt";
        loadFile("syl3",syl3File);

        String syl2File = Plugin.pluginPath+File.separator+"npc"+File.separator+"syl2.txt";
        loadFile("syl2",syl2File);
    }

    private static void loadFile(String name, String path) {
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line.replace("\n", "").replace("\r", "");

                if(name.equals("syl2")) {
                    syl2.add(line);
                } else {
                    syl3.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
