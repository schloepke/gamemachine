package io.gamemachine.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;

import com.google.common.base.Strings;


public class ExternalProcess implements Runnable {
	
	public enum OS {
		LINUX,
		WIN
	}
	
	public enum Status {
		NONE,
		DOWN,
		UP,
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ExternalProcess.class);
	
	public String startScript;
	public String executable;
	public Status status;
	public String name;
	public boolean running = false;
	
	public ExternalProcess(String name, String startScript, String executable) {
		this.startScript = startScript;
		this.executable = executable;
		this.name = name;
		this.status = Status.DOWN;
	}
	
	public String name() {
		return name;
	}
	public static OS os() {
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			return OS.WIN;
		} else {
			return OS.LINUX;
		}
	}
		
	public boolean stop() {
		Runtime rt = Runtime.getRuntime();
		if (Strings.isNullOrEmpty(executable)) {
			logger.warn("Invalid executable name "+executable);
			return false;
		}
		
		try {
			String command;
			if (os() == OS.WIN) {
				command = "taskkill /F /IM "+executable;
			} else {
				command = "pkill -f \""+executable+"\"";
			}
			
			logger.debug("Killing "+executable+" with "+command);
			rt.exec(command);
			return true;
		} catch (IOException e) {
			logger.info(e.getMessage(),e);
			return false;
		}
	}
	
	@Override
	public void run() {
		try {
			logger.warn("Process " + executable + " starting at " + startScript);
			ProcessExecutor pex = new ProcessExecutor();
			running = true;
			int rvalue = pex.command(startScript).execute().getExitValue();
			running = false;
			status = Status.DOWN;
			logger.warn("Process "+ startScript+" exit value was " + rvalue);
		} catch (InvalidExitValueException | IOException | InterruptedException | TimeoutException e) {
			logger.debug(e.getMessage(),e);
		}
	}
	
	public boolean isRunning() {
		return running;
//		try {
//			Runtime rt = Runtime.getRuntime();
//			String pattern = "(.*)"+executable+"(.*)";
//			Pattern r = Pattern.compile(pattern);
//			
//	        String line;
//	        String command;
//	        if (os() == OS.WIN) {
//	        	command = "tasklist.exe";
//	        } else {
//	        	command = "ps -e";
//	        }
//	        
//	        Process p = rt.exec(command);
//	        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//	        while ((line = input.readLine()) != null) {
//	        	Matcher m = r.matcher(line);
//	        	if (m.find()) {
//	        		input.close();
//	        		return true;
//	        	}
//	        }
//	        input.close();
//	        return false;
//	    } catch (Exception err) {
//	        logger.info(err.getMessage(),err);
//	        return false;
//	    }
	}
	
	public static int execute(String path) {
		try {
			logger.warn("Process " + path + " starting");
			ProcessExecutor pex = new ProcessExecutor();
			int rvalue = pex.command(path).execute().getExitValue();
			logger.warn("Process "+ path+" exit value was " + rvalue);
			return rvalue;
		} catch (InvalidExitValueException | IOException | InterruptedException | TimeoutException e) {
			logger.debug(e.getMessage(),e);
			return -1;
		}
	}
}
