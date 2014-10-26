package User.Messages;

public class Vitals {

	public static final int HEALTH_MAX = 100;
	public String id;
	public int health;
	public String entityType = "player";
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public boolean updated = false;
	
	public void setEntityType(String type) {
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
		this.health += amount;
		if (this.health > HEALTH_MAX) {
			this.health = HEALTH_MAX;
		}
		this.updated = true;
	}
	
	public String dbkey() {
		return "player_vitals_"+this.id;
	}

}
