package com.game_machine.client.agent;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.game_machine.client.Api;
import com.game_machine.client.UdpClient;

public class PlayerManager {

	private UdpClient udpClient;
	private String host;
	private int port;
	private String playerId;
	private String authtoken;
	
	public PlayerManager(String host, int port, String playerId, String authtoken) {
		this.host = host;
		this.port = port;
		this.playerId = playerId;
		this.authtoken = authtoken;
	}
	
	public void start() {
		udpClient = new UdpClient(host,port,playerId);
		udpClient.start();
		startPlayerActor();
	}
	
	private void startPlayerActor() {
		ActorSystem system = Api.getActorSystem();
		Api api = new Api(playerId, authtoken, udpClient);
		system.actorOf(Props.create(PlayerActor.class, api, playerId), playerId);
	}
	
	private void stopPlayerActor() {
		ActorSelection sel = Api.getActorByName(playerId);
		sel.tell(akka.actor.PoisonPill.getInstance(), null);
	}
	
	public void stop() {
		stopPlayerActor();
		
		udpClient.getChannel().close();
		try {
			udpClient.getChannel().closeFuture().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
