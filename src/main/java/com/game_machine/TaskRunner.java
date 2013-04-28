package com.game_machine;

public class TaskRunner {

	protected Runnable wrapTask(final Runnable task) {
	    return new Runnable() {
	      public void run() {
	        Thread currentThread = Thread.currentThread();
	        String threadName = currentThread.getName();
	        try {
	          task.run();
	        } catch (Throwable t) {
	         // reportException(t);
	        } finally {
	          if (!threadName.equals(currentThread.getName())) {
	            currentThread.setName(threadName);
	          }
	        }
	      }
	    };
	  }
}
