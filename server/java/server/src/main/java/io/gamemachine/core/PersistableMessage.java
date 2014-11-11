package io.gamemachine.core;

import java.io.UnsupportedEncodingException;

public interface PersistableMessage {
	String getId();
	byte[] toByteArray();
	String toJson();
}
