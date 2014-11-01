package user.agents;

import java.util.ArrayList;
import java.util.List;

import user.Globals;
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
	private List<String> npcIds = new ArrayList<String>();

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			for (int x = 0; x < 200; x++) {
				String npcId = this.env.getAgentId()+"npc_" + x;
				npcIds.add(npcId);
				Vitals vitals = Globals.getVitalsFor(npcId);
				if (vitals == null) {
					vitals = new Vitals(npcId);
					vitals.setEntityType(EntityType.NPC);
					vitals.x = (float)(Math.random() * 2000 + 1);
					vitals.y = (float)(Math.random() * 2000 + 1);
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
		
		// Random npc
		int idx = (int) (Math.random() * 20);
		String npcId = npcIds.get(idx);
		Vitals vitals = Globals.getVitalsFor(npcId);
		
		// Random player
		List<TrackData> players = Globals.grid.neighbors(vitals.x, vitals.y, EntityType.PLAYER);
		int playerIdx = (int) (Math.random() * players.size());
		if (!players.contains(playerIdx)) {
			return;
		}
		TrackData trackData = players.get(playerIdx);
		
		// Random chance to attack
		int chance = (int) (Math.random() * 1000 + 1);
		if (chance >= 990) {
			Attack attack = new Attack();
			attack.attacker = npcId;
			attack.target = trackData.getId();
			attack.attack = "smack";
			this.env.sendToAgent("CombatManager", attack);
		}

		this.env.tick(30, "ai");

	}
}
