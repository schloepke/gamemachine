package io.gamemachine.util;

import com.typesafe.config.Config;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.unity.UnityInstanceTest;

public class TestRunner {

    public static void run() {

        Config appConfig = AppConfig.getConfig();
        boolean test = appConfig.getBoolean("gamemachine.test");

        if (test) {
            unityInstanceTest();
        }

    }

    public static void unityInstanceTest() {
        ActorUtil.createActor(UnityInstanceTest.class, UnityInstanceTest.class.getSimpleName());
    }
}
