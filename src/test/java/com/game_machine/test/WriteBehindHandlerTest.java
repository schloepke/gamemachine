package com.game_machine.test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.game_machine.ActorUtil;
import com.game_machine.persistence.GameObject;
import com.game_machine.persistence.WriteBehindActor;
import com.game_machine.persistence.WriteBehindHandler;

import static org.fest.assertions.api.Assertions.*;

public class WriteBehindHandlerTest {

	public void sleep(Integer ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void firstWriteShouldBeTrue() {
		
		WriteBehindHandler handler = new WriteBehindHandler(2000, 50);
		GameObject message = new GameObject();
		message.setId("test");
		Integer count = 0;
		assertThat(handler.write(message)).isTrue();
	}
	
	@Test
	public void shouldOnlyWriteSameKeyOnceEveryInterval() {
		WriteBehindHandler handler = new WriteBehindHandler(1000, 50);
		GameObject message = new GameObject();
		message.setId("test");
		Integer count = 0;
		for(int i=0;i<210;i++) {
			if (handler.write(message)) {
				count++;
			}
			sleep(10);
		}
		assertThat(count).isEqualTo(3);
	}
	
	@Test
	public void shouldLimitTotalWrites() {
		WriteBehindHandler handler = new WriteBehindHandler(2000, 50);
		GameObject message = new GameObject();
		Integer count = 0;
		for(int i=0;i<100;i++) {
			message.setId(Integer.toString(i));
			if (handler.write(message)) {
				count++;
			}
			sleep(10);
		}
		
		assertThat(count).isEqualTo(50);
		
		
		
	}

	// @Test
	public static void example() {
		try {
			ActorSystem system = ActorUtil.createSystem("system");
			final Props props = Props.create(WriteBehindActor.class);
			final TestActorRef<WriteBehindActor> ref = TestActorRef.create(system, props, "testA");
			final WriteBehindActor actor = ref.underlyingActor();

			GameObject o = new GameObject();
			o.setId("test");
			final Future<Object> future = akka.pattern.Patterns.ask(ref, o, 3000);

			assertThat(future.isCompleted()).isFalse();
			// assertTrue(future.isCompleted());
			Await.result(future, Duration.create(5, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
