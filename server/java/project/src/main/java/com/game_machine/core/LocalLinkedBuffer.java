package com.game_machine.core;

import static com.dyuproject.protostuff.LinkedBuffer.allocate;
import com.dyuproject.protostuff.LinkedBuffer;

public class LocalLinkedBuffer {

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
