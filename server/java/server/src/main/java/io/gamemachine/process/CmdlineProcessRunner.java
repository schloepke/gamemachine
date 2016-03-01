package io.gamemachine.process;

public class CmdlineProcessRunner {
    private static UnityProcessManager manager;

    private static long checkInterval = 10000l;

    public static void main(String[] args) throws InterruptedException {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                UnityProcessManager.stopAll();
            }
        });

        manager = new UnityProcessManager(true);
        manager.startAll();

        while (true) {

            manager.checkStatus();
            Thread.sleep(checkInterval);
        }

    }
}
