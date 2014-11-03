package user.agents;

import java.util.ArrayList;
import java.util.List;

import user.Globals;
import user.Npc;
import user.messages.Attack;
import user.messages.Vitals;
import Client.Messages.TrackData;
import Client.Messages.TrackData.EntityType;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.codeblocks.Codeblock;

public class NpcAi implements Codeblock {

	private CodeblockEnv env;
	private Api api;
	private List<Npc> npcs = new ArrayList<Npc>();
	private int npcCount = 5;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			for (int x = 0; x < npcCount; x++) {
				String npcId = this.env.getAgentId() + "npc_" + x;
				Npc npc = new Npc(npcId, Globals.grid);
				npcs.add(npc);
				Vitals vitals = Globals.getVitalsFor(npc.id);
				if (vitals == null) {
					vitals = new Vitals(npcId);
					vitals.setEntityType(EntityType.NPC);
					vitals.x = npc.position.x;
					vitals.y = npc.position.y;
					vitals.updated = true;
					Globals.setVitalsFor(vitals);
				}
			}
			if (this.env.getReloadCount() == 0) {
				this.env.tick(30, "ai");
			}
		}

	}

	@Override
	public void run(Object message) throws Exception {

		for (Npc npc : npcs) {
			npc.update();
			Vitals vitals = Globals.getVitalsFor(npc.id);
			vitals.x = npc.position.x;
			vitals.y = npc.position.y;
		}

		// Random npc
		int idx = (int) (Math.random() * npcCount - 1);
		Npc npc = npcs.get(idx);

		Vitals vitals = Globals.getVitalsFor(npc.id);

		// Random player
		List<TrackData> players = Globals.grid.neighbors((float) vitals.x, (float) vitals.y, EntityType.PLAYER);
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

		this.env.tick(30, "ai");

	}
}
