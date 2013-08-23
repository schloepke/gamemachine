module GameMachine
  module GameSystems
    class EntityTracking < Actor::Base

      GRID =  Physics::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
      aspect %w(TrackEntity)
      aspect %w(GetNeighbors)

      attr_reader :grid

      def post_init
        @scheduler = get_context.system.scheduler
        @dispatcher = get_context.system.dispatcher
        @entity_updates = []
        @grid = GRID
        @paths = {}
      end

      def on_receive(message)

        # Ignore our own publishes
        if get_sender == get_self
          return
        end

        if message.is_a?(Entity)
          if message.get_neighbors && !message.published
            send_neighbors(message)
          end

          if message.track_entity
            entity = entity_from_message(message)

            if entity.nil?
              unhandled(message)
              return
            end

            set_entity_location(entity)

            unless message.published
              publish_entity_location_update(message)
            end
          end


        elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
          GameMachine.logger.debug "EntityTracking Subscribed"
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
        entity.set_published(true)
        GameMachine::ClusterMonitor.remote_members.keys.each do |address|
          @paths[address] ||= "#{address}#{self.class.local_path(self.class.name)}"
          Actor::Ref.new(@paths[address],self.class.name).tell(entity)
        end
      end

      def default_search_radius
        Settings.world_grid_cell_size
      end

      def default_grid
        Physics::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
      end

      def set_entity_location(entity)
        @grid.remove(entity.id)
        @grid.set(entity)
      end

      def send_neighbors(message)
        neighbors = neighbors_from_grid(
          message.get_neighbors.vector3.x,
          message.get_neighbors.vector3.y,
          message.get_neighbors.search_radius,
          message.get_neighbors.neighbor_type
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

      def neighbors_from_grid(x,y,search_radius,neighbor_type)
        if search_radius.nil?
          search_radius = default_search_radius
        end

        @grid.neighbors(x,y,search_radius,neighbor_type)
      end

    end
  end
end
