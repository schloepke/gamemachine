module GameMachine
  class CommandRouter <GameSystem 

    def on_receive(gateway_message)
      GameMachine.logger.info "CommandRouter got #{gateway_message}"

      entities = Components.parse_from(gateway_message.get_bytes).to_entities
      client_id = gateway_message.get_client_id

      entities.get_entities.values.each do |entity|
        GameSystem.systems.each do |klass|
          next if klass.components.empty?
          if (klass.components & entity.component_names).size == klass.components.size
            game_message = GameMessage.new(client_id,entity)
            klass.tell(game_message)
          end
        end
      end

    end

  end

end


#Query.put(entity.get_id,entity)

#Query.get(entity.get_id) do |get_entity|
#  #puts "GET ENTITY = #{get_entity}"
#end

#puts "OUTSIDE #{Thread.current}"
#Query.update(entity.get_id) do |update_entity|
#  game_command = GameCommand.new
#  game_command.set_name("NotEcho")
#  update_entity.set_game_command(game_command)
#  #puts "INSIDE #{Thread.current}"
#  #puts "IN BLOCK #{update_entity.get_id} #{client_id}"
#end

#puts entity.get_game_command.get_name
