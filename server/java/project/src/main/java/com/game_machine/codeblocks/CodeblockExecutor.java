package com.game_machine.codeblocks;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;

import com.game_machine.codeblocks.api.Codeblock;

public class CodeblockExecutor {

	private AccessControlContext context; 
	
	public void setPerms() {
		Permissions permissions = new Permissions();
		//permissions.add(new RuntimePermission("accessDeclaredMembers"));
        ProtectionDomain protectionDomain =
	    new ProtectionDomain(null, permissions);
        context = new AccessControlContext(
            new ProtectionDomain[] { protectionDomain });
	}
	
	public boolean runUnrestricted(Codeblock codeblock, Object message) {
		try {
			codeblock.run(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean runRestricted(Codeblock codeblock, Object message) {
		try {
			run(codeblock,message);
			return true;
		} catch (AccessControlException | PrivilegedActionException e) {
			// Log this somewhere
			return false;
		}catch (Exception e) {
			return false;
		}
	}
	
	public void run(final Codeblock codeblock, final Object message) throws PrivilegedActionException {
		AccessController.doPrivileged(new PrivilegedExceptionAction<Void> () {
		    public Void run() throws Exception {
		    	codeblock.run(message);
				return null;
		    }
		}, context);
	}
}
