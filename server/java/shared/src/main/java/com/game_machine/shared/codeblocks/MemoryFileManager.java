package com.game_machine.shared.codeblocks;

import java.util.HashMap;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject.Kind;

public class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
	public Map<String, Output> map = new HashMap<String, Output>();

	MemoryFileManager(JavaCompiler compiler) {
		super(compiler.getStandardFileManager(new DiagnosticCollector<JavaFileObject>(), null, null));
	}

	@Override
	public Output getJavaFileForOutput(Location location, String name, Kind kind, FileObject source) {
		//System.out.println("getJavaFileForOutput called with "+name);
		Output mc = new Output(name, kind);
		this.map.put(name, mc);
		return mc;
	}
}