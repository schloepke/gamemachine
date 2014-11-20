package user.agents;

import io.gamemachine.client.agent.CodeblockEnv;
import io.gamemachine.client.api.Api;
import io.gamemachine.client.api.ApiMessage;
import io.gamemachine.client.messages.AgentTrackData;
import io.gamemachine.client.messages.TrackData;
import io.gamemachine.client.messages.TrackData.EntityType;
import io.gamemachine.codeblocks.Codeblock;

import java.util.ArrayList;
import java.util.List;

import user.Globals;
import user.Npc;
import user.messages.Attack;
import user.messages.GameEntity;

public class NpcAi implements Codeblock {

	private CodeblockEnv env;
	private Api api;
	private List<Npc> npcs = new ArrayList<Npc>();

	private int npcCount = 5;
	private int delay = 50;
	private double speedScale = 4f;
	private int updateCount = 0;

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
				GameEntity gameEntity = Globals.gameEntityFor(npc.id);
				if (gameEntity == null) {
					gameEntity = new GameEntity(npcId);
					gameEntity.setEntityType(EntityType.NPC);
					gameEntity.updated = true;
					Globals.setGameEntity(gameEntity);
				}
			}
			if (this.env.getReloadCount() == 0) {
				this.env.tick(delay, "ai");
			}
		}

	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			npcUpdate();
			this.env.tick(delay, "ai");
		}
	}

	private void npcUpdate() {
		AgentTrackData agentTrackData = new AgentTrackData();
		for (Npc npc : npcs) {
			double x = npc.position.x;
			double y = npc.position.y;
			npc.update();
			GameEntity gameEntity = Globals.gameEntityFor(npc.id);

			TrackData trackData = new TrackData();
			trackData.setId(gameEntity.id);
			trackData.setEntityType(gameEntity.entityType);

			if (Globals.npcResend.containsKey(npc.id)) {
				trackData.setX((int) Math.round(npc.position.x * 100));
				trackData.setY((int) Math.round(npc.position.y * 100));
				Globals.npcResend.remove(npc.id);
			} else {
				double xDelta;
				double yDelta;
				if (npc.position.x >= x) {
					xDelta = npc.position.x - x;
				} else {
					xDelta = -(x - npc.position.x);
				}

				if (npc.position.y >= y) {
					yDelta = npc.position.y - y;
				} else {
					yDelta = -(y - npc.position.y);
				}

				int xi = (int) (xDelta * 100);
				int yi = (int) (yDelta * 100);
				trackData.setIx(xi);
				trackData.setIy(yi);
			}

			trackData.setGetNeighbors(0);
			agentTrackData.addTrackData(trackData);
		}

		if (agentTrackData.getTrackDataCount() >= 1) {
			ApiMessage apiMessage = this.api.newMessage();
			apiMessage.setAgentTrackData(agentTrackData);
			apiMessage.send();
		}
		updateCount++;
	}

	private void doCombat() {

		// Random npc
		int idx = (int) (Math.random() * npcCount - 1);
		Npc npc = npcs.get(idx);

		// Random player
		List<TrackData> players = Globals.grid.neighbors((float) npc.position.x, (float) npc.position.y,
				EntityType.PLAYER);
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

			trackData.setX(-1);
			trackData.setY(-1);
			trackData.setZ(-1);
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
