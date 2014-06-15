module GameMachine
  module Handlers
    class Request < Actor::Base

      def post_init(*args)
        @auth_handler = Authentication.new
      end

      def on_receive(message)
        if message.is_a?(MessageLib::ClientMessage)
          if message.has_player_logout
            if Authentication.authenticated?(message.player)
              unregister_client(message)
            end
          elsif message.has_player
            update_entities(message)
            if Authentication.authenticated?(message.player)
              game_handler.tell(message)
            else
              if @auth_handler.authenticate!(message.player)
                register_client(message)
                game_handler.tell(message)
              end
            end
          else
            unhandled(message)
          end
        else
          unhandled(message)
        end
      end

      private

      def register_client(message)
        GameMachine.logger.info "Register #{message.player.id}"
        player_id = message.player.id
        client_id = message.client_connection.id
        register = MessageLib::ClientManagerRegister.new.
          set_register_type('client').set_name(client_id)
        entity = MessageLib::Entity.new.set_id(client_id).
          set_client_manager_register(register).set_client_connection(
          message.client_connection
        )
        entity.set_player(message.player)
        ClientManager.find.ask(entity,5000)
      end

      def unregister_client(message)
        player_id = message.player.id
        client_id = message.client_connection.id
        Authentication.unregister_player(player_id)
        entity = MessageLib::Entity.new.set_id(client_id).set_client_manager_unregister(
          MessageLib::ClientManagerUnregister.new.set_register_type('client').
            set_name(client_id)
        )
        entity.set_player(message.player)
        ClientManager.find.tell(entity)
      end

      def game_handler
        @game_handler ||= Handlers::Game.find
      end

      def update_entities(message)
        if message.get_entity_list
          message.get_entity_list.each do |entity|
            entity.set_player(message.player)
          end
        end
      end

    end
  end
end
