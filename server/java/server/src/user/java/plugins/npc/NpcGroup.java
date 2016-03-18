package plugins.npc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/12/2016.
 */
public class NpcGroup {
    public String id;
    public Npc leader;
    public List<Npc> members = new ArrayList<>();
}
