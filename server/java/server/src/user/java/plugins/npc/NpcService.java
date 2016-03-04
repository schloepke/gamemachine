package plugins.npc;

import com.google.common.base.Strings;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.*;
import io.gamemachine.messages.Character;
import io.gamemachine.regions.ZoneService;
import io.gamemachine.unity.unity_engine.RegionData;
import plugins.MyGameConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 3/4/2016.
 */
public class NpcService {

    private static int generateCount = 20000;
    private static HashSet<String> names = new HashSet<>();

    private static Random rand = new Random();
    private CharacterService cs;
    private PlayerService ps;
    private List<NpcName> takenNames = new ArrayList<>();
    private List<NpcName> freeNames = new ArrayList<>();
    private Map<String,List<Npc>> npcs = new ConcurrentHashMap<>();
    private int nameCount;

    private NpcService() {
        NpcStaticNames.loadNames();
        ps = PlayerService.getInstance();
        cs = CharacterService.instance();

        List<NpcName> npcNames = NpcName.db().findAll();
        nameCount = npcNames.size();
        for (NpcName npcName : npcNames) {
            if (npcName.taken) {
                takenNames.add(npcName);
            } else {
                freeNames.add(npcName);
            }
        }

        if (nameCount < generateCount) {
            generateNames();
        }

        populateRegions();
    }

    private static class LazyHolder {
        private static final NpcService INSTANCE = new NpcService();
    }

    public static NpcService instance() {
        return LazyHolder.INSTANCE;
    }

    private void generateNames() {
        while (nameCount < generateCount) {
            String firstName = getFirstname();
            String lastName = getLastname();
            String fullName = firstName+" "+lastName;;

            if (!names.contains(fullName)) {
                names.add(fullName);

                NpcName npcName = NpcName.db().findFirst("npc_name_id = ?", fullName);
                if (npcName != null) {
                    continue;
                }

                npcName = new NpcName();
                npcName.id = fullName;
                npcName.firstName = firstName;
                npcName.lastName = lastName;
                NpcName.db().save(npcName);
                freeNames.add(npcName);
                nameCount++;
            }
        }
    }

    private void populateRegions() {
        for (RegionData data : MyGameConfig.regions) {
            createForRegion(data.region,(int)data.size);
        }
    }

    public void createForRegion(String region, int count) {
        if (!npcs.containsKey(region)) {
            npcs.put(region, new ArrayList<>());
        }

        List<Character> characters = Character.db().where("character_region = ?", region);
        for (Character character : characters) {
            Npc npc = new Npc();
            npc.character = character;
            npcs.get(region).add(npc);
        }
        if (npcs.size() >= count) {
            return;
        }

        while (npcs.get(region).size() < count) {
            Npc npc = getNpc("region",region);
            npcs.get(region).add(npc);
        }

    }

    public Npc getNpc(String ownerId, String region) {

        NpcName npcName = freeNames.remove(0);
        takenNames.add(npcName);

        String characterId = npcName.id;
        String playerId = ownerId+"_"+characterId;

        Character character = cs.find(playerId, characterId);
        if (character != null) {
            throw new RuntimeException("Duplicate character "+characterId);
        }

        Player player = ps.find(playerId);
        if (player != null) {
            throw new RuntimeException("Duplicate player "+playerId);
        }

        player = ps.create(playerId, AppConfig.getDefaultGameId());
        character = cs.create(playerId, characterId, Vitals.Template.NpcTemplate, null);

        character.ownerId = ownerId;
        character.gameEntityPrefab = "default";
        character.firstName = npcName.firstName;
        character.lastName = npcName.lastName;

        cs.save(character);

        ps.setCharacter(playerId, characterId);
        PlayerService.getInstance().setZone(playerId, ZoneService.defaultZone());

        Npc npc = new Npc();
        npc.character = character;
        return npc;
    }

    private static String getFirstname() {
        return NpcStaticNames.syl2.get(rand.nextInt(NpcStaticNames.syl2.size()));
    }

    private static String getLastname() {
        return NpcStaticNames.syl3.get(rand.nextInt(NpcStaticNames.syl3.size()));
    }

}
