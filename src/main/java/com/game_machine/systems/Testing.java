package com.game_machine.systems;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;


public class Testing {

	private static final Logger log = Logger.getLogger(Testing.class.getName());
	
	public static void test1() {
		
		try {
		for (int i=1;i<3;i++) {
			//MemcachedClient client=new XMemcachedClient("192.168.130.128",11211);
			//store a value for one hour(synchronously).
			//client.set("key",3600,"test");
			//Retrieve a value.(synchronously).
			//Object someObject=client.get("key");
			
			Root.memcachedClient.set("someKey", 3600, "testing");
			Future<Object> f=Root.memcachedClient.asyncGet("someKey");
			try {
			    f.get(5, TimeUnit.SECONDS);
			} catch(TimeoutException e) {
			    // Since we don't need this, go ahead and cancel the operation.  This
			    // is not strictly necessary, but it'll save some work on the server.
			    f.cancel(false);
			    // Do other timeout related stuff
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
