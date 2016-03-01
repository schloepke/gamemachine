package io.gamemachine.regions;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.RegionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.contrib.pattern.ClusterSingletonManager;
import akka.contrib.pattern.ClusterSingletonProxy;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.google.common.base.Strings;
import com.typesafe.config.Config;

public class RegionManager extends GameMessageActor {

    private static Map<String, RegionInfo> regions = new ConcurrentHashMap<String, RegionInfo>();

    public static String name = RegionManager.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private CurrentClusterState state;
    private Long updateInterval = 10000l;

    public static ActorRef getProxy() {
        ActorSystem system = GameMachineLoader.getActorSystem();
        return system.actorOf(ClusterSingletonProxy.defaultProps("user/singleton/" + RegionManager.name, null),
                "singletonProxy");
    }

    public static void start() {
        ActorSystem system = GameMachineLoader.getActorSystem();
        system.actorOf(
                ClusterSingletonManager.defaultProps(Props.create(RegionManager.class), RegionManager.name,
                        PoisonPill.getInstance(), null), "singleton");
    }

    @Override
    public void awake() {
        logger.warning(ActorUtil.selfAddress() + " is now region handler");
        loadRegionConfig();
        scheduleOnce(updateInterval, "tick");

    }

    public void onTick(String message) {
        updateStatus();
        scheduleOnce(updateInterval, "tick");
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {

        // Reliable messages handled here
        if (exactlyOnce(gameMessage)) {

            setReply(gameMessage);
        }

        // Sending a response to the player from an unreliable message
        // PlayerCommands.sendGameMessage(gameMessage, playerId);

    }

    private void loadRegionConfig() {
        Config config = AppConfig.getConfig();
        List<? extends Config> values = config.getConfigList("gamemachine.regions");
        for (Config value : values) {
            String regionName = value.getString("name");
            int number = value.getInt("number");

            RegionInfo info;
            info = RegionInfo.db().findFirst("region_info_id = ?", regionName);
            if (info == null) {
                info = new RegionInfo();
                info.id = regionName;
                info.number = number;
                info.assigned = false;
                RegionInfo.db().save(info);
                logger.warning("Saving new region to db: " + info.id);
            }

            regions.put(info.id, info);
        }

        for (RegionInfo info : RegionInfo.db().findAll()) {
            if (!regions.containsKey(info.id)) {
                RegionInfo.db().delete(info.recordId);
                logger.warning("Deleting unused region " + info.id);
            }
        }
    }

    private Member getBestNode() {
        Member best = null;
        int lowestNodeCount = 1000;
        int nodeCount;

        for (Member member : state.getMembers()) {
            nodeCount = 0;
            String address = member.address().toString();
            for (RegionInfo info : regions.values()) {
                if (!Strings.isNullOrEmpty(info.node) && info.node.equals(address)) {
                    nodeCount++;
                }
            }
            if (nodeCount < lowestNodeCount) {
                lowestNodeCount = nodeCount;
                best = member;
            }
        }
        return best;
    }

    private boolean isNodeUp(String address) {
        for (Member member : state.getMembers()) {
            if (member.address().toString().equals(address)) {
                if (member.status() == MemberStatus.up()) {
                    return true;
                }
            }
        }
        return false;
    }

    private String addressToHostname(String address) {
        address = address.replace("akka.tcp://cluster@", "");
        String[] parts = address.split(":");
        return parts[0];
    }

    private void updateStatus() {
        state = Cluster.get(getContext().system()).state();

        for (RegionInfo info : regions.values()) {
            if (info.assigned) {
                if (!isNodeUp(info.node)) {
                    logger.warning("Unassigning region " + info.id + " from node " + info.node);
                    info.hostname = null;
                    info.node = null;
                    info.assigned = false;
                    RegionInfo.db().save(info);

                }
            }
        }

        for (RegionInfo info : regions.values()) {
            if (!info.assigned) {
                Member member = getBestNode();
                if (member != null) {
                    info.node = member.address().toString();
                    info.hostname = addressToHostname(info.node);
                    info.assigned = true;
                    RegionInfo.db().save(info);

                    logger.warning("Assigned region " + info.id + " to node " + info.node);
                } else {
                    logger.warning("Unable to find node to assign to region " + info.id);
                }
            }
        }
    }


}
