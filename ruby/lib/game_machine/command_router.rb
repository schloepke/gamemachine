module GameMachine
  class CommandRouter < ActorBase


    def self.systems
      [ConnectionManager, LocalEcho]
    end


    def on_receive(gateway_message)

      entities = Components.parse_from(gateway_message.get_bytes).to_entities
      client_id = gateway_message.get_client_id

      ConnectionManager.tell({:client_id => client_id, :authorized => true},get_self)

      #LocalEcho.ask(gateway_message) do |m|
      #  #puts "M=#{m.inspect}"
      #end


      Gateway.send(client_id, gateway_message.get_bytes)
      entities.get_entities.values.each do |entity|
        entity.component_names.each do |component_name|
          
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
