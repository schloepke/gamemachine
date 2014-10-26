package User.Agents;

import java.util.List;

import Client.Messages.AgentTrackData;
import Client.Messages.Neighbors;
import Client.Messages.TrackData;
import User.Globals;
import User.Messages.Attack;
import User.Messages.Vitals;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.codeblocks.Codeblock;

public class CombatManager implements Codeblock {

	private CodeblockEnv env;
	private Api api;

	@Override
	public void awake(Object message) {
		if (message instanceof CodeblockEnv) {
			this.env = (CodeblockEnv) message;
			this.api = env.getApi();
			System.out.println("Agent " + this.env.getAgentId() + " is awake");

			if (this.env.getReloadCount() == 0) {
				this.env.tick(30, "get_neighbors");
			}
		}
	}

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof String) {
			String periodic = (String) message;
			if (periodic.equals("get_neighbors")) {
				getNeighbors();
				this.env.tick(30, "get_neighbors");
			}
		} else if (message instanceof Neighbors) {
			Neighbors neighbors = (Neighbors) message;
			updatePositions(neighbors.getTrackDataList());
		} else if (message instanceof Attack) {
			Attack attack = (Attack) message;
			Vitals attacker = Globals.getVitalsFor(attack.attacker);
			if (attacker == null) {
				System.out.println("Invalid attacker " + attack.attacker + ".  No registered?");
				return;
			}
			Vitals vitals = Globals.getVitalsFor(attack.target);
			if (vitals != null) {
				int damage = (int) (Math.random() * 5 + 1);
				vitals.lowerHealth(damage);
				System.out.println(attack.attacker + " attacked " + attack.target + " for " + damage + " damage");
			}
		}
	}

	private void updatePositions(List<TrackData> trackDatas) {
		for (TrackData trackData : trackDatas) {
			Vitals vitals = Globals.getVitalsFor(trackData.getId());
			if (vitals != null) {

				// Always consider coords to possibly be null.
				if (trackData.getX() != null) {
					vitals.x = trackData.getX();
				} else {
					vitals.x = 0;
				}
				if (trackData.getY() != null) {
					vitals.y = trackData.getY();
				} else {
					vitals.y = 0;
				}
				if (trackData.getZ() != null) {
					vitals.z = trackData.getZ();
				} else {
					vitals.z = 0;
				}
				Globals.grid.set(trackData);
				Globals.aoeGrid.set(trackData);
			}
		}
	}

	private void getNeighbors() {

		// The server allows agents to be entities in the grid. In this case an
		// 'agent' can be anything, it doesn't have to
		// be an actual agent. You could have a single agent managing the ai for
		// several 'mobs', and have a Vitals instance for each mob
		AgentTrackData agentTrackData = new AgentTrackData();
		for (Vitals vitals : Globals.getVitalsList()) {
			TrackData trackData = new TrackData();
			trackData.setId(vitals.id);
			trackData.setEntityType(vitals.entityType);
			trackData.setX(vitals.x);
			trackData.setY(vitals.y);
			trackData.setZ(vitals.z);
			agentTrackData.addTrackData(trackData);
		}
		ApiMessage apiMessage = this.api.newMessage();
		apiMessage.setAgentTrackData(agentTrackData);

		// This trackdata tells the server what kind of grid query to perform.
		// Players with
		// the agent_controller role can specify 'grid' as the entity type to
		// get all players regardless of location
		TrackData trackData = new TrackData();
		trackData.setId(this.api.getPlayerId());
		trackData.setEntityType("player");
		trackData.setX(0f);
		trackData.setY(0f);
		trackData.setZ(0f);
		trackData.setNeighborEntityType("grid");
		apiMessage.setTrackData(trackData);
		apiMessage.send();

	}

}
