require 'spec_helper'

module GameMachine

  describe "basic" do 
    let(:entity) do
      entity = Entity.new
      entity.set_id('1')
      player_connection = PlayerConnection.new
      player_connection.set_id('client1')
      player_connection.set_player(Player.new.set_id('1'))
      entity.set_player_connection(player_connection)
      entity
    end

    let(:entity_list) do 
      entity_list = EntityList.new
      entity_list.add_entity(entity)
      entity_list
    end

    let(:client) {Client.new(:app01)}

    context "test" do

      describe "sending messages to remote actors" do
        it "should get there" do
          ref = LocalEcho.find_distributed('blah')
          ref.send_message('blah', :blocking => true)
        end
      end

      describe "sending messages via udp" do
        it "should do" do
          client.send_message(entity_list.to_byte_array)
          client.receive_message
        end
      end

    end
  end
end
