package io.gamemachine.unity.unity_engine;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.*;
import io.gamemachine.messages.Character;
import io.gamemachine.regions.ZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 3/4/2016.
 */
public class NpcService {

    private static final Logger logger = LoggerFactory.getLogger(NpcService.class);
    private HashSet<String> taken = new HashSet<>();
    private CharacterService cs;
    private PlayerService ps;
    private Map<String,ArrayBlockingQueue<Npc>> npcs = new ConcurrentHashMap<>();

    private NpcService() {
        ps = PlayerService.getInstance();
        cs = CharacterService.instance();

        for (Character character : Character.db().findAll()) {
            taken.add(character.id);
        }

        populateRegions();
    }

    private static class LazyHolder {
        private static final NpcService INSTANCE = new NpcService();
    }

    public static NpcService instance() {
        return LazyHolder.INSTANCE;
    }


    private HashSet<NpcName> getNames(int count) {
        HashSet<NpcName> names = new HashSet<>();

        while(names.size() < count) {
            NpcName npcName = new NpcName();
            npcName.firstName = NpcStaticNames.getFirstname();
            npcName.lastName = NpcStaticNames.getLastname();
            npcName.id = npcName.firstName+" "+npcName.lastName;
            if (!taken.contains(npcName.id)) {
                names.add(npcName);
            }
        }

        return names;
    }

    private void populateRegions() {
        for (RegionData data : RegionData.regions.values()) {
            createForRegion(data.region,(int)data.size);
        }
    }

    public void createForRegion(String region, int count) {
        if (npcs.containsKey(region)) {
            return;
        } else {
            npcs.put(region, new ArrayBlockingQueue<>(count));
        }

        List<Character> characters = Character.db().where("character_owner_id = ?",region);
        for (Character character : characters) {
            Npc npc = new Npc();
            npc.id = character.id;
            npcs.get(region).add(npc);
        }

        int remaining = count - characters.size();
        if (remaining == 0) {
            return;
        }

        HashSet<NpcName> npcNames = getNames(remaining);
        for (NpcName npcName : npcNames) {
            Npc npc = createNpc(npcName,region);
            npcs.get(region).add(npc);
        }
        logger.warn("Npcs created for region "+region);
    }

    public Npc getNpc(String region) {
        return npcs.get(region).poll();
    }

    public void releaseNpc(String region, Npc npc) {
        npcs.get(region).add(npc);
    }

    public boolean npcExists(String characterId) {
        Character character = Character.db().findFirst("character_id = ?", characterId);
        return character != null;
    }

    public Npc createNpc(NpcName npcName, String ownerId) {

        Player player = ps.create(npcName.id, AppConfig.getDefaultGameId());
        Character character = cs.create(npcName.id, npcName.id, Vitals.Template.NpcTemplate, null);

        character.ownerId = ownerId;
        character.gameEntityPrefab = "default";
        character.firstName = npcName.firstName;
        character.lastName = npcName.lastName;

        cs.save(character);

        ps.setCharacter(npcName.id, npcName.id);
        PlayerService.getInstance().setZone(npcName.id, ZoneService.defaultZone());

        Npc npc = new Npc();
        npc.id = character.id;
        return npc;
    }

}
