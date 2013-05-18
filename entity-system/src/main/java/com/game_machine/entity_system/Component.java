package com.game_machine.entity_system;

public class Component {

	private Integer entityId;
	
	public Component() {
	}

	public Integer getEntityId()
    {
        return entityId;
    }

    public void setEntityId(Integer entityId)
    {
        this.entityId = entityId;
    }
    
    public Boolean hasEntityId() {
    	return (this.entityId != null);
    }
    
}
