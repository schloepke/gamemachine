package com.game_machine.codeblocks;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static class CompileResult {
		private Map<String, Output> classes = new HashMap<String, Output>();
		private String classname;
		private boolean compiled = false;
		private List<String> errors = new ArrayList<String>();
		
		public CompileResult(String classname) {
			this.setClassname(classname);
		}

		public void addError(String error) {
			this.errors.add(error);
		}
		
		public List<String> getErrors() {
			return this.errors;
		}

		public String getClassname() {
			return classname;
		}

		public void setClassname(String classname) {
			this.classname = classname;
		}

		public boolean isCompiled() {
			return compiled;
		}

		public void setCompiled(boolean compiled) {
			this.compiled = compiled;
		}

		public Map<String, Output> getClasses() {
			return classes;
		}

		public void setClasses(Map<String, Output> classes) {
			this.classes = classes;
		}
	}

	public static CompileResult memoryCompile(String classpath, String code, String classname) {

		CompileResult result = new CompileResult(classname);
		JavaFileObject file = new JavaSourceFromString(classname, code);
		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		MemoryFileManager fileManager = new MemoryFileManager(compiler);

		List<String> options = new ArrayList<String>();
		options.add("-classpath");
		options.add(classpath);
		
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null,
				compilationUnits);
		if (task.call()) {
			if (fileManager.map.containsKey(classname)) {
				result.setClasses(fileManager.map);
				result.setCompiled(true);
			} else {
				result.addError("No output to load for "+classname);
			}
		} else {
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				result.addError(String.format("Error on line %d in %s%n", diagnostic.getLineNumber(),
						((FileObject) diagnostic.getSource()).toUri()));
				result.addError(diagnostic.getMessage(null));
			}
		}
		return result;
	}

	public static Codeblock loadFromMemory(CompileResult compileResult) {
		logger.info("Attempting to load " + compileResult.getClassname());

		ByteArrayClassLoader classLoader;

		try {
			ClassLoader parent = ByteArrayClassLoader.class.getClassLoader();
			classLoader = new ByteArrayClassLoader(compileResult.getClasses(), compileResult.getClassname(), parent);

			Class<?> cls = classLoader.loadClass(compileResult.getClassname());
			Codeblock codeblock = (Codeblock) cls.newInstance();
			return codeblock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean fileCompile(String outdir, String filename) {

		logger.info("Classpath=" + System.getProperty("java.class.path"));
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
		// options.add("-classpath");
		// options.add(System.getProperty("java.class.path"));
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
		logger.info("Attempting to load " + classname + " from " + path);

		File bin = new File(path);
		CodeblockClassLoader classLoader;

		try {
			ClassLoader parent = CodeblockCompiler.class.getClassLoader();
			classLoader = new CodeblockClassLoader(new URL[] { bin.toURI().toURL() }, parent);

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
