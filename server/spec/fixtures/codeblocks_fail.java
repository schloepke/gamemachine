package mypackage;
import java.io.File;
import java.io.IOException;
import com.game_machine.shared.codeblocks.Codeblock;
import com.game_machine.client.Api;
import com.game_machine.client.ApiMessage;
import com.game_machine.client.agent.CodeblockEnv;
import GameMachine.Messages.GameMessage;

public class Myclass implements Codeblock {

  public class MyInnerclass {
    public void testit() {
      System.out.println("MyInnerclass testit");
    }
  }

  static class MyInnerStaticclass {}

  private CodeblockEnv env;
  private Api api;

  public void awake(Object message) {
    if (message instanceof CodeblockEnv) {
      this.env = (CodeblockEnv) message;
      System.out.println("I am agent "+this.env.getAgentId());
      this.api = env.getApi();
      if (this.env.getReloadCount() == 0) {
        this.env.tick(100,"tick");
      }
      
    }
  }

  private void sendGameMessage() {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setAgentId("agent1dd");
    ApiMessage msg = this.api.newMessage();
    msg.setGameMessage(gameMessage);
    msg.setDestination("player#controller2");
    msg.send();
  }

  public void run(Object message) throws Exception {
    if (message instanceof String) {
      String msg = (String)message;
      if (msg.equals("tick")) {
        this.env.tick(5000,"tick");
        System.out.println("Codeblock got "+msg);
        sendGameMessage();
      }
      System.setProperty("testing","true");
      File file = new File("/tmp/testfile");
      file.createNewFile();
    }

    if (message instanceof GameMessage) {
      System.out.println("Got gamemessage");
    }
    System.out.println("run Test");
   
    new MyInnerclass().testit();
  }
}