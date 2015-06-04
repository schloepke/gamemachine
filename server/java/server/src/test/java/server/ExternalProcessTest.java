package server;

import io.gamemachine.process.ExternalProcess;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

@Test
public class ExternalProcessTest {
  
  public void parseProcess() {
	  ExternalProcess process;
	  process = new ExternalProcess("blah","notrunning");
	  assertThat(process.isRunning()).isFalse();
	  
	  process = new ExternalProcess("blah","update-notifier");
	  assertThat(process.isRunning()).isTrue();
  }
}
