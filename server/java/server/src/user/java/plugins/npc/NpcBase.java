package plugins.npc;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.*;
import io.gamemachine.regions.ZoneService;
import io.gamemachine.unity.UnityMessageHandler;
import io.gamemachine.unity.UnitySync;
import io.gamemachine.unity.unity_engine.*;
import io.gamemachine.unity.unity_engine.engine_results.DestroyResult;
import io.gamemachine.unity.unity_engine.engine_results.InstantiateResult;
import io.gamemachine.unity.unity_engine.engine_results.PathResult;
import io.gamemachine.unity.unity_engine.engine_results.UnityEngineResult;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;
import io.gamemachine.util.Mathf;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsProxy;
import scala.concurrent.duration.Duration;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by chris on 3/1/2016.
 */
public class NpcBase extends UntypedActor implements UnityEngineHandler {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    protected State state;
    protected int worldOffset = 0;
    protected String id;
    protected Grid grid;
    protected Vector3 position = Vector3.zero;
    protected TrackData trackData;
    protected long runningInterval = 60l;
    protected long idleInterval = 5000L;
    protected long startingInterval = 5000L;
    protected long regionUpdateInterval = 5000L;
    protected long startTimeout = 10000L;
    protected long startTime;
    protected double speed = 5d;
    protected Vector3 moveTarget = Vector3.zero;
    protected Random rand;
    protected long lastUpdate = 0;
    protected VitalsProxy vitalsProxy;
    protected UnityEngine engine;
    protected String region;
    protected RegionData regionData;

    public enum State {
        Idle,
        Starting,
        Running
    }

    public NpcBase(String playerId, String characterId, String region) {
        id = playerId;
        this.region = region;

        rand = new Random();
        state = State.Idle;
        initTrackData();
        worldOffset = AppConfig.getWorldOffset();
        grid = GridService.getInstance().getPlayerGrid(null, id);
        vitalsProxy = VitalsHandler.get(id, ZoneService.defaultZone().name);

        engine = new UnityEngine(region, this);
        UnitySync.registerHandler(this, id);
    }

    @Override
    public void onReceive(Object message) throws Exception {
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

    @Override
    public void preStart() {
        awake();
        tick();
        scheduleOnce(regionUpdateInterval, "regionUpdate");
    }

    protected void awake() {

    }

    public void regionUpdate() {
        regionData = UnityMessageHandler.getRegionData(region);
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
        if (moveTarget == Vector3.zero) {
            return;
        }

        double deltatime = deltaTime();

        if (deltatime > 0.1) {
            logger.warning("Deltatime warning "+deltatime);
            return;
        }

        Vector3 dir = Vector3.zero();
        dir.x = moveTarget.x - position.x;
        dir.z = moveTarget.z - position.z;
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
        trackData.entityType = TrackData.EntityType.Npc;
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


    protected Vector3 randomStart() {
        Vector3 max = new Vector3(regionData.position.x+regionData.size,0d,regionData.position.z+regionData.size);
        return Mathf.randomInRange(regionData.position, max);
    }

    protected Vector3 randomMove(Vector3 min, Vector3 max, Vector3 pos, double range) {
        Vector3 np = new Vector3();
        double startX = Mathf.randomRange(pos.x-(range/2),pos.x+(range/2));
        if (startX < min.x) {
            startX = min.x;
        } else if (startX > max.x) {
            startX = max.x;
        }
        double startZ = Mathf.randomRange(pos.z-(range/2),pos.z+(range/2));
        if (startZ < min.z) {
            startZ = min.z;
        } else if (startZ > max.z) {
            startZ = max.z;
        }
        return new Vector3(startX,0d,startZ);
    }

    // User configurable

    private void doStarting() {
        position = randomStart();
        setState(State.Running);
    }

    private void doIdle() {

    }

    private void doRunning() {
        if (vitalsProxy.isDead()) {
            sendTrackData();
            lastUpdate = System.currentTimeMillis();
            return;
        }


        move();
        updateTarget();

        lastUpdate = System.currentTimeMillis();
        sendTrackData();
    }

    private void updateTarget() {
        if (moveTarget == Vector3.zero || position.distance2d(moveTarget) <= 2) {
            Vector3 max = new Vector3(regionData.position.x+regionData.size,0d,regionData.position.z+regionData.size);
            moveTarget = randomMove(regionData.position, max, position, 50d);
            engine.findPath(position, moveTarget);
        }
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
                setState(State.Idle);
            } else {
                logger.warning("Destroy error " + destroyResult.status.toString());
            }
        } else if (result instanceof PathResult) {
            PathResult pathResult = (PathResult)result;
            if (pathResult.status != PathResponse.Status.Success) {
                logger.warning("Path error "+pathResult.status.toString());
            }
        }
    }

    @Override
    public void componentUpdated(Object object) {
        if (object instanceof GmNpc) {
            GmNpc gmNpc = (GmNpc) object;
            //position = Vector3.fromGmVector3(gmNpc.transform.position);
        }
    }

    @Override
    public void componentRemoved(String objectId) {
        setState(State.Idle);
        logger.warning("Component removed " + objectId);
    }

    @Override
    public void componentAdded(Object object) {
        setState(State.Running);
    }
}
