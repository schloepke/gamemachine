package com.game_machine;

import com.game_machine.persistence.WriteBehindHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

public class DevelopmentModule extends AbstractModule {
	
	//Injector injector = Guice.createInjector(new DevelopmentModule());
	
	@Override
    protected void configure() {
		
	}
	
	//@Provides
	//WriteBehindHandler provideWriteBehindCache() {
	//	WriteBehindHandler cache = new WriteBehindHandler(null,5000,50);
	//	return cache;
	//}

}
