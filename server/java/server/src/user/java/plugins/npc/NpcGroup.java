package plugins.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 3/3/2016.
 */
public class NpcGroup {
    public Map<String,Npc> npcs = new ConcurrentHashMap<>();
    public Npc leader;
}
