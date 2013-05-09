package com.game_machine;

import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;


public class Ruby {

	@SuppressWarnings("unchecked")
	public static <T extends Object> T run(String filename, String methodName, Object[] methodArgs, Class<?> returnType) {
		filename = FileUtils.join(System.getProperty("user.dir"),"src","main","ruby",filename);
		ScriptingContainer container = new ScriptingContainer();
		Object receiver = container.runScriptlet(PathType.ABSOLUTE, filename);
		return (T) container.callMethod(receiver, methodName, methodArgs, returnType);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Object> T run(String filename, String methodName, Class<?> returnType) {
		filename = FileUtils.join(System.getProperty("user.dir"),"src","main","ruby",filename);
		ScriptingContainer container = new ScriptingContainer();
		Object receiver = container.runScriptlet(PathType.ABSOLUTE, filename);
		return (T) container.callMethod(receiver, methodName, returnType);
	}
}