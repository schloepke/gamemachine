package io.gamemachine.codeblocks;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class CodeblockClassLoader extends URLClassLoader {
	
	@SuppressWarnings("serial")
	private static List<String> allowedPackages = new ArrayList<String>(){{
		add("java.lang");
		add("java.io");
		add("java.math");
		add("java.net");
		add("java.nio");
		add("java.text");
		add("java.util");
		add("javax.crypto");
		add("javax.xml");
		add("user.");
		add("org.");
		add("com.");
		add("io.");
	}};
		
	public CodeblockClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public List<String> getAllowedPackages() {
		return allowedPackages;
	}
	
	public static void addAllowedPackage(String allowedPackage) {
		allowedPackages.add(allowedPackage);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		//System.out.println("loadClass "+name);
		if (name.startsWith("user.codeblocks")) {
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
