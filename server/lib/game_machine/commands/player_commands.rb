module GameMachine
  module Commands
    class PlayerCommands
      include MessageHelper

      def send_message(message,player_id)
        if is_entity?(message)
          entity = message
          unless entity.has_player
            set_player(entity,player_id)
          end
        elsif message.kind_of?(GameMachine::Model)
          entity = entity_with_player(player_id,player_id)
          entity.set_json_entity(message.to_json_entity)
        else
          entity = entity_with_player(player_id,player_id)
          if is_component?(message)
            entity.add_component(message)
          else
            raise "#{message} is not a valid object to send to a player"
          end
        end
        entity.set_send_to_player(true)
        ClientManager.send_to_player(entity)
      end
    end
  end
end
