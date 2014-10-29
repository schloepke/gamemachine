package com.game_machine.codeblocks;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;

public class CodeblockExecutor {

	private AccessControlContext context; 
	
	// Default permissions
	public void setPerms() {
		Permissions permissions = new Permissions();
		//permissions.add(new RuntimePermission("accessDeclaredMembers"));
        ProtectionDomain protectionDomain =
	    new ProtectionDomain(null, permissions);
        context = new AccessControlContext(
            new ProtectionDomain[] { protectionDomain });
	}
	
	public void setPerms(Permissions permissions) {
        ProtectionDomain protectionDomain =
	    new ProtectionDomain(null, permissions);
        context = new AccessControlContext(
            new ProtectionDomain[] { protectionDomain });
	}
	
	public boolean runUnrestricted(Codeblock codeblock, String method, Object message) {
		try {
			if (method.equals("run")) {
	    		codeblock.run(message);
	    	} else if (method.equals("awake")) {
	    		codeblock.awake(message);
	    	}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean runRestricted(Codeblock codeblock, String method, Object message) {
		try {
			run(codeblock, method, message);
			return true;
		} catch (AccessControlException | PrivilegedActionException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void run(final Codeblock codeblock, final String method, final Object message) throws PrivilegedActionException {
		AccessController.doPrivileged(new PrivilegedExceptionAction<Void> () {
		    public Void run() throws Exception {
		    	if (method.equals("run")) {
		    		codeblock.run(message);
		    	} else if (method.equals("awake")) {
		    		codeblock.awake(message);
		    	}
				return null;
		    }
		}, context);
	}
}
