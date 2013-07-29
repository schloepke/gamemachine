module GameMachine
  module ProtobufExtensions
    module ClientMessageSender
      def send_to_player
        if self.player.nil? || self.player.id.nil?
          GameMachine.logger.info "player/player_id not set on ClientMessage!"
        end

        player = self.player
        if client_connection = PlayerRegistry.client_connection_for(player.id)
          self.set_client_connection(client_connection)
          Actor::Base.find(client_connection.gateway).tell(self)
        else
          GameMachine.logger.info "Unable to get client_connection for player_id #{player.id}"
        end
      end
    end
  end
end

ClientMessage.send(:include, GameMachine::ProtobufExtensions::ClientMessageSender)
