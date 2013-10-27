module GameMachine
  module Commands
    module MessageHelper
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
