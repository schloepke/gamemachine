package plugins.npcdemo;

import java.util.Random;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.grid.GridService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.*;
import io.gamemachine.messages.TrackData.EntityType;
import io.gamemachine.regions.ZoneService;
import io.gamemachine.unity.UnitySync;
import io.gamemachine.unity.unity_engine.*;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsProxy;

public class NpcEntity extends GameMessageActor implements UnityEngineHandler {

    public enum State {
        Idle,
        Starting,
        Running
    }

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private State state;
    private int worldOffset = 0;
    private String id;
    private Grid grid;
    private Vector3 position;
    private TrackData trackData;
    public long runningInterval = 40l;
    public long idleInterval = 5000L;
    public long startTimeout = 10000L;
    public long startTime;
    public float mapSize = 990f;
    public float startPoint = 0f;
    public double speed = 5d;
    public Vector3 target;
    private Random rand;
    private long lastUpdate = 0;
    private VitalsProxy vitalsProxy;
    private UnityEngine engine;

    public NpcEntity(String playerId, String characterId, int mapSize, int startPoint) {
        id = playerId;
        this.mapSize = mapSize;
        this.startPoint = startPoint;
        state = State.Idle;
        engine = new UnityEngine("default", this);
        UnitySync.registerHandler(this, id);
    }

    @Override
    public void awake() {
        worldOffset = AppConfig.getWorldOffset();
        rand = new Random();
        grid = GridService.getInstance().getPlayerGrid(null, id);
        position = randVector();
        //logger.warning("npc "+position);
        target = randVector();
        initTrackData();
        lastUpdate = System.currentTimeMillis();
        vitalsProxy = VitalsHandler.get(id, ZoneService.defaultZone().name);


        scheduleOnce(runningInterval, state.toString());
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
    }


    private void SetState(State state) {
        logger.warning("Transition "+this.state+" -> "+state);
        this.state = state;
    }
    private void doStarting() {
        if (System.currentTimeMillis() - startTime > startTimeout) {
            logger.warning("Start timeout");
            SetState(State.Idle);
        }
    }

    private void doIdle() {
        if (!engine.isAlive()) {
            return;
        }

        UnitySync.registerHandler(this, id);
        engine.instantiate("npc", id, position);
        startTime = System.currentTimeMillis();
        SetState(State.Starting);
    }

    private void doRunning() {
        if (vitalsProxy.isDead()) {
            sendTrackData();
            lastUpdate = System.currentTimeMillis();
            scheduleOnce(runningInterval, "update");
            return;
        }

        move();
        double dist = position.distance2d(target);
        if (dist <= 2) {
            target = randVector();
        }
        lastUpdate = System.currentTimeMillis();
        sendTrackData();
    }

    @Override
    public void onTick(String message) {

        if (message.equals(State.Idle.toString())) {
            doIdle();
            scheduleOnce(idleInterval, state.toString());
        } else if (message.equals(State.Starting.toString())) {
            doStarting();
            scheduleOnce(idleInterval, state.toString());
        } else if (message.equals(State.Running.toString())) {
            doRunning();
            scheduleOnce(runningInterval, state.toString());
        }


    }

    private void move() {
        double deltatime = deltaTime();
        Vector3 dir = Vector3.zero();
        dir.x = target.x - position.x;
        dir.z = target.z - position.z;
        dir.normalizeLocal();

        position.x += (dir.x * speed * deltatime);
        position.z += (dir.z * speed * deltatime);
    }

    private double deltaTime() {
        return (System.currentTimeMillis() - lastUpdate) / 1000d;
    }

    private void initTrackData() {
        trackData = new TrackData();
        trackData.id = id;
        trackData.entityType = EntityType.Npc;
        trackData.userDefinedData = new UserDefinedData();
    }

    private int toInt(double num) {
        return (int) Math.round(num * 100l);
    }

    private void sendTrackData() {
        trackData.setX(toInt(position.x + worldOffset));
        trackData.setY(toInt(position.y + worldOffset));
        trackData.setZ(toInt(position.z + worldOffset));

        grid.set(trackData);
    }

    private Vector3 randVector() {
        Vector3 np = new Vector3();
        np.x = randFloat(startPoint, startPoint + mapSize);
        np.z = randFloat(startPoint, startPoint + mapSize);
        np.y = 40f;
        return np;
    }

    private float randFloat(float min, float max) {
        return rand.nextInt((int) ((max - min) + 1)) + min;
    }

    @Override
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
                SetState(State.Idle);
            } else {
                logger.warning("Destroy error " + destroyResult.status.toString());
            }
        }
    }

    @Override
    public void componentUpdated(Object object) {
        if (object instanceof GmNpc) {
            GmNpc gmNpc = (GmNpc) object;
            position = Vector3.fromGmVector3(gmNpc.transform.position);
        }
    }

    @Override
    public void componentRemoved(String objectId) {
        SetState(State.Idle);
        logger.warning("Component removed " + objectId);
    }

    @Override
    public void componentAdded(Object object) {
        SetState(State.Running);
    }
}
