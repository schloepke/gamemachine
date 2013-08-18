require 'integration_helper'

  def get_neighbors_message(player_id)
    m = GameMachine::Helpers::GameMessage.new(player_id.to_s)
    m.track_entity
    m.get_neighbors
    m.to_entity
    m.current_entity.player.set_x(rand(1000)).set_y(1000)
    m
  end

  def player_logout(player_id)
    m = GameMachine::Helpers::GameMessage.new(player_id.to_s)
    m.player_logout
    m
  end

module GameMachine
  COUNT = JavaLib::AtomicInteger.new

  describe 'Player tracking' do

    it "players are tracked" do
      pre = Proc.new do
        player_id = COUNT.increment_and_get
        Thread.current['player_id'] = player_id
        Thread.current['bytes'] = get_neighbors_message(player_id).to_byte_array
        Thread.current['c'] = Clients::UdtClient.new(:seed01)
        Thread.current['c'].connect
      end

      post = Proc.new do
        player_id = Thread.current['player_id']
        Thread.current['c'].send_message(player_logout(player_id).to_byte_array)
        sleep 0.100
        Thread.current['c'].disconnect
      end

      measure(100,1000,pre,post) do
        Thread.current['c'].send_message(Thread.current['bytes'])
        res = Thread.current['c'].receive
        client_message = ClientMessage.parse_from(res)
      end
    end
  end
end


