require 'integration_helper'

TMUTEX = Mutex.new

module GameMachine

  describe 'Player tracking' do

    let(:message) do
      ::TMUTEX.synchronize do
        @@count ||= 1
        m = Helpers::GameMessage.new(@@count.to_s)
        @@count += 1
        m.track_player
        m.get_neighbors
        m.to_entity
        m.current_entity.player.set_x(rand(1000)).set_y(1000)
        m
      end
    end

    it "players are tracked" do
      clients = java.util.concurrent.ConcurrentHashMap.new
      measure(10,10000) do
        unless Thread.current['bytes']
        m = message
        puts message.current_entity.player.id
        Thread.current['bytes'] = m.to_byte_array
        end
        Thread.current['bytes'] ||= message.to_byte_array
        Thread.current['c'] ||= Clients::Client.new(:seed01)
        Thread.current['c'].send_message(Thread.current['bytes'])
        message = Thread.current['c'].receive_message
        ClientMessage.parse_from(message.to_java_bytes)
      end
    end
  end
end


