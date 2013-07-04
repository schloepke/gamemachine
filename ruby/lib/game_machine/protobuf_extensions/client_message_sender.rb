module GameMachine
  module ProtobufExtensions
    module ClientMessageSender
      def send_to_client(sender=nil)
        Actor.find(self.client_connection.gateway).tell(self,sender)
      end
    end
  end
end

ClientMessage.send(:include, GameMachine::ProtobufExtensions::ClientMessageSender)
