package io.gamemachine.codeblocks;

public interface Codeblock {
	void run(Object message) throws Exception;
	void awake(Object message);
	void shutdown(Object message) throws Exception;
}
