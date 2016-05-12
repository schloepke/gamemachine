package io.gamemachine.unity.unity_engine;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.*;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.*;
import io.gamemachine.net.Connection;
import io.gamemachine.unity.UnityMessageHandler;
import org.apache.commons.codec.binary.Base64;
import plugins.npc.LandrushNpc;

import java.util.HashSet;
import java.util.List;

/**
 * Created by chris on 3/3/2016.
 */
public class NpcManager extends GameMessageActor {

    public static String name = NpcManager.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private long updateInterval = 5000L;
    private NpcService npcService;

    //private static Map<String, List<ActorRef>> regionActors = new ConcurrentHashMap<>();
    //private static Map<String, List<Npc>> regionNpcs = new ConcurrentHashMap<>();
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
        if (gameMessage.npcRequest != null) {
            Zone zone = PlayerService.getInstance().getZone(playerId);
            RegionData regionData = RegionData.getRegionByZone(zone.name);

            Npc npc = regionData.getNpc(gameMessage.npcRequest.characterId);
            ActorSelection sel = ActorUtil.getSelectionByName(npc.getActorName());
            sel.tell(gameMessage.npcRequest,null);
        }
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

    private static void saveGroupData(NpcGroupConfig groupData) {
        NpcGroupDatas datas = new NpcGroupDatas();
        datas.data = Base64.encodeBase64String(groupData.toByteArray());
        datas.id = groupData.id;
        NpcGroupDatas.db().save(datas);
    }

    private static NpcGroupConfig getNpcGroupConfig(String id) {
        NpcGroupDatas datas = NpcGroupDatas.db().findFirst("npc_group_datas_id = ?",id);
        if (datas == null) {
            return null;
        }

        byte[] bytes = Base64.decodeBase64(datas.data);
        NpcGroupConfig groupConfig = NpcGroupConfig.parseFrom(bytes);
        return groupConfig;
    }

    public static void saveSpawnGroup(GroupSpawnRequest request) {
        NpcGroupConfig groupData = request.npcGroupConfig;

        NpcGroupConfig existing = getNpcGroupConfig(groupData.id);
        if (existing == null) {
            groupData.region = request.region;
            saveGroupData(groupData);
        }
    }


    private static void activateNpcGroups() {
        List<NpcGroupDatas> datasList = NpcGroupDatas.db().findAll();
        for (NpcGroupDatas datas : datasList) {
            byte[] bytes = Base64.decodeBase64(datas.data);
            NpcGroupConfig groupConfig = NpcGroupConfig.parseFrom(bytes);
            spawnNpcGroup(groupConfig);
        }
    }

    private static void spawnNpcGroup(NpcGroupConfig groupConfig) {
        RegionData data = RegionData.getRegionData(groupConfig.region);
        if (data.status == RegionData.Status.Inactive) {
            return;
        }
        if (spawnGroups.contains(groupConfig.id)) {
            return;
        }
        spawnGroups.add(groupConfig.id);

        NpcGroup group = new NpcGroup();
        for (NpcConfig npcConfig : groupConfig.npcConfig) {

            Npc npc = NpcService.instance().getNpc(groupConfig.region);

            npc.faction = groupConfig.faction;
            CharacterService.instance().setFaction(npc.id,npc.faction);
            CharacterService.instance().setPrefab(npc.id,npcConfig.prefab);

            npcConfig.id = npc.id;

            if (groupConfig.leader.equals(npcConfig.referenceId)) {
                group.leader = npc;
            } else {
                group.members.add(npc);
            }
            npc.group = group;

            spawnNpc(npc, data);
        }

        GroupSpawnRequest request = new GroupSpawnRequest();
        request.npcGroupConfig = groupConfig;
        request.region = groupConfig.region;
        sendSpawnRequest(request);
    }

    private static void spawnNpc(Npc npc, RegionData regionData) {

        ActorRef ref = GameMachineLoader.getActorSystem().actorOf(Props.create(LandrushNpc.class, npc.id,regionData.region), npc.getActorName());

        regionData.addNpcActor(npc.id,ref);
        regionData.addNpc(npc);
    }

    private void setRegionInactive(RegionData regionData) {
        logger.warning("De-Activating region "+regionData.region);
        spawnGroups.clear();

        for (ActorRef ref : regionData.getNpcActors()) {
            GameMachineLoader.getActorSystem().stop(ref);
        }
        regionData.clearNpcActors();

        for (Npc npc : regionData.getNpcs()) {
            NpcService.instance().releaseNpc(regionData.region,npc);
        }
        regionData.clearNpcs();

        RegionData.setRegionStatus(regionData.region, RegionData.Status.Inactive);

        GridService.getInstance().removeForZone(regionData.region);
    }

    private void setRegionActive(RegionData regionData) {
        logger.warning("Activating region "+regionData.region);

        GridService.getInstance().removeForZone(regionData.region);
        GridService.getInstance().createForZone(regionData.region);

        RegionData.setRegionStatus(regionData.region, RegionData.Status.Active);

        activateNpcGroups();
        //Npc npc = NpcService.instance().getNpc(regionData.region);
        //ActorRef ref = GameMachineLoader.getActorSystem().actorOf(Props.create(FakePlayer.class, npc.id,regionData.region), npc.getActorName());
        //regionActors.get(regionData.region).add(ref);
    }


}
