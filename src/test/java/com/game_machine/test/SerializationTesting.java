package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.game_machine.Entity;
import com.game_machine.MessageBuilder;
import com.game_machine.ModelTest;

public class SerializationTesting {

	@Test
	public void test1() {
		LinkedBuffer buffer = LinkedBuffer.allocate(512);
		OutputStream out =null;
		
		ModelTest model = new ModelTest("TEST");
		Entity entity = new Entity();
		entity.model = model;
		Schema<Entity> schema = RuntimeSchema.getSchema(Entity.class);
		//MessageBuilder.encodeMessage(new ModelTest("TEST"), ModelTest.class);
		try
		{
			byte[] protostuff = ProtobufIOUtil.toByteArray(entity, schema, buffer);
		    buffer.clear();
		    
		    Entity entity2 = new Entity();
		    assertThat(entity2.model).isEqualTo(null);
		    ProtobufIOUtil.mergeFrom(protostuff, entity2, schema);
		    assertThat(entity2.model.getClass().getSimpleName()).isEqualTo("ModelTest");
		    assertThat(entity2.model.getName()).isEqualTo("TEST");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
