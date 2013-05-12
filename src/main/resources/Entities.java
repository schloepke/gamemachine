	
	public Components toComponents() {
		Components components = new Components();
		for (Entity entity : this.entities.values()) {
			for (Map.Entry<String, Component> entry : entity.getComponents().entrySet()) {
				
				if (entry.getKey().equals("Player")) {
					components.addPlayer(Player.class.cast(entry.getValue()));
				}
				
				if (entry.getKey().equals("PlayersAroundMe")) {
					components.addPlayersAroundMe(PlayersAroundMe.class.cast(entry.getValue()));
				}
				
				if (entry.getKey().equals("GameCommand")) {
					components.addGameCommand(GameCommand.class.cast(entry.getValue()));
				}
				
				
			}
		}

		return components;
	}

