package io.gamemachine.process;

public class CmdlineProcessRunner {
	private static ProcessManager manager;

	private static long checkInterval = 10000l;

	public static void main(String[] args) throws InterruptedException {
		manager = new ProcessManager(true);
		manager.startAll();

		while (true) {

			manager.checkStatus();
			Thread.sleep(checkInterval);
		}

	}
}
