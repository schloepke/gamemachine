module GameMachine
  module Commands
    class PlayerCommands
      include MessageHelper

      def send_game_message(game_message,player_id)
        entity = MessageLib::Entity.new.set_id('0')
        game_messages = MessageLib::GameMessages.new
        game_messages.add_game_message(game_message)
        entity.set_game_messages(game_messages)
        send_message(entity,player_id)
      end

      def send_message(message,player_id)
        if player_id.nil?
          GameMachine.logger.error "PlayerCommands.send_message: player id cannot be nil"
          return
        end
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
            GameMachine.logger.error "PlayerCommands.send_message: #{message} is not a valid object to send to a player"
            return
          end
        end
        entity.set_send_to_player(true)
        ClientManager.send_to_player(entity)
      end
    end
  end
end
