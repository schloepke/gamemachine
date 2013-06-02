module GameMachine
  class CommandRouter < ActorBase

    def on_receive(gateway_message)
      puts "CommandRouter got #{gateway_message}"

      entities = Components.parse_from(gateway_message.get_bytes).to_entities
      client_id = gateway_message.get_client_id

      dispatch_map = {}
      entities.get_entities.values.each do |entity|
        entity.component_names.each do |component_name|
          dispatch_map[entity] ||= []
          dispatch_map[entity] += Systems.systems_with_component(component_name)
        end
      end
      dispatch_map.each do |entity,systems|
        systems.sort.each {|system| system.tell(entity)}
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
