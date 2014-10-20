package com.game_machine.client.api;

import static com.dyuproject.protostuff.LinkedBuffer.allocate;

import com.dyuproject.protostuff.LinkedBuffer;

public class CachedLinkedBuffer {

	static int bufferSize = 2048;

	private static final ThreadLocal<LinkedBuffer> localBuffer = new ThreadLocal<LinkedBuffer>() {
		@Override
		protected LinkedBuffer initialValue() {
			return allocate(bufferSize);
		}
	};

	public static LinkedBuffer get() {
		return localBuffer.get();
	}
}