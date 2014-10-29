package mypackage;
import java.nio.charset.Charset;
import com.game_machine.codeblocks.Codeblock;
import com.game_machine.client.api.*;
import com.game_machine.client.agent.CodeblockEnv;
import Client.Messages.GameMessage;

public class Myclass implements Codeblock {

  public class MyInnerclass {
    public void testit() {
      System.out.println("MyInnerclass testit");
    }
  }

  static class MyInnerStaticclass {}

  private CodeblockEnv env;
  private Api api;
  private Cloud cloud;

  public void awake(Object message) {
    if (message instanceof CodeblockEnv) {
      this.env = (CodeblockEnv) message;
      System.out.println("I am agent "+this.env.getAgentId());
      this.api = env.getApi();
      this.cloud = api.getCloud();
      if (this.env.getReloadCount() == 0) {
        this.env.tick(100,"tick");
      }
      
    }
  }

  private void sendGameMessage() {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setAgentId(this.env.getAgentId());
    ApiMessage msg = this.api.newMessage();
    msg.setGameMessage(gameMessage);
    msg.setDestination("player#"+this.env.getPlayerId());
    msg.send();
  }

  public void cloudTest() {
    byte[] b = "testing".getBytes(Charset.forName("UTF-8"));
    this.cloud.put("id1",b);
    this.cloud.put("id2","testing");
    Cloud.ByteResponse byteResponse = this.cloud.getBytes("id1");
    Cloud.StringResponse stringResponse = this.cloud.getString("id2");
    this.cloud.delete("id1");
    this.cloud.delete("id2");
  }

  public void run(Object message) throws Exception {
    if (message instanceof String) {
      String msg = (String)message;
      if (msg.equals("tick")) {
        this.env.tick(5000,"tick");
        System.out.println("Codeblock got "+msg);
        sendGameMessage();
        //cloudTest();
      }
    }


    if (message instanceof GameMessage) {
      //System.out.println("Got gamemessage");
    }
    //System.out.println("run Test");
   
    //new MyInnerclass().testit();
  }
}