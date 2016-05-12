package plugins.npc;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.*;
import io.gamemachine.regions.ZoneService;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by chris on 3/1/2016.
 */
public class NpcBase extends UntypedActor {

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

    public enum State {
        Idle,
        Starting,
        Running
    }

    public NpcBase(String characterId, String region) {
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
    public void postStop() {
        grid.remove(id);
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
        if (moveTarget == Vector3.zero) {
            return;
        }

        double deltatime = deltaTime();

        if (deltatime > 0.1) {
            logger.warning("Deltatime warning "+deltatime);
            return;
        }

        double distanceToTarget = position.distance2d(moveTarget);
        if (distanceToTarget <= 0.5) {
            targetReached = true;
            return;
        }

        targetReached = false;

        Vector3 dir = Vector3.zero();
        dir.x = moveTarget.x - position.x;
        dir.z = moveTarget.z - position.z;
        dir.normalizeLocal();


        double x = (dir.x * speed * deltatime);
        double z = (dir.z * speed * deltatime);

        //logger.warning("position "+position.toString()+ " dist "+distanceToTarget+" x "+x+" * "+dir.x+" * 5 * "+deltatime);
        //logger.warning("position "+position.toString()+ " dist "+distanceToTarget+" z "+x+" * "+dir.z+" * 5 * "+deltatime);

        position.x += x;
        position.z += z;
    }

    protected double deltaTime() {
        return (System.currentTimeMillis() - lastUpdate) / 1000d;
    }

    protected void initTrackData() {
        trackData = new TrackData();
        trackData.id = id;
        trackData.entityType = TrackData.EntityType.Npc;
        trackData.userDefinedData = new UserDefinedData();
    }



    protected void sendTrackData() {
        trackData.setX(Mathf.toInt(position.x + worldOffset));
        trackData.setY(Mathf.toInt(position.y + worldOffset));
        trackData.setZ(Mathf.toInt(position.z + worldOffset));

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

    protected void doStarting() {
        position = npc.position;
        setState(State.Running);
    }

    protected void doIdle() {

    }

    protected void doRunning() {
        if (vitalsProxy.isDead()) {
            sendTrackData();
            lastUpdate = System.currentTimeMillis();
            return;
        }

        npc.position = position;
        updateTarget();

        move();

        lastUpdate = System.currentTimeMillis();
        sendTrackData();

        if (combatAi != null) {
            combatAi.update(position);
        }
    }

    protected void updateTarget() {

       /* if (moveTarget == Vector3.zero || targetReached) {
            if (path.size() > 0) {
                moveTarget = path.poll();
                return;
            }

            if (System.currentTimeMillis() - lastPathRequest < 500) {
                return;
            }

            Vector3 wanted;

            if (npc.isLeader()) {
                if (npc.hasRoute()) {
                    Waypoint wp = npc.route.currentWaypoint();
                    if (wp.position.distance2d(position) <= 0.5) {
                        wp = npc.route.nextWaypoint();
                    }
                    wanted = wp.position;
                    //logger.warning("wanted "+wanted.toString());
                } else {
                    wanted = randomMoveInRegion(100D);

                }
            } else if (npc.hasLeader()) {
                wanted = npc.getLeader().position;
            } else {
                wanted = randomMoveInRegion(100D);
            }

            if (wanted == null) {
                logger.warning("Wanted is null");
                return;
            }
            findPath(position,wanted);
        }*/
    }

    protected Vector3 randomMoveInRegion(double distance) {
        Vector3 max = new Vector3(regionData.position.x+regionData.size,0d,regionData.position.z+regionData.size);
        return randomMove(regionData.position, max, position, distance);
    }

    protected void findPath(Vector3 start, Vector3 end) {
        engine.findPath(start, end);
        lastPathRequest = System.currentTimeMillis();
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
        } else if (result instanceof PathResult) {
            PathResult pathResult = (PathResult)result;
            if (pathResult.status != PathResponse.Status.Success) {
                logger.warning("Path error "+pathResult.status.toString());
            } else {
                path.clear();
                //logger.warning("paths "+pathResult.path.size());
                for (Vector3 vec : pathResult.path) {
                    path.add(vec);
                }
            }
        }
    }

    public void componentUpdated(Object object) {
        if (object instanceof NpcSyncData) {
            NpcSyncData gmNpc = (NpcSyncData) object;
            //position = Vector3.fromGmVector3(npcSyncData.transform.position);
        }
    }

    public void componentRemoved(String objectId) {
        setState(State.Idle);
        logger.warning("Component removed " + objectId);
    }

    public void componentAdded(Object object) {
        setState(State.Running);
    }
}
