package com.game_machine.core.game;

import java.io.IOException;
import java.util.logging.Logger;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;



public class Testing {

	private static final Logger log = Logger.getLogger(Testing.class.getName());
	
	public static void test1() {
		try {
			MemcachedClient memcachedClient = new MemcachedClient(AddrUtil.getAddresses("192.168.130.128:11211"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to init memcached client");
		}
		try {
		for (int i=1;i<3;i++) {
			//MemcachedClient client=new XMemcachedClient("192.168.130.128",11211);
			//store a value for one hour(synchronously).
			//client.set("key",3600,"test");
			//Retrieve a value.(synchronously).
			//Object someObject=client.get("key");
			
			/*Root.memcachedClient.set("someKey", 3600, "testing");
			Future<Object> f=Root.memcachedClient.asyncGet("someKey");
			try {
			    f.get(5, TimeUnit.SECONDS);
			} catch(TimeoutException e) {
			    // Since we don't need this, go ahead and cancel the operation.  This
			    // is not strictly necessary, but it'll save some work on the server.
			    f.cancel(false);
			    // Do other timeout related stuff
			}*/
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
