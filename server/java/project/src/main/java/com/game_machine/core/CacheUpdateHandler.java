package com.game_machine.core;

import java.lang.reflect.Method;
import java.util.HashMap;

import akka.actor.UntypedActor;


public class CacheUpdateHandler extends UntypedActor {

	private static HashMap<Class<?>, Method> methods = new HashMap<Class<?>, Method>();
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof CacheUpdate) {
			CacheUpdate cacheUpdate = (CacheUpdate) message;
			Class<?> klass = cacheUpdate.getKlass();
			if (!methods.containsKey(klass)) {
				methods.put(klass, klass.getMethod("cacheSetFromUpdate", CacheUpdate.class));
			}
			Method method = methods.get(klass);
			method.invoke(null, cacheUpdate);
			getSender().tell(true, null);
		}
	}

}
