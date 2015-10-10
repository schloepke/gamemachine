package io.gamemachine.core;


public class GameEntityManagerService {
	
	private GameEntityManager gameEntityManager;
	
	private static class LazyHolder {
		private static final GameEntityManagerService INSTANCE = new GameEntityManagerService();
	}

	public static GameEntityManagerService instance() {
		return LazyHolder.INSTANCE;
	}
	
	public void setGameEntityManager(GameEntityManager gameEntityManager) {
		this.gameEntityManager = gameEntityManager;
	}
	
	public GameEntityManager getGameEntityManager() {
		return this.gameEntityManager;
	}
}
