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
          dispatch_entity(entity)
        end
      end

      def dispatch_entity(entity)
        if entity.has_destination
          return dispatch_by_destination(entity)
        end

        if entity.has_game_messages
          dispatch_by_game_message(entity)
        end

        dispatch_by_component(entity)
      end

      def destination_to_player(destination,sender_id)
        unless destination.match(/^player\//)
          return false
        end

        player_service = JavaLib::PlayerService.get_instance
        null,recipient,agent = destination.split('/')
        if recipient_player = player_service.find(recipient)
          player_game_id = player_service.get_game_id(sender_id)
          if recipient_player.get_game_id == player_game_id
            return {:recipient => recipient_player, :agent => agent}
          else
            self.class.logger.info("Destination player #{recipient_player.id} game id does not match sender")
          end
        end
        return false
      end

      def dispatch_by_destination(entity)

        if result = destination_to_player(entity.destination,entity.player.id)
          recipient_player = result[:recipient]
          player_id = entity.player.id
          recipient_player = MessageLib::Player.new.set_id(recipient_player.id)
          entity.set_player(recipient_player).set_sender_id(player_id)
          commands.player.send_message(entity,recipient_player.id)
          self.class.logger.debug("Message sent to player destination #{recipient_player.id}")
          return
        end

        # Only allow direct routing to specific systems
        unless entity.destination.match(/RemoteEcho/) || entity.destination.match(/Devnull/) ||
          entity.destination.match(/TeamManager/) || entity.destination.match(/RegionService/)
          self.class.logger.info("Bad destination #{entity.destination}")
          return
        end

        unless actor_ref = destinations.fetch(entity.destination,nil)
          destination = entity.destination.gsub('/','::')
          actor_ref = Actor::Base.find(destination)
          destinations[entity.destination] = actor_ref
        end
        entity.set_destination(nil)
        actor_ref.tell(entity)
        self.class.logger.debug("RouteToDestination: #{entity.id} #{destination}")
      end

      def dispatch_by_game_message(entity)
        entity.game_messages.get_game_message_list.each do |game_message|
          if game_message.has_destination_id
            destination = game_message.destination_id
          elsif game_message.has_destination
            destination = game_message.destination
            if result = destination_to_player(destination,entity.player.id)
              recipient_player = result.fetch(:recipient)
              if agent = result.fetch(:agent,nil)
                game_message.set_agent_id(agent)
              end
              commands.player.send_game_message(game_message,recipient_player.id)
              self.class.logger.debug("GameMessage to player: #{recipient_player.id} agent: #{game_message.get_agent_id}")
              next
            end
          else
            self.class.logger.warn "Unable to find destination for game message, skipping"
            next 
          end

          if route = game_message_routes.fetch(destination,nil)
            game_message.set_player_id(entity.player.id)
            if route[:distributed]
              Actor::Base.find_distributed(entity.player.id,route[:to]).tell(game_message)
            else
              Actor::Base.find(route[:to]).tell(game_message)
            end
          else
            self.class.logger.warn "No route for destination #{destination}"
          end
        end
      end

      def dispatch_by_component(entity)
        # DEPRECATED - convert to named routes as we do for game messages
        component_names = entity.component_names
        GameMachine.logger.debug("Dispatch: #{entity} #{component_names.to_a.inspect}")
        next if component_names.empty?
        Application.registered.each do |klass|
          dispatched = false
          klass.aspects.each do |aspect|
            next if dispatched
            if (aspect & component_names).size == aspect.size
              self.class.logger.debug "Routing #{entity} via #{aspect} #{component_names} to #{klass}"
              klass.find.tell(entity)
              dispatched = true
            end
          end
        end
      end

    end
  end
end

