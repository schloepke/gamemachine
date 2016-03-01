package plugins.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.messages.FactionStanding;
import io.gamemachine.messages.FactionStandings;
import io.gamemachine.messages.Factions;
import plugins.core.combat.ClientDbLoader;
import io.gamemachine.core.CharacterService;
import io.gamemachine.messages.Character;

public class FactionService {

    private final Logger logger = LoggerFactory.getLogger(FactionService.class);
    private CharacterService cs;

    private ConcurrentHashMap<String, FactionStanding> factionStandings = new ConcurrentHashMap<String, FactionStanding>();


    private FactionService() {
        cs = CharacterService.instance();
        saveClientCatalog();
        loadStandings();
    }

    private static class LazyHolder {
        private static final FactionService INSTANCE = new FactionService();
    }

    public static FactionService instance() {
        return LazyHolder.INSTANCE;
    }

    public void saveStanding(FactionStanding standing) {
        FactionStanding.db().save(standing);
        loadStanding(standing);
    }

    public FactionStandings getFactionStandings() {
        FactionStandings standings = new FactionStandings();
        standings.standings = new ArrayList(factionStandings.values());
        return standings;
    }

    public FactionStandings getFactionStandings(String characterId) {
        FactionStandings standingsContainer = new FactionStandings();
        List<FactionStanding> standings = new ArrayList<FactionStanding>();
        for (FactionStanding standing : factionStandings.values()) {
            if (!Strings.isNullOrEmpty(standing.meCharacterId) && standing.meCharacterId.equals(characterId)) {
                standings.add(standing);
            } else if (standing.type == FactionStanding.Type.FactionToFaction) {
                standings.add(standing);
            }
        }
        standingsContainer.standings = standings;
        return standingsContainer;
    }

    public FactionStanding getStanding(String meId, String themId) {
        String key = getKey(meId, themId);
        FactionStanding standing;

        if (factionStandings.containsKey(key)) {
            standing = factionStandings.get(key);
        } else {
            Character me = cs.find(meId);
            Character them = cs.find(themId);

            String qs = "faction_standing_type = ? ANd faction_standing_me_character_id = ? ANd faction_standing_them_character_id = ?";
            standing = FactionStanding.db().findFirst(qs, FactionStanding.Type.CharacterToCharacter.getNumber(), me.id, them.id);
            if (standing == null) {
                standing = new FactionStanding();
                standing.id = UUID.randomUUID().toString();
                standing.type = FactionStanding.Type.CharacterToCharacter;
                standing.meCharacterId = me.id;
                standing.standing = getStanding(me.faction, them.faction).standing;
            }
        }
        return standing;
    }

    public FactionStanding getStanding(String meId, Factions.Faction them) {
        String key = getKey(meId, them);
        FactionStanding standing;

        if (factionStandings.containsKey(key)) {
            standing = factionStandings.get(key);
        } else {
            Character me = cs.find(meId);

            String qs = "faction_standing_type = ? ANd faction_standing_me_character_id = ? ANd faction_standing_them = ?";
            standing = FactionStanding.db().findFirst(qs, FactionStanding.Type.CharacterToFaction.getNumber(), me.id, them.getNumber());
            if (standing == null) {
                standing = new FactionStanding();
                standing.id = UUID.randomUUID().toString();
                standing.type = FactionStanding.Type.CharacterToCharacter;
                standing.meCharacterId = me.id;
                standing.standing = getStanding(me.faction, them).standing;
            }
        }
        return standing;
    }

    public FactionStanding getStanding(Factions.Faction me, Factions.Faction them) {
        String key = getKey(me, them);

        if (factionStandings.containsKey(key)) {
            return factionStandings.get(key);
        } else {
            throw new RuntimeException("No such factionStanding " + me.toString() + " -> " + them.toString());
        }
    }

    private void loadStanding(FactionStanding standing) {
        String key = null;

        if (standing.type == FactionStanding.Type.CharacterToCharacter) {
            key = getKey(standing.meCharacterId, standing.themCharacterId);
        } else if (standing.type == FactionStanding.Type.CharacterToFaction) {
            key = getKey(standing.meCharacterId, standing.them);
        } else if (standing.type == FactionStanding.Type.FactionToFaction) {
            key = getKey(standing.me, standing.them);
        } else {
            throw new RuntimeException("Invalid faction type " + standing.type.toString());
        }
        factionStandings.put(key, standing);
    }

    private String getKey(String meId, String themId) {
        return FactionStanding.Type.CharacterToCharacter.getNumber() + "_" + meId + "_" + themId;
    }

    private String getKey(String meId, Factions.Faction them) {
        return FactionStanding.Type.CharacterToFaction.getNumber() + "_" + meId + "_" + them.getNumber();
    }

    private String getKey(Factions.Faction me, Factions.Faction them) {
        return FactionStanding.Type.FactionToFaction.getNumber() + "_" + me.getNumber() + "_" + them.getNumber();
    }

    private void saveClientCatalog() {
        FactionStandings standings = ClientDbLoader.getFactionStandings();
        for (FactionStanding standing : standings.standings) {
            FactionStanding.db().save(standing);
        }
    }

    private void loadStandings() {
        List<FactionStanding> standings = FactionStanding.db().findAll();
        for (FactionStanding standing : standings) {
            loadStanding(standing);
        }
    }

}
