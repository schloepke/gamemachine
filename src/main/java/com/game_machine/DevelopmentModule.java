package com.game_machine;

import com.game_machine.persistence.WriteBehindHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class DevelopmentModule extends AbstractModule {
	
	@Override
    protected void configure() {
		
	}
	
	//@Provides
	//WriteBehindHandler provideWriteBehindCache() {
	//	WriteBehindHandler cache = new WriteBehindHandler(null,5000,50);
	//	return cache;
	//}

}
