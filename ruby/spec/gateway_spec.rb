require 'spec_helper'

module GameMachine
  describe  'Gateway' do
    before(:each) do
      Server.new.start_actor_system
    end
    after(:each) do
      Server.new.stop_actor_system
    end

    let(:client_id) {"localhost:8100"}
    let(:bytes) {"test".to_java_bytes}
    let(:net_message) do
      NetMessage.new(nil,NetMessage::UDP, bytes,'localhost',8100,nil)
    end

    let(:gateway_message) do
      GatewayMessage.new(bytes,client_id)
    end

    subject do
      props = Props.new(Gateway);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, Gateway.name);
      ref.underlying_actor
    end

    describe "#send_to_client" do
      it "sends a GatewayMessage to onReceive" do
        subject.should_receive(:onReceive)
        Gateway.send_to_client('test',bytes)
      end
    end

    describe '#onReceive' do
      context "message is a NetMessage" do
        let(:handler) {mock("Handler")}

        it "should create a gateway message and send it to the game handler" do
          ActorUtil.stub(:getSelectionByName).and_return handler
          handler.should_receive(:tell).with(kind_of(GatewayMessage),subject.getSelf)
          subject.onReceive(net_message)
        end
      end

      context "message is a GatewayMessage" do
        let(:server) {mock("Server")}

        context "netmessage is udp" do
          it "should send udp message" do
            UdpServer.stub(:get_udp_server).and_return server
            server.should_receive(:send_to_client)
            subject.stub(:get_client).and_return net_message
            subject.onReceive(gateway_message)
          end
        end

        context "netmessage is udt" do
          let(:net_message) do
            NetMessage.new(nil,NetMessage::UDT, bytes,'localhost',8100,nil)
          end

          it "should send udt message" do
            UdtServer.stub(:get_udt_server).and_return server
            server.should_receive(:send_to_client)
            subject.stub(:get_client).and_return net_message
            subject.onReceive(gateway_message)
          end
        end
      end

    end
  end
end
