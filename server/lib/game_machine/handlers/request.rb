module GameMachine
  module Handlers
    class Request < Actor::Base
      include Commands

      def post_init(*args)
        @auth_handler = Authentication.new
      end

      def on_receive(message)
        if message.is_a?(MessageLib::ClientMessage)
          if message.has_player_logout
            if Authentication.authenticated?(message.player)
              self.class.unregister_client(message)
            end
          elsif message.has_player
             update_entities(message)
            if Authentication.authenticated?(message.player)
              game_handler.tell(message)
            else
              if ENV['CLUSTER_TEST']
                player_service = GameMachine::JavaLib::PlayerService.get_instance
                unless player = player_service.find(message.player.id)
                  player_service.create(message.player.id,'cluster_test')
                end
              end
              if @auth_handler.authenticate!(message.player)
                register_client(message)
                game_handler.tell(message)
              else
                self.class.logger.info("Authentication failed for #{message.player.id}.  authtoken=#{message.player.authtoken}")
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

      def update_entities(message)
        if message.get_entity_list
          message.get_entity_list.each do |entity|
            entity.set_player(message.player)
          end
        end
      end

      def register_client(message)
        self.class.logger.debug "Register #{message.player.id}"
        player_id = message.player.id
        client_id = message.client_connection.id
        #GameMachine.logger.info "player_id=#{message.player.id} client_id=#{client_id}"
        register = MessageLib::ClientManagerRegister.new.
          set_register_type('client').set_name(client_id)
        entity = MessageLib::Entity.new.set_id(client_id).
          set_client_manager_register(register).set_client_connection(
          message.client_connection
        )
        entity.set_player(message.player)
        ClientManager.find.ask(entity,50)
      end

      def self.unregister_client(message)
        logger.debug "Unregister #{message.player.id}"
        player_id = message.player.id
        client_id = message.client_connection.id
        Authentication.unregister_player(player_id)
        entity = MessageLib::Entity.new.set_id(client_id).set_client_manager_unregister(
          MessageLib::ClientManagerUnregister.new.set_register_type('client').
            set_name(client_id)
        ).set_client_connection(message.client_connection)
        entity.set_player(message.player)
        ClientManager.find.tell(entity)

        ## Just for now, need to do this correctly by registering with the client manager
        chat_destroy = MessageLib::ChatDestroy.new.set_player_id(message.player.id)
        GameSystems::ChatManager.find.tell(chat_destroy)
      end

      def game_handler
        @game_handler ||= Handlers::Game.find
      end

    end
  end
end
