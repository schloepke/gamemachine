package user.messages;

import io.gamemachine.client.messages.TrackData.EntityType;


public class GameEntity {

	public static final int HEALTH_MAX = 100;
	public String id;
	public int health = HEALTH_MAX;
	public EntityType entityType = EntityType.PLAYER;
	public boolean updated = false;

	// Classes that get saved to the cloud need a default no-arg constructor.
	public GameEntity() {
		
	}
	
	public ClientGameEntity toClientGameEntity() {
		ClientGameEntity clientGameEntity = new ClientGameEntity();
		clientGameEntity.health = this.health;
		return clientGameEntity;
	}
	
	public void setEntityType(EntityType type) {
		this.entityType = type;
	}

	public GameEntity(String id) {
		this.id = id;
		this.health = HEALTH_MAX;
	}

	public void lowerHealth(int amount) {
		this.health -= amount;
		if (this.health < 0) {
			this.health = 0;
		}
		this.updated = true;
	}

	public void raiseHealth(int amount) {
		if (this.health == HEALTH_MAX) {
			return;
		}
		
		this.health += amount;
		if (this.health > HEALTH_MAX) {
			this.health = HEALTH_MAX;
		}
		this.updated = true;
	}

}
