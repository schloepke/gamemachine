package com.game_machine.systems;

import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
 
public class UdpConsumer extends UntypedConsumerActor{
  private String uri;
 
  public String getEndpointUri() {
    return uri;
  }
 
  public void onReceive(Object message) throws Exception {
    if (message instanceof CamelMessage) {
      /* ... */
    } else
      unhandled(message);
  }
 
  // Extra constructor to change the default uri,
  // for instance to "jetty:http://localhost:8877/example"
  public void UdpConsumer(String uri) {
    this.uri = uri;
  }
 
  public void UdpConsumer() {
    this.uri = "netty:udp://localhost:1234/";
  }
}