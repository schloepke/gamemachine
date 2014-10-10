package com.game_machine.codeblocks;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.codeblocks.api.Codeblock;

public class CodeblockCompiler {

	private static final Logger logger = LoggerFactory.getLogger(CodeblockCompiler.class);
	
	public static boolean compile(String outdir, String filename) {

		logger.info("Classpath="+System.getProperty("java.class.path"));
		logger.info("Compiling " + filename + " to " + outdir);
		File sourceFile = new File(filename);
		File[] javaFiles = new File[] { sourceFile };
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays
				.asList(javaFiles));

		List<String> options = new ArrayList<String>();
		options.add("-d");
		options.add(outdir);
		//options.add("-classpath");
		//options.add(System.getProperty("java.class.path"));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null,
				compilationUnits);
		if (task.call()) {
			return true;
		} else {
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(),
						((FileObject) diagnostic.getSource()).toUri());
				System.out.println(diagnostic.getMessage(null));
			}
			return false;
		}

	}

	public static Codeblock tryload(String path, String classname) {
		logger.info("Attempting to load "+classname+" from "+path);
		
		File bin = new File(path);
		CodeblockClassLoader classLoader;
		
		try {
			ClassLoader parent = CodeblockCompiler.class.getClassLoader();
			classLoader = new CodeblockClassLoader(new URL[] { bin.toURI().toURL() },parent);
			
			Class<?> cls = classLoader.loadClass(classname);
			classLoader.close();
			Codeblock codeblock = (Codeblock) cls.newInstance();
			return codeblock;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
