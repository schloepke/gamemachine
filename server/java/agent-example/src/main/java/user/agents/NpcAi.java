package user.agents;

import java.util.ArrayList;
import java.util.List;

import user.Globals;
import user.Npc;
import user.messages.Attack;
import user.messages.GameEntity;
import io.gamemachine.client.messages.AgentTrackData;
import io.gamemachine.client.messages.TrackData;
import io.gamemachine.client.messages.TrackData.EntityType;

import io.gamemachine.client.agent.CodeblockEnv;
import io.gamemachine.client.api.Api;
import io.gamemachine.client.api.ApiMessage;
import io.gamemachine.codeblocks.Codeblock;

public class NpcAi implements Codeblock {

	private CodeblockEnv env;
	private Api api;
	private List<Npc> npcs = new ArrayList<Npc>();
	private int npcCount = 50;
	private int delay = 50;
	private double speedScale = 4f;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			for (int x = 0; x < npcCount; x++) {
				String npcId = this.env.getAgentId() + "-" + x;
				Npc npc = new Npc(npcId, Globals.grid, speedScale);
				npcs.add(npc);
				GameEntity vitals = Globals.gameEntityFor(npc.id);
				if (vitals == null) {
					vitals = new GameEntity(npcId);
					vitals.setEntityType(EntityType.NPC);
					vitals.updated = true;
					Globals.setGameEntity(vitals);
				}
			}
			if (this.env.getReloadCount() == 0) {
				this.env.tick(delay, "ai");
			}
		}

	}

	@Override
	public void run(Object message) throws Exception {
		npcUpdate();
		this.env.tick(delay, "ai");
	}

	private void npcUpdate() {
		AgentTrackData agentTrackData = new AgentTrackData();
		for (Npc npc : npcs) {
			npc.update();
			GameEntity vitals = Globals.gameEntityFor(npc.id);
			
			TrackData trackData = new TrackData();
			trackData.setId(vitals.id);
			trackData.setEntityType(vitals.entityType);
			
			// Limit scale to 2, no need to send extra data we don't have to
			trackData.setX((float) Math.round(npc.position.x * 100) / 100);
			trackData.setY((float) Math.round(npc.position.y * 100) / 100);
			trackData.setZ(0f);
			trackData.setGetNeighbors(0);
			agentTrackData.addTrackData(trackData);
		}
		
		if (agentTrackData.getTrackDataCount() >= 1) {
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setAgentTrackData(agentTrackData);
			apiMessage.send();
		}
	}

	private void doCombat() {
		
		// Random npc
		int idx = (int) (Math.random() * npcCount - 1);
		Npc npc = npcs.get(idx);

		// Random player
		List<TrackData> players = Globals.grid.neighbors((float) npc.position.x, (float) npc.position.y, EntityType.PLAYER);
		int playerIdx = (int) (Math.random() * players.size());
		if (players.contains(playerIdx)) {
			TrackData trackData = players.get(playerIdx);

			// Random chance to attack
			int chance = (int) (Math.random() * 1000 + 1);
			if (chance >= 990) {
				Attack attack = new Attack();
				attack.attacker = npc.id;
				attack.target = trackData.getId();
				attack.attack = "smack";
				this.env.sendToAgent("CombatManager", attack);
			}
		}
	}

	@Override
	public void shutdown(Object arg0) throws Exception {
		
		// Remove npc's from grid
		AgentTrackData agentTrackData = new AgentTrackData();
		for (Npc npc : npcs) {
			TrackData trackData = new TrackData();
			trackData.setId(npc.id);
			trackData.setEntityType(TrackData.EntityType.NPC);
			
			trackData.setX(-1f);
			trackData.setY(-1f);
			trackData.setZ(-1f);
			trackData.setGetNeighbors(0);
			agentTrackData.addTrackData(trackData);
		}
		
		if (agentTrackData.getTrackDataCount() >= 1) {
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setAgentTrackData(agentTrackData);
			apiMessage.send();
		}
		npcs.clear();
		
	}
}
