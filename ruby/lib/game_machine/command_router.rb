module GameMachine
  class CommandRouter < ActorBase


    def self.systems
      [ConnectionManager, LocalEcho]
    end


    def on_receive(gateway_message)
      puts "CommandRouter got #{gateway_message}"
      
      entities = Components.parse_from(gateway_message.get_bytes).to_entities
      client_id = gateway_message.get_client_id

      entities.get_entities.values.each do |entity|
        entity.component_names.each do |component_name|
          self.class.systems.each do |system|
            if system.components.include?(component_name)
              system.tell(entity)
            end
          end
        end
      end
      Gateway.send_to_client(client_id, gateway_message.get_bytes)
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
