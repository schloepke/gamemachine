module GameMachine
  module Systems
    class RequestHandler < Actor

      def on_receive(message)
        if message.is_a?(ClientMessage)
          entity_list = client_message_to_entity_list(message)
          reset_player_authentication(entity_list.get_player)
          actor_ref = AuthenticationHandler.find_distributed_local(entity_list.player.id)
          actor_ref.send_message(entity_list,:sender => get_self)
        else
          unhandled(message)
        end
      end

      def reset_player_authentication(player)
        player.set_authenticated(false)
      end

      def client_message_to_entity_list(message)
        entity_list = EntityList.parse_from(message.data)
        entity_list.get_entity_list.each do |entity|
          entity.set_client_connection(message.client_connection)
        end
        entity_list
      end

    end
  end
end
