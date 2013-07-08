module GameMachine
  module Systems
    class RequestHandler < Actor

      def on_receive(message)
        if message.is_a?(ClientMessage)
          if message.has_player_logout
            logged_out(message)
          elsif message.has_client_disconnect
            disconnected(message)
          elsif message.has_player
            update_entities(message)
            authenticate_player(message)
          else
            unhandled(message)
          end
        else
          unhandled(message)
        end
      end

      private

      def logged_out(message)
        PlayerRegistry.player_logout(
          message.player_logout.player_id
        )
      end

      def disconnected(message)
        PlayerRegistry.client_disconnect(
          message.client_disconnect.client_connection
        )
      end

      def update_entities(message)
        if message.get_entity_list
          message.get_entity_list.each do |entity|
            entity.set_player(message.player)
          end
        end
      end

      def authenticate_player(message)
        AuthenticationHandler.find_distributed_local(
          message.player.id
        ).tell(message,self)
      end

    end
  end
end
