package io.gamemachine.util;

import com.typesafe.config.Config;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.routing.GameMessageRoute;
import io.gamemachine.unity.UnityInstanceManager;

public class TestRunner {

	public static void run() {
		
		Config appConfig = AppConfig.getConfig();
		boolean test = appConfig.getBoolean("gamemachine.test");
		
		if (test) {
			unityInstanceTest();
		}
		
	}
	
	public static void unityInstanceTest() {
		GameMessageRoute.add(UnityInstanceManager.name,UnityInstanceManager.name,false);
		ActorUtil.createActor(UnityInstanceManager.class,UnityInstanceManager.name);
	}
}
