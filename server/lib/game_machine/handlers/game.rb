module GameMachine
  module Handlers
    class Game < Actor::Base 
      include Commands

      attr_reader :destinations, :game_message_routes
      def post_init(*args)
        @game_message_routes = Routes.instance.game_message_routes
        @destinations = {}
      end


      def on_receive(message)
        if message.is_a?(MessageLib::ClientMessage)
          if message.get_entity_list
            dispatch_entities(message.get_entity_list)
          end
        else
          unhandled(message)
        end
      end

      private

      def dispatch_entities(entities)
        entities.each do |entity|
          if entity.save
            entity = GameSystems::ObjectDbProxy.save_entity(entity)
          end

          if entity.has_game_messages
            entity.game_messages.get_game_message_list.each do |game_message|
              if game_message.has_destination_id
                destination = game_message.destination_id
              elsif game_message.has_destination
                destination = game_message.destination
              else
                GameMachine.logger.warn "Unable to find destination for game message, skipping"
                next 
              end

              if route = game_message_routes.fetch(destination)
                game_message.set_player_id(entity.player.id)
                if route[:distributed]
                  Actor::Base.find_distributed(entity.player.id,route[:to]).tell(game_message)
                else
                  Actor::Base.find(route[:to]).tell(game_message)
                end
              else
                GameMachine.logger.warn "No route for destination #{destination}"
              end
            end
          end

          if entity.has_destination
            unless actor_ref = destinations.fetch(entity.destination,nil)
              destination = entity.destination.gsub('/','::')
              actor_ref = Actor::Base.find(destination)
              destinations[entity.destination] = actor_ref
            end
            entity.set_destination(nil)
            actor_ref.tell(entity)
            GameMachine.logger.debug("RouteToDestination: #{entity.id} #{destination}")
            next
          end

          # TODO: Optimize this
          component_names = entity.component_names
          GameMachine.logger.debug("Dispatch: #{entity} #{component_names.to_a.inspect}")
          next if component_names.empty?
          Application.registered.each do |klass|
            dispatched = false
            klass.aspects.each do |aspect|
              next if dispatched
              if (aspect & component_names).size == aspect.size
                GameMachine.logger.debug "Routing #{entity} via #{aspect} #{component_names} to #{klass}"
                klass.find.tell(entity)
                dispatched = true
              end
            end
          end
        end
      end

    end
  end
end

