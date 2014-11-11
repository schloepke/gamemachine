package io.gamemachine.client.agent;

import io.gamemachine.client.api.Api;
import io.gamemachine.client.api.cloud.ObjectStore;
import io.gamemachine.client.messages.Agent;
import io.gamemachine.client.messages.AgentController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

public class AgentUpdater {

	private static final Logger logger = LoggerFactory.getLogger(AgentUpdater.class);
	
	private Config conf;
	private ObjectStore cloud;

	private HashMap<String, String> agents = new HashMap<String, String>();
	private HashMap<String, PlayerManager> playerManagers = new HashMap<String, PlayerManager>();

	public AgentUpdater() {
		conf = Config.getInstance();
		this.cloud = new ObjectStore(conf.getCloudHost(),conf.getCloudUser(),conf.getCloudApiKey(),conf.getGameId(),null);
	}

	private void pruneAgents(List<String> latest) {
		Iterator<Map.Entry<String, String>> iter = agents.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			if (!latest.contains(entry.getKey())) {
				Agent agent = new Agent().setRemove(true).setId(entry.getKey());
				ActorSelection sel = Api.getActorByName(entry.getValue());
				sel.tell(agent, null);
				iter.remove();

			}
		}
	}

	public void playerDisconnected(String playerId) {
		logger.warn("Stopping player manager for "+playerId);
		if (playerManagers.containsKey(playerId)) {
			PlayerManager playerManager = playerManagers.get(playerId);
			playerManager.stop();
			playerManagers.remove(playerId);
		}
	}
	
	public void updateCodeblocks() {
		byte[] bytes = cloud.getAgents(conf.getPlayerId());
		if (bytes == null) {
			logger.error("cloud.getAgents returned null");
			return;
		}
		AgentController agentController = new AgentController();
		ProtobufIOUtil.mergeFrom(bytes, agentController, RuntimeSchema.getSchema(AgentController.class));

		String playerId = agentController.getPlayer().getId();
		String authtoken = agentController.getPlayer().getAuthtoken();

		if (!playerManagers.containsKey(playerId)) {
			PlayerManager playerManager = new PlayerManager(conf.getDefaultHost(), conf.getDefaultPort(),
					conf.getGameId(), playerId, authtoken);
			if (playerManager.start()) {
				playerManagers.put(playerId, playerManager);
			} else {
				logger.error("PlayerManager.start returned false");
				return;
			}
		}

		List<String> agentIds = new ArrayList<String>();
		if (agentController.getAgentCount() >= 1) {
			for (Agent a : agentController.getAgentList()) {
				agentIds.add(a.getId());
				agents.put(a.getId(), playerId);
				ActorSelection sel = Api.getActorByName(playerId);
				sel.tell(a, null);
			}
		} else {
			logger.warn("No agents found");
		}
		pruneAgents(agentIds);
	}

}
