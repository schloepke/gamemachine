module GameMachine
  module GameSystems
    class EntityTracking < Actor::Base

      GRID =  JavaLib::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
      aspect %w(TrackEntity)
      aspect %w(GetNeighbors)

      attr_reader :grid


      def post_init
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
          end
        else
          unhandled(message)
        end
      end

      private

      def set_entity_location(entity)
        vector = entity.vector3
        #puts "#{entity.id},#{vector.x},#{vector.y},#{vector.z},#{entity.entity_type}"
        @grid.set(entity.id,vector.x,vector.y,vector.z,entity.entity_type)
      end

      def location_entity(grid_value)
        Entity.new.set_id(grid_value.id).set_vector3(
          Vector3.new.set_xi(grid_value.x.to_i).set_yi(grid_value.y.to_i)
        )
      end

      def send_neighbors(message)
        type = message.get_neighbors.neighbor_type
        search_results = self.class.neighbors_from_grid(
          message.get_neighbors.vector3.x,
          message.get_neighbors.vector3.y,
          type
        )
       
        neighbors = {:players => [], :npcs => []}
        search_results.each do |grid_value|
          if grid_value.entityType == 'player'
            neighbors[:players] << location_entity(grid_value)
          elsif grid_value.entityType == 'npc'
            neighbors[:npcs] << location_entity(grid_value)
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

      def self.neighbors_from_grid(x,y,neighbor_type)
        GRID.neighbors(x,y,neighbor_type)
      end

    end
  end
end
