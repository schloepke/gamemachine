package com.game_machine.codeblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ByteArrayClassLoader extends ClassLoader {

	private Map<String, Output> classes;
	private String classname;

	private static List<String> allowedPackages = new ArrayList<String>() {
		{
			add("java.lang");
			add("java.io");
			add("java.math");
			add("java.net");
			add("java.nio");
			add("java.text");
			add("java.util");
			add("javax.crypto");
			add("javax.xml");
			add("com.game_machine.codeblocks.api");
			add("com.game_machine.client.Api");
			add("com.game_machine.client.agent.CodeblockEnv");
			add("com.game_machine.client.agent.CodeblockRunner");
		}
	};

	public ByteArrayClassLoader(Map<String, Output> classes, String classname, ClassLoader parent) {
		super(parent);
		this.classes = classes;
		this.classname = classname;
	}

	public List<String> getAllowedPackages() {
		return allowedPackages;
	}

	public static void addAllowedPackage(String allowedPackage) {
		allowedPackages.add(allowedPackage);
	}

	private Class<?> getClass(String name) {
		byte[] code = classes.remove(name).toByteArray();
		Class<?> c = defineClass(name, code, 0, code.length);
		resolveClass(c);
		return c;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		System.out.println("loadClass " + name);
		if (classes.containsKey(name)) {
			return getClass(name);
		} else if (name.startsWith("user.codeblocks")) {
			return findClass(name);
		} else {
			for (String allowedPackage : allowedPackages) {
				if (name.startsWith(allowedPackage)) {
					return super.loadClass(name);
				}
			}
			return findClass(name);

		}
	}
}
