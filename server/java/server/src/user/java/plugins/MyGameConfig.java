package plugins;

import com.typesafe.config.Config;
import io.gamemachine.core.Plugin;
import io.gamemachine.unity.unity_engine.RegionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/4/2016.
 */
public class MyGameConfig {

    public static List<RegionData> regions = new ArrayList<>();

    static {

        addRegion("default",2000);
    }

    private static void addRegion(String region, int size) {
        RegionData data = new RegionData();
        data.region = region;
        data.size = size;
        regions.add(data);
    }
}
