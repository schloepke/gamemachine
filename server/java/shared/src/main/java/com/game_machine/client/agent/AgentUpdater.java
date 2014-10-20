package com.game_machine.client.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Client.Messages.Agent;
import Client.Messages.AgentController;
import akka.actor.ActorSelection;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.Cloud;

public class AgentUpdater {

	private Config conf;
	private Cloud cloud;

	private HashMap<String, String> agents = new HashMap<String, String>();
	private HashMap<String, PlayerManager> playerManagers = new HashMap<String, PlayerManager>();

	public AgentUpdater() {
		conf = Config.getInstance();
		this.cloud = new Cloud();
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

	public void updateCodeblocks() {
		byte[] bytes = cloud.getAgents(conf.getPlayerId());
		if (bytes == null) {
			return;
		}
		AgentController agentController = new AgentController();
		ProtobufIOUtil.mergeFrom(bytes, agentController, RuntimeSchema.getSchema(AgentController.class));

		String playerId = agentController.getPlayer().getId();
		String authtoken = agentController.getPlayer().getAuthtoken();

		if (!playerManagers.containsKey(playerId)) {
			PlayerManager playerManager = new PlayerManager(conf.getDefaultHost(), conf.getDefaultPort(),
					conf.getGameId(), playerId, authtoken);
			playerManager.start();
			playerManagers.put(playerId, playerManager);
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
			System.out.println("No agents found");
		}
		pruneAgents(agentIds);
	}

}
