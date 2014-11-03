package user.messages;

import Client.Messages.TrackData.EntityType;


public class Vitals {

	public static final int HEALTH_MAX = 100;
	public String id;
	public int health;
	public EntityType entityType = EntityType.PLAYER;
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public boolean updated = false;

	// Classes that get saved to the cloud need a default no-arg constructor.
	public Vitals() {
		
	}
	
	public void setEntityType(EntityType type) {
		this.entityType = type;
	}

	public Vitals(String id) {
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
