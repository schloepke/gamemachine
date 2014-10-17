package com.game_machine.client.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import GameMachine.Messages.Agent;
import GameMachine.Messages.AgentController;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import com.game_machine.client.Api;
import com.game_machine.core.CloudClient;
import com.game_machine.core.CloudClient.CloudResponse;

public class AgentUpdater extends UntypedActor {

	private Config conf;
	
	private HashMap<String, String> agents = new HashMap<String, String>();
	private HashMap<String, PlayerManager> playerManagers = new HashMap<String, PlayerManager>();
	public AgentUpdater() {
		conf = Config.getInstance();
	}

	private void pruneAgents(List<String> latest) {
		Iterator<Map.Entry<String,String>> iter = agents.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,String> entry = iter.next();
		    if (!latest.contains(entry.getKey())) {
		    	Agent agent = new Agent().setRemove(true).setId(entry.getKey());
		    	ActorSelection sel = Api.getActorByName(entry.getValue());
				sel.tell(agent, null);
		        iter.remove();
		        
		    }
		}
	}
	private void updateCodeblocks() {
		CloudClient.getInstance().setCredentials(conf.getCloudHost(), conf.getCloudUser(), conf.getCloudApiKey());
		CloudResponse cloudResponse;
		try {
			cloudResponse = CloudClient.getInstance().getAgents(conf.getPlayerId());
			AgentController agentController = AgentController.parseFrom(cloudResponse.byteBody);
			String playerId = agentController.getPlayer().getId();
			String authtoken = agentController.getPlayer().getAuthtoken();
			
			if (!playerManagers.containsKey(playerId)) {
				PlayerManager playerManager = new PlayerManager(conf.getDefaultHost(), conf.getDefaultPort(), conf.getGameId(), playerId, authtoken);
				playerManager.start();
				playerManagers.put(playerId, playerManager);
			}
			
			List<String> agentIds = new ArrayList<String>();
			if (agentController.getAgentCount() >= 1) {
				for (Agent a : agentController.getAgentList()) {
					agentIds.add(a.id);
					agents.put(a.id,playerId);
					ActorSelection sel = Api.getActorByName(playerId);
					sel.tell(a, null);
				}
			} else {
				System.out.println("No agents found");
			}
			pruneAgents(agentIds);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
