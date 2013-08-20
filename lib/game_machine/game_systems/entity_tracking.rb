module GameMachine
  module GameSystems
    class EntityTracking < Actor::Base

      aspect %w(TrackEntity)
      aspect %w(GetNeighbors)

      attr_reader :grid

      def post_init(grid=default_grid)
        @scheduler = get_context.system.scheduler
        @dispatcher = get_context.system.dispatcher
        @entity_updates = []
        @grid = grid
        entity_updates = Subscribe.new.set_topic('entity_location_updates')
        MessageQueue.find.tell(entity_updates,self)
        schedule_update
      end

      def schedule_update
        duration = GameMachine::JavaLib::Duration.create(100, java.util.concurrent.TimeUnit::MILLISECONDS)
        @scheduler.schedule(duration, duration, get_self, "send_entity_updates", @dispatcher, nil)
      end

      def on_receive(message)

        # Ignore our own publishes
        if get_sender == get_self
          return
        end

        if message.is_a?(Entity)
          if message.track_entity
            entity = entity_from_message(message)

            if entity.nil?
              unhandled(message)
              return
            end

            set_entity_location(entity)

            @entity_updates << message
          elsif message.has_entity_list
            message.entity_list.get_entity_list.each do |entity_message|
              entity = entity_from_message(entity_message)
              set_entity_location(entity)
            end
          end

          if message.get_neighbors
            send_neighbors(message)
          else
            unhandled(message)
          end

        elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
          GameMachine.logger.debug "EntityTracking Subscribed"
        elsif message.is_a?(String)
          if message == 'send_entity_updates'
            unless @entity_updates.empty?
              entity_list = EntityList.new.set_entity_list(@entity_updates)
              entity = Entity.new.set_id('0').set_entity_list(entity_list)
              @entity_updates = []
              publish_entity_location_update(entity)
            end
          end
        else
          unhandled(message)
        end
      end

      private

      def entity_from_message(message)
        if message.has_player
          message.player
        elsif message.has_npc
          message.npc
        else
          nil
        end
      end

      def publish_entity_location_update(entity)
        publish = Publish.new.
          set_topic('entity_location_updates').
          set_message(entity)
        MessageQueue.find.tell(publish,self)
      end

      def default_search_radius
        Settings.world_grid_cell_size
      end

      def default_grid
        Physics::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
      end

      def set_entity_location(entity)
        if values = @grid.get(entity.id)
          @grid.remove(entity.id)
        end
        @grid.set(entity)
      end

      def send_neighbors(message)
        neighbors = neighbors_from_grid(
          message.get_neighbors.vector3.x,
          message.get_neighbors.vector3.y,
          message.get_neighbors.search_radius
        )
        
        if neighbors[:players].empty? && neighbors[:npcs].empty?
          return
        end

        if message.has_player
          send_neighbors_to_player(neighbors,message.player)
        else
          send_neighbors_to_sender(neighbors,message)
        end
      end

      def send_neighbors_to_player(neighbors,player)
        response = Helpers::GameMessage.new(player.id)
        response.neighbors(neighbors[:players],neighbors[:npcs])
        response.send_to_player
      end

      def send_neighbors_to_sender(neighbors,message)
        entity = Entity.new.set_neighbors(
          Neighbors.new.
          set_player_list(neighbors[:players]).
          set_npc_list(neighbors[:npcs])
        ).set_id(message.id)
        sender.tell(entity,self)
      end

      def neighbors_from_grid(x,y,search_radius)
        if search_radius.nil?
          search_radius = default_search_radius
        end

        neighbors = {:players => [], :npcs => []}
        @grid.neighbors(x,y,search_radius).each do |neighbor|
          if neighbor.is_a?(Player)
            neighbors[:players] << neighbor
          elsif neighbor.is_a?(Npc)
            neighbors[:npcs] << neighbor
          end
        end
        neighbors
      end

    end
  end
end
