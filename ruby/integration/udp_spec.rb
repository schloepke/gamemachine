require 'integration_helper'

module GameMachine
  CHARS = [*('a'..'z'),*('0'..'9')].flatten
  STR = Array.new(100) {|i| CHARS.sample}.join

  describe "echo" do 

    let(:player) do
      player = Player.new
      player.set_id('2')
      player.set_authtoken('authorized')
    end

    let(:entity) do
      entity = Entity.new
      entity.set_id(STR.to_java_string)
      entity.set_player(player)
      echo_test = EchoTest.new.set_message('testing')
      entity.set_echo_test(echo_test)
      entity
    end

    let(:entity_list) do 
      entity_list = EntityList.new
      entity_list.add_entity(entity)
      entity_list.set_player(player)
      entity_list
    end

    context "test" do

      describe "sending messages via udp" do
        it "should do" do
          clients = java.util.concurrent.ConcurrentHashMap.new
          bytes = entity_list.to_byte_array
          puts 'starting udp client test'
          measure(10,10000) do
            Thread.current['c'] ||= Client.new(:seed01)
            Thread.current['c'].send_message(bytes)
            message = Thread.current['c'].receive_message
            Entity.parse_from(message.to_java_bytes)
          end
          puts 'udp client test done'
        end
      end

    end
  end
end

