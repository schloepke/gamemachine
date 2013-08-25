module GameMachine
  module GameSystems
    class EntityTracking < Actor::Base

      DEFAULT_SEARCH_RADIUS = Settings.world_grid_cell_size
      GRID =  JavaLib::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
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
            set_entity_location(message)

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

      def publish_entity_location_update(entity)
        entity.set_published(true)
        GameMachine::ClusterMonitor.remote_members.keys.each do |address|
          @paths[address] ||= "#{address}#{self.class.local_path(self.class.name)}"
          Actor::Ref.new(@paths[address],self.class.name).tell(entity)
        end
      end

      def set_entity_location(entity)
        @grid.set(entity,entity.entity_type)
      end

      def send_neighbors(message)
        type = message.get_neighbors.neighbor_type
        search_results = self.class.neighbors_from_grid(
          message.get_neighbors.vector3.x,
          message.get_neighbors.vector3.y,
          message.get_neighbors.search_radius,
          type,
          message.id
        )
       
        neighbors = {:players => [], :npcs => []}
        search_results.each do |grid_value|
          if grid_value.entityType == 'player'
            neighbors[:players] << grid_value.entity
          elsif grid_value.entityType == 'npc'
            neighbors[:npcs] << grid_value.entity
          end
        end
 
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

      def self.neighbors_from_grid(x,y,search_radius,neighbor_type)
        if search_radius.nil?
          search_radius = DEFAULT_SEARCH_RADIUS
        end

        GRID.neighbors(x,y,search_radius,neighbor_type)
      end

    end
  end
end
