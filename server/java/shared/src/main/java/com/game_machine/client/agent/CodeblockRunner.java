package com.game_machine.client.agent;

import java.security.Permissions;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import Client.Messages.Agent;
import akka.actor.UntypedActor;

import com.game_machine.client.Api;
import com.game_machine.shared.codeblocks.Codeblock;
import com.game_machine.shared.codeblocks.CodeblockCompiler;
import com.game_machine.shared.codeblocks.CodeblockExecutor;


public class CodeblockRunner extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(CodeblockRunner.class);
	private Codeblock codeblock;
	private CodeblockExecutor executor;
	private CodeblockEnv codeblockEnv;
	private String classname;
	private String agentId;
	private String encodedCompileResult;

	public CodeblockRunner(Api api, Agent agent) {
		Permissions permissions = new Permissions();
		permissions.add(new java.net.SocketPermission("*:24130", "connect,resolve"));
		this.executor = new CodeblockExecutor();
		this.executor.setPerms(permissions);
		this.classname = agent.getClassname();
		this.encodedCompileResult = agent.getCompileResult();
		this.agentId = agent.getId();
		this.codeblockEnv = new CodeblockEnv(api,this,agent.getId());
		updateCodeblock();
	}

	private void updateCodeblock() {
		CodeblockCompiler.CompileResult compileResult = CodeblockCompiler.CompileResult.decode(this.encodedCompileResult);
		if (compileResult.isCompiled()) {
			Codeblock codeblock = CodeblockCompiler.load(compileResult);
			logger.info("New codeblock " + codeblock.toString());
			if (codeblock != null) {
				if (this.codeblock != null) {
					this.codeblockEnv.incReloadCount();
				}
				this.codeblock = codeblock;
				return;
			}
		} else {
			for (String error : compileResult.getErrors()) {
				logger.warn(error);
			}
		}
		this.codeblock = null;
		this.codeblockEnv.resetReloadCount();
		logger.warn("Agent "+agentId+": Failed to compile codeblock");
	}

	@Override
	public void postStop() {
		logger.info(agentId+" stopped");
	}
	
	@Override
	public void preStart() {
		logger.warn("Agent "+agentId+": starting");
		if (this.codeblock == null) {
			logger.warn("Agent "+agentId+": Codeblock in prestart is null");
			return;
		}
		this.executor.runRestricted(this.codeblock, "awake", codeblockEnv);
	}

	public void tick(int delay, Object message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Agent) {
			Agent agent = (Agent)message;
			if (!this.classname.equals(agent.getClassname())) {
				logger.info("Agent "+agentId+": class change");
				this.classname = agent.getClassname();
				this.encodedCompileResult = agent.getCompileResult();
				updateCodeblock();
				this.executor.runRestricted(this.codeblock, "awake", codeblockEnv);
			}
			return;
		}

		if (this.codeblock == null) {
			logger.warn("Agent "+agentId+": Codeblock in onReceive is null");
			return;
		}

		logger.debug("Agent "+agentId+": running codeblock with "+message.toString());
		this.executor.runRestricted(this.codeblock, "run", message);
	}
}
