package com.game_machine.codeblocks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.tools.ToolProvider;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeblockCompiler {

	private static final Logger logger = LoggerFactory.getLogger(CodeblockCompiler.class);

	public static class CompileResult {
		private Map<String, byte[]> classes = new HashMap<String, byte[]>();
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

		public Map<String, byte[]> getClasses() {
			return classes;
		}

		public void setClasses(Map<String, byte[]> classes) {
			this.classes = classes;
		}

		public String encode() {
			Map<String, String> encoded = new HashMap<String, String>();
			for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
				encoded.put(entry.getKey(), encodeBytes(entry.getValue()));
			}
			encoded.put("classname", classname);
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream o;
			try {
				o = new ObjectOutputStream(b);
				o.writeObject(encoded);
				o.close();
				return encodeBytes(b.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		public static CompileResult decode(String encoded) {

			byte[] bytes = decodeString(encoded);
			ByteArrayInputStream b = new ByteArrayInputStream(bytes);
			ObjectInputStream o = null;
			try {
				o = new ObjectInputStream(b);
				Map<String, String> encodedClasses = (Map<String, String>) o.readObject();
				o.close();
				
				CompileResult result = new CompileResult(encodedClasses.remove("classname"));
				Map<String, byte[]> decodedClasses = new HashMap<String, byte[]>();

				for (Map.Entry<String, String> entry : encodedClasses.entrySet()) {
					decodedClasses.put(entry.getKey(), decodeString(entry.getValue()));
				}
				result.setClasses(decodedClasses);
				result.setCompiled(true);
				return result;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
			}

			return null;
		}

		public static String encodeBytes(byte[] bytes) {
			return DatatypeConverter.printBase64Binary(bytes);
		}

		public static byte[] decodeString(String value) {
			return DatatypeConverter.parseBase64Binary(value);
		}

	}

	public static CompileResult compile(String classpath, String code, String classname) {

		CompileResult result = new CompileResult(classname);
		JavaFileObject file = new JavaSourceFromString(classname, code);
		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		MemoryFileManager fileManager = new MemoryFileManager(compiler);

		List<String> options = new ArrayList<String>();
		options.add("-classpath");
		options.add(classpath);

		final JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null,
				compilationUnits);
		if (task.call()) {
			if (fileManager.map.containsKey(classname)) {
				Map<String, byte[]> classes = new HashMap<String, byte[]>();
				for (Map.Entry<String, Output> entry : fileManager.map.entrySet()) {
					classes.put(entry.getKey(), entry.getValue().toByteArray());
				}
				result.setClasses(classes);
				result.setCompiled(true);
			} else {
				result.addError("No output to load for " + classname);
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

	public static Codeblock load(CompileResult compileResult) {
		logger.info("Attempting to load " + compileResult.getClassname());

		try {
			ClassLoader parent = ByteArrayClassLoader.class.getClassLoader();
			final ByteArrayClassLoader classLoader = new ByteArrayClassLoader(compileResult.getClasses(),
					compileResult.getClassname(), parent);

			Class<?> cls = classLoader.loadClass(compileResult.getClassname());
			Codeblock codeblock = (Codeblock) cls.newInstance();
			return codeblock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
