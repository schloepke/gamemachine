require 'forwardable'
module GameMachine
  module Commands
    class All
      extend Forwardable

      def send_to_player(message,player_id)
        if is_entity?(message)
          entity = message
        else
          entity = entity_with_player(player_id,player_id)
          if is_component?(message)
            entity.add_component(message)
          else
            raise "#{message} is not a valid object to send to a player"
          end
        end
        entity.set_send_to_player(true)
        PlayerGateway.find.tell(entity)
      end

      def grid_find_by_id(id)
        GameMachine::GameSystems::EntityTracking::GRID.get(id)
      end

      def grid_neighbors(x,z,type='player')
        GameMachine::GameSystems::EntityTracking.neighbors_from_grid(x,z,type)
      end

      def grid_track(id,x,z,y,entity_type='npc')
        GameMachine::GameSystems::EntityTracking::GRID.set(id,x,z,y,entity_type)
      end
      

      private

      def entity(id)
        Entity.new.set_id(id)
      end

      def entity_with_player(id,player_id)
        entity(id).set_player(Player.new.set_id(player_id))
      end

      def is_component?(obj)
        obj.respond_to?(:message_name)
      end

      def is_entity?(obj)
        obj.is_a?(Entity)
      end
    end
  end
end
