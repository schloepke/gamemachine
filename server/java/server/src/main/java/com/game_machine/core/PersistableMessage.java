package com.game_machine.core;

import java.io.UnsupportedEncodingException;

public interface PersistableMessage {
	String getId();
	byte[] toByteArray();
	String toJson();
}
