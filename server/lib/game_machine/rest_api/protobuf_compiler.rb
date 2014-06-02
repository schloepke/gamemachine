module GameMachine
  module RestApi
    class ProtobufCompiler < Actor::Base

      def on_receive(message)
        proto_messages = GameMachine::Protobuf::Generate.new(
          GameMachine.app_root
        ).generate
        getSender.tell(proto_messages,get_self)
      end
    end
  end
end
