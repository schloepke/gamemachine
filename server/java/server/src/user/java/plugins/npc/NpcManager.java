package plugins.npc;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.*;
import io.gamemachine.net.Connection;
import io.gamemachine.unity.UnityMessageHandler;
import io.gamemachine.unity.unity_engine.RegionData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 3/3/2016.
 */
public class NpcManager extends GameMessageActor {

    public static String name = NpcManager.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private long updateInterval = 5000L;
    private NpcService npcService;

    private static Map<String, List<ActorRef>> regionActors = new ConcurrentHashMap<>();
    private static Map<String, List<Npc>> regionNpcs = new ConcurrentHashMap<>();
    private static HashSet<String> spawnGroups = new HashSet<>();

    public NpcManager() {
        npcService = NpcService.instance();
    }

    @Override
    public void awake() {
        scheduleOnce(updateInterval, "updateRegions");
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {

    }

    public void onTick(String message) {
        updateRegions();
        scheduleOnce(updateInterval, "updateRegions");
    }

    public void updateRegions() {
        for (RegionData regionData : RegionData.getRegions()) {
            logger.warning("Region "+regionData.region+" "+regionData.status);
            if (RegionData.isAlive(regionData.region) && regionData.status == RegionData.Status.Inactive) {
                setRegionActive(regionData);
            } else if (!RegionData.isAlive(regionData.region) && regionData.status == RegionData.Status.Active) {
                setRegionInactive(regionData);
            }
        }
    }

    public static void sendSpawnRequest(GroupSpawnRequest request) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.unityMessage = new UnityMessage();
        clientMessage.unityMessage.playerId = request.region;
        clientMessage.unityMessage.groupSpawnRequest = request;

        Connection connection = UnityMessageHandler.getConnection(request.region);
        if (connection != null) {
            connection.sendToClient(clientMessage);
        }
    }

    public static void spawn(GroupSpawnRequest request) {
        RegionData data = RegionData.getRegionData(request.region);
        if (data.status == RegionData.Status.Inactive) {
            return;
        }
        if (spawnGroups.contains(request.npcGroupData.referenceId)) {
            return;
        }
        spawnGroups.add(request.npcGroupData.referenceId);

        NpcGroup group = new NpcGroup();
        for (NpcData spawn : request.npcGroupData.npcData) {

            Npc npc = NpcService.instance().getNpc(request.region);

            npc.faction = request.npcGroupData.faction;
            CharacterService.instance().setFaction(npc.id,npc.faction);

            spawn.id = npc.id;

            if (request.npcGroupData.leader.equals(spawn.referenceId)) {
                group.leader = npc;
            } else {
                group.members.add(npc);
            }
            npc.group = group;

            spawnNpc(npc, data);
        }
        sendSpawnRequest(request);
    }


    private static void spawnNpc(Npc npc, RegionData regionData) {

        Npc.npcs.put(npc.id,npc);
        ActorRef ref = GameMachineLoader.getActorSystem().actorOf(Props.create(LandrushNpc.class, npc.id,regionData.region), npc.getActorName());


        regionActors.get(regionData.region).add(ref);

        if (!regionNpcs.containsKey(regionData.region)) {
            regionNpcs.put(regionData.region,new ArrayList<>());
        }
        regionNpcs.get(regionData.region).add(npc);

    }

    private void setRegionInactive(RegionData regionData) {
        logger.warning("De-Activating region "+regionData.region);
        spawnGroups.clear();

        if (regionActors.containsKey(regionData.region)) {
            for (ActorRef ref : regionActors.get(regionData.region)) {
                GameMachineLoader.getActorSystem().stop(ref);
            }
            regionActors.get(regionData.region).clear();
        }


        if (regionNpcs.containsKey(regionData.region)) {
            for (Npc npc : regionNpcs.get(regionData.region)) {
                NpcService.instance().releaseNpc(regionData.region,npc);
            }
            regionNpcs.get(regionData.region).clear();
        }


        RegionData.setRegionStatus(regionData.region, RegionData.Status.Inactive);

        GridService.getInstance().removeForZone(regionData.region);
    }

    private void setRegionActive(RegionData regionData) {
        logger.warning("Activating region "+regionData.region);

        GridService.getInstance().removeForZone(regionData.region);
        GridService.getInstance().createForZone(regionData.region);

        logger.warning("spawn group count "+regionData.gmNpcGroups.size());

        RegionData.setRegionStatus(regionData.region, RegionData.Status.Active);

        if (!regionActors.containsKey(regionData.region)) {
            regionActors.put(regionData.region,new ArrayList<>());
        }

        Npc npc = NpcService.instance().getNpc(regionData.region);
        ActorRef ref = GameMachineLoader.getActorSystem().actorOf(Props.create(FakePlayer.class, npc.id,regionData.region), npc.getActorName());
        regionActors.get(regionData.region).add(ref);
    }


}
