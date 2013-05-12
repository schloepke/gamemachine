package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;

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
import com.game_machine.proto.PsEntity;
import com.game_machine.proto.PsErrorComponent;
import com.game_machine.proto.PsModelTest;

public class SerializationTesting {

	private static final Logger log = LoggerFactory.getLogger(SerializationTesting.class);
	
	@Test
	public void test1() {
		try {
			LinkedBuffer buffer = LinkedBuffer.allocate(512);

			PsModelTest model = new PsModelTest("TEST");
			PsEntity entity = new PsEntity();
			entity.model = model;
			entity.componentList.add("element1");
			entity.map.put("one", "one");

			PsErrorComponent error = new PsErrorComponent();
			error.setMessage("help!");
			entity.addComponent(error);

			assertThat(entity.componentList.get(0)).isEqualTo("element1");
			assertThat(entity.map.get("one")).isEqualTo("one");
			assertThat(entity.getComponent("PsErrorComponent")).isInstanceOf(PsErrorComponent.class);

			Schema<PsEntity> schema = RuntimeSchema.getSchema(PsEntity.class);
			log.info("SCHEMA="+schema.getFieldName(5));
			byte[] data = ProtobufIOUtil.toByteArray(entity, schema, buffer);
			buffer.clear();

			PsEntity entity2 = new PsEntity();
			assertThat(entity2.model).isEqualTo(null);
			ProtobufIOUtil.mergeFrom(data, entity2, schema);

			assertThat(entity2.getComponent("PsErrorComponent")).isInstanceOf(PsErrorComponent.class);
			assertThat(entity2.componentList.get(0)).isEqualTo("element1");
			assertThat(entity2.map.get("one")).isEqualTo("one");
			assertThat(entity2.model.getClass().getSimpleName()).isEqualTo("PsModelTest");
			assertThat(entity2.model.getName()).isEqualTo("TEST");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
