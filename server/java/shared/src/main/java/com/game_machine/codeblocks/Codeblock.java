package com.game_machine.codeblocks;

public interface Codeblock {
	void run(Object message) throws Exception;
	void awake(Object message);
}
