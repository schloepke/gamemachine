package com.game_machine.codeblocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeblockSecurityManager extends SecurityManager {
	
	private static final Logger logger = LoggerFactory.getLogger(CodeblockSecurityManager.class);
	
	private boolean isRestricted() {
		for (Class<?> cls : getClassContext())
			if (cls.getClassLoader() instanceof CodeblockClassLoader)
				return true;
		return false;
	}
	// Implement other checks based on isRestricted().
	
}