package com.game_machine.entity_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtils {

	public static String userDir() {
		return System.getProperty("user.dir");
	}
	
	public static String readLocalFile(String path) {
		path = FileUtils.join(userDir(), path);
		return readfile(path);
	}

	public static String readfile(String path) {
		String output = null;
		try {
			output = new Scanner(new File(path)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File Not Found "+ path);
		}
		return output;
	}

	public static String join(String... paths) {
		if (paths.length == 0) {
			return "";
		}

		File combined = new File(paths[0]);

		int i = 1;
		while (i < paths.length) {
			combined = new File(combined, paths[i]);
			++i;
		}

		return combined.getPath();
	}

}
