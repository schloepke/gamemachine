module GameMachine
  module Systems
    class RequestHandler < Actor

      def on_receive(message)
        if message.is_a?(ClientMessage)
          if message.has_player_login
            LoginHandler.find.tell(message)
          elsif message.has_player
            message.get_entity_list.each do |entity|
              entity.set_client_connection(message.client_connection)
            end
            AuthenticationHandler.find_distributed_local(
              message.player.id
            ).tell(message,self)
          else
            unhandled(message)
          end
        else
          unhandled(message)
        end
      end

    end
  end
end
