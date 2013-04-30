package com.game_machine.systems.memorydb;

public class WriteBehindHandler implements Runnable {

	private Integer writeInterval = 5000;
	private Integer maxWritesPerSecond = 50;
	private Integer msPerWrite = 1000 / maxWritesPerSecond;

	public WriteBehindHandler(Integer minWriteInterval, Integer maxWritesPerSecond) {
		this.writeInterval = minWriteInterval;
		this.maxWritesPerSecond = maxWritesPerSecond;

	}

	public void write(Object object) {

	}

	public Object readFromCache() {
		return null;
	}

	public void run() {
		int writeCount;
		Long timePassed;
		Long intervalStartTime;
		Long startTime;
		Long writeTime;
		Object data;

		while (true) {
			writeCount = 0;
			timePassed = 0L;
			startTime = System.currentTimeMillis();

			try {
				data = readFromCache();
				write(data);
				writeCount++;
				writeTime = System.currentTimeMillis() - startTime;
				if (msPerWrite > writeTime) {
					Thread.sleep(msPerWrite - writeTime);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
