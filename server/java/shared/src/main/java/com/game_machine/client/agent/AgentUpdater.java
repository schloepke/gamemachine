package com.game_machine.client.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import Client.Messages.Agent;
import Client.Messages.AgentController;
import Client.Messages.ClientMessage;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.game_machine.client.Api;

public class AgentUpdater extends UntypedActor {

	private Config conf;

	private HashMap<String, String> agents = new HashMap<String, String>();
	private HashMap<String, PlayerManager> playerManagers = new HashMap<String, PlayerManager>();

	public AgentUpdater() {
		conf = Config.getInstance();
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

	private void updateCodeblocks() {
		byte[] bytes = CloudHttp.getAgents(conf.getCloudHost(), conf.getPlayerId(), conf.getCloudUser(),
				conf.getCloudApiKey());
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

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String imsg = (String) message;
			if (imsg.equals("update_codeblocks")) {
				updateCodeblocks();
				tick(5000, "update_codeblocks");
			}
		} else {
			unhandled(message);
		}
	}

	@Override
	public void preStart() {
		tick(5000, "update_codeblocks");
	}

	public void tick(int delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

}
