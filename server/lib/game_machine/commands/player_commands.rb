module GameMachine
  module Commands
    class PlayerCommands
      include MessageHelper

      def send_message(message,player_id)
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
    end
  end
end
