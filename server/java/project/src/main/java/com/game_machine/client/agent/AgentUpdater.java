package com.game_machine.client.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import GameMachine.Messages.Agent;
import GameMachine.Messages.AgentUpdate;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import com.game_machine.client.Api;
import com.game_machine.core.CloudClient;
import com.game_machine.core.CloudClient.CloudResponse;

public class AgentUpdater extends UntypedActor {

	private Config conf;
	
	private HashMap<String, String> agents = new HashMap<String, String>();
	private HashMap<String, PlayerManager> playerManagers = new HashMap<String, PlayerManager>();
	private static final Logger logger = LoggerFactory.getLogger(AgentUpdater.class);

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
			cloudResponse = CloudClient.getInstance().getAgents(conf.getGameId());
			AgentUpdate agentUpdate = AgentUpdate.parseFrom(cloudResponse.byteBody);

			List<String> nodes = agentUpdate.getNodesList();
			List<String> agentIds = new ArrayList<String>();
			// String node = nodes.get(0);
			if (agentUpdate.getAgentCount() >= 1) {
				PlayerManager playerManager;
				
				for (Agent a : agentUpdate.getAgentList()) {
					if (!playerManagers.containsKey(a.playerId)) {
						playerManager = new PlayerManager(conf.getDefaultHost(), conf.getDefaultPort(), a.playerId, conf.getCloudApiKey());
						playerManager.start();
						playerManagers.put(a.playerId, playerManager);
					}
					agentIds.add(a.id);
					agents.put(a.id,a.playerId);
					ActorSelection sel = Api.getActorByName(a.playerId);
					sel.tell(a, null);
				}
			} else {
				System.out.println("No agents found");
			}
			pruneAgents(agentIds);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
