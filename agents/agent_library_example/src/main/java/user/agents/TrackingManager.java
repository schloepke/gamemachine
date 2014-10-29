package user.agents;

import java.util.List;

import user.Globals;
import user.messages.Vitals;
import Client.Messages.AgentTrackData;
import Client.Messages.Neighbors;
import Client.Messages.TrackData;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.codeblocks.Codeblock;

public class TrackingManager implements Codeblock {

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
		}	
	}
	
	private void updatePositions(List<TrackData> trackDatas) {
		for (TrackData trackData : trackDatas) {
			Vitals vitals = Globals.getVitalsFor(trackData.getId());
			if (vitals != null) {
				vitals.x = trackData.getX();
				vitals.y = trackData.getY();
				vitals.z = trackData.getZ();

				Globals.grid.set(trackData);
				Globals.aoeGrid.set(trackData);
			}
		}
	}
	
	private void getNeighbors() {

		// Send a TrackData for each ai. Although the container is called
		// AgentTrackData, the TrackData's it contains
		// do not have to be agents, they can be anything they just need an id
		// and coordinates.
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

		// Trackdata for the player (controller). We don't need to track the
		// controller, but the server does it's neighbor
		// query off of the entity type that trackdata contains. Trackdata was
		// designed for 'normal' clients, so if this seems a bit odd
		// that is why.

		// Note: The entity type of 'grid' is specific to controllers (players
		// with a role of agent_controller). It tells the server to send us
		// the entire grid not just entities within range. Normal clients do not
		// have access to this.
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
