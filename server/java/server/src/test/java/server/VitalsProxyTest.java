package server;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.messages.Vitals;
import plugins.core.combat.VitalsProxy;


public class VitalsProxyTest {
	private static final Logger logger = LoggerFactory.getLogger(VitalsProxyTest.class);
	
	@Test
	public void TestAddBase() {
		String name = "health";
		Vitals vitals = new Vitals();
		vitals.health = 100;
		VitalsProxy proxy = new VitalsProxy(vitals);
		
		assertThat(proxy.getMax(name)).isEqualTo(100);
		
		
		proxy.addMax(name, 100);
		assertThat(proxy.getMax(name)).isEqualTo(200);
		assertThat(proxy.get(name)).isEqualTo(200);
		
		proxy.set(name, 0);
		proxy.addMax(name, 50);
		assertThat(proxy.getMax(name)).isEqualTo(250);
		assertThat(proxy.get(name)).isEqualTo(50);
	}
	
	@Test
	public void TestSubtractBase() {
		String name = "health";
		Vitals vitals = new Vitals();
		vitals.health = 100;
		VitalsProxy proxy = new VitalsProxy(vitals);
		
		assertThat(proxy.getMax(name)).isEqualTo(100);
		
		proxy.subtractMax(name, 100);
		assertThat(proxy.getMax(name)).isEqualTo(0);
		assertThat(proxy.get(name)).isEqualTo(0);
		
	}
	
	@Test
	public void TestSetBaseIncrease() {
		String name = "health";
		Vitals vitals = new Vitals();
		vitals.health = 100;
		VitalsProxy proxy = new VitalsProxy(vitals);
		
		assertThat(proxy.getMax(name)).isEqualTo(100);
		
		proxy.setMax(name, 200);
		assertThat(proxy.getMax(name)).isEqualTo(200);
		assertThat(proxy.get(name)).isEqualTo(200);
	}
	
	@Test
	public void TestSetBaseDecrease() {
		String name = "health";
		Vitals vitals = new Vitals();
		vitals.health = 100;
		VitalsProxy proxy = new VitalsProxy(vitals);
		
		assertThat(proxy.getMax(name)).isEqualTo(100);
		
		proxy.setMax(name, 50);
		assertThat(proxy.getMax(name)).isEqualTo(50);
		assertThat(proxy.get(name)).isEqualTo(50);
	}
}
