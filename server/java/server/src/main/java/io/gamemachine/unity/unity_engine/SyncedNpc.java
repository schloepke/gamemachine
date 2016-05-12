package io.gamemachine.unity.unity_engine;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.base.Strings;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.*;
import io.gamemachine.messages.Character;
import io.gamemachine.regions.ZoneService;
import io.gamemachine.unity.UnitySync;
import io.gamemachine.unity.unity_engine.engine_results.DestroyResult;
import io.gamemachine.unity.unity_engine.engine_results.InstantiateResult;
import io.gamemachine.unity.unity_engine.engine_results.UnityEngineResult;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;
import io.gamemachine.util.Mathf;
import plugins.core.combat.CombatHandler;
import plugins.core.combat.PlayerSkillHandler;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsProxy;
import plugins.npc.InpcCombatAi;
import scala.concurrent.duration.Duration;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by chris on 3/1/2016.
 */
public class SyncedNpc extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    protected State state;
    protected int worldOffset = 0;
    protected String id;
    protected Grid grid;
    protected Vector3 position = Vector3.zero;
    protected TrackData trackData;
    protected long runningInterval = 60l;
    protected long idleInterval = 1000L;
    protected long startingInterval = 1000L;
    protected long regionUpdateInterval = 5000L;
    protected long combatInterval = 1000L;
    protected long lastCombatUpdate;
    protected double speed = 5d;
    protected Vector3 moveTarget = Vector3.zero;
    protected Random rand;
    protected long lastUpdate = 0;
    protected VitalsProxy vitalsProxy;
    protected UnityEngine engine;
    protected String region;
    protected RegionData regionData;
    protected Npc npc;
    protected ArrayBlockingQueue<Vector3> path = new ArrayBlockingQueue<Vector3>(50);
    protected long lastPathRequest;
    protected boolean targetReached = false;
    protected InpcCombatAi combatAi;
    protected NpcSyncData npcSyncData;

    public enum State {
        Idle,
        Starting,
        Running
    }

    public SyncedNpc(String characterId, String region) {
        id = characterId;
        this.region = region;
        RegionData regionData = RegionData.getRegionData(region);
        npc = regionData.getNpc(id);

        rand = new Random();
        state = State.Idle;
        initTrackData();
        worldOffset = AppConfig.getWorldOffset();
        grid = RegionData.getPlayerGrid(region);
        vitalsProxy = VitalsHandler.get(id, ZoneService.defaultZone().name);

        engine = new UnityEngine(region, getSelf());
        UnitySync.registerHandler(getSelf(), id);
    }



    @Override
    public void postStop() {
        grid.remove(id);
        UnitySync.unregisterHandler(id);
        logger.warning("Npc stopping "+npc.id);
    }

    @Override
    public void preStart() {
        awake();
        tick();
        scheduleOnce(regionUpdateInterval, "regionUpdate");
    }

    protected void awake() {

    }

    public void regionUpdate() {
        regionData = RegionData.getRegionData(region);
        if (state == State.Idle) {
            if (engine.isAlive() && regionData != null) {
                setState(State.Starting);
            }
        } else {
            if (regionData == null) {
                setState(State.Idle);
            }
            if (!engine.isAlive()) {
                setState(State.Idle);
            }
        }
    }

    public void tick() {
        long interval;
        if (state == State.Idle) {
            interval = idleInterval;
        } else if (state == State.Running) {
            interval = runningInterval;
        } else if (state == State.Starting) {
            interval = startingInterval;
        } else {
            throw new IllegalArgumentException("Invalid state");
        }

        scheduleOnce(interval, state.toString());
    }

    public final void scheduleOnce(long delay, String message) {
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
                        null);
    }

    protected void setState(State state) {
        logger.warning("Transition "+this.state+" -> "+state);
        this.state = state;
    }

    protected void move() {

    }


    private void initTrackData() {
        trackData = new TrackData();
        trackData.id = id;
        trackData.entityType = TrackData.EntityType.Npc;
        trackData.userDefinedData = new UserDefinedData();
    }

    private void sendTrackData() {
        trackData.setX(Mathf.toInt(position.x + worldOffset));
        trackData.setY(Mathf.toInt(position.y + worldOffset));
        trackData.setZ(Mathf.toInt(position.z + worldOffset));

        grid.set(trackData);
    }


    private void doStarting() {
        //position = npc.position;
        //engine.instantiate("npc",id,npc.position, Quaternion.identity);

    }

    private void doIdle() {

    }

    private void doRunning() {
        if (vitalsProxy.isDead()) {
            vitalsProxy.setDeathTime(90000f);
            grid.remove(id);
            //engine.destroy(id);
            //sendTrackData();
            lastUpdate = System.currentTimeMillis();
            sendSync();
            return;
        }

        npc.position = position;
        //updateTarget();

        //move();

        lastUpdate = System.currentTimeMillis();
        sendTrackData();

       if (System.currentTimeMillis() - lastCombatUpdate > combatInterval) {
           combatUpdate();
           sendSync();
           lastCombatUpdate = System.currentTimeMillis();
       }
    }

    protected void combatUpdate() {
        if (npcSyncData == null) {
            return;
        }

        if (Strings.isNullOrEmpty(npcSyncData.combatTarget)) {
            return;
        }


        Character character = CharacterService.instance().find(npcSyncData.combatTarget);
        if (character == null) {
            return;
        }

        VitalsProxy targetVitals = VitalsHandler.get(character.playerId, ZoneService.defaultZone().name);
        if (targetVitals.isDead()) {
            return;
        }

        TrackData trackData = grid.get(character.playerId);
        if (trackData == null) {
            return;
        }

        SkillRequest request = createAttack();
        GameMessage gameMessage = new GameMessage();
        gameMessage.skillRequest = request;

        //logger.warning("Attack "+npcSyncData.combatTarget);
        //logger.warning("Health "+vitalsProxy.get("health"));
        CombatHandler.tell(gameMessage,id,CombatHandler.name);
    }

    private SkillRequest createAttack() {
        GmTarget target = new GmTarget();
        target.characterId = npcSyncData.combatTarget;
        target.type = GmTarget.Type.GameEntity;

        SkillRequest skillRequest = new SkillRequest();
        skillRequest.target = target;
        skillRequest.originEntityId = id;
        skillRequest.attackerCharacterId = id;
        skillRequest.playerSkillId = "Hammer blow";
        skillRequest.targetId = npcSyncData.combatTarget;
        skillRequest.playerSkill = PlayerSkillHandler.getTemplate(skillRequest.playerSkillId);




        return skillRequest;
    }

    public void sendSync() {
        SyncComponentMessage msg = getSyncMessage();
        engine.syncComponentMessage(msg);
    }

    private void sendConvert() {
        SyncComponentMessage msg = getSyncMessage();
        msg.npcUpdate.command = NpcUpdate.Command.ChangeGroup;
        engine.syncComponentMessage(msg);
    }

    private SyncComponentMessage getSyncMessage() {
        SyncComponentMessage msg = new SyncComponentMessage();
        msg.id = id;
        msg.npcUpdate = new NpcUpdate();
        msg.npcUpdate.health = vitalsProxy.get("health");
        msg.npcUpdate.isDead = vitalsProxy.isDead();
        return msg;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof HandlerMessage) {
            HandlerMessage handlerMessage = (HandlerMessage)message;
            if (handlerMessage.type == HandlerMessage.Type.EngineResult) {
                onEngineResult((UnityEngineResult)handlerMessage.message);
            } else if (handlerMessage.type == HandlerMessage.Type.ComponentAdd) {
                componentAdded(handlerMessage.message);
            } else if (handlerMessage.type == HandlerMessage.Type.ComponentUpdate) {
                componentUpdated(handlerMessage.message);
            } else if (handlerMessage.type == HandlerMessage.Type.ComponentRemove) {
                componentRemoved((String)handlerMessage.message);
            }
            return;
        }

        if (message instanceof NpcRequest) {
            NpcRequest npcRequest = (NpcRequest)message;
            if (npcRequest.command == NpcRequest.Command.Convert) {

            }
            return;
        }

        String command = (String)message;

        if (command.equals(State.Running.toString())) {
            doRunning();
            tick();
        } else if (command.equals(State.Starting.toString())) {
            doStarting();
            tick();
        } else if (command.equals(State.Idle.toString())) {
            doIdle();
            tick();
        } else if (command.equals("regionUpdate")) {
            regionUpdate();
            scheduleOnce(regionUpdateInterval, "regionUpdate");
        }
    }

    public void onEngineResult(UnityEngineResult result) {
        if (result instanceof InstantiateResult) {
            InstantiateResult iResult = (InstantiateResult) result;
            if (iResult.status == InstantiateResponse.Status.Success) {
                logger.warning("Instantiate success");
            } else {
                if (iResult.status == InstantiateResponse.Status.Duplicate) {
                    engine.destroy(id);
                }
                logger.warning("Instantiate error " + iResult.status.toString());
            }
        } else if (result instanceof DestroyResult) {
            DestroyResult destroyResult = (DestroyResult) result;
            if (destroyResult.status == DestroyResponse.Status.Success) {
                setState(State.Idle);
            } else {
                logger.warning("Destroy error " + destroyResult.status.toString());
            }
        }
    }

    public void componentUpdated(Object object) {
        SyncObject syncObject = (SyncObject)object;

        if (syncObject.npcSyncData != null) {
            npcSyncData = syncObject.npcSyncData;
            position = Vector3.fromGmVector3(npcSyncData.transform.position);
            trackData.speed = Mathf.toInt(npcSyncData.playerSpeed);
            trackData.userDefinedData.userdefInt1 = Mathf.toInt(npcSyncData.heading);

            if (npc.group.leader == npc) {
                //logger.warning(position.toString());
                //logger.warning(npcSyncData.moveTarget);
            }
        }
    }

    public void componentRemoved(String objectId) {
        npcSyncData = null;
        setState(State.Idle);
        logger.warning("Component removed " + objectId);
    }

    public void componentAdded(Object object) {
        setState(State.Running);
    }
}
