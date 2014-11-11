require 'spec_helper_minimal'

require_relative '../../lib/game_machine/clients/test_client'
require_relative '../../lib/game_machine/console'

module GameMachine

  describe "connections" do

    xit "entity tracking" do
    end

    xit "logout all" do
      clients = {}
      count = 100

      count.times do |i|
        name = "user#{i}"
        clients[i] = Clients::TestClient.new(name,'seed')
      end
      count.times do |i|
        clients[i].logout
        sleep 0.002
      end
    end

    it "login all" do
      clients = {}
      count = 1000

      count.times do |i|
        name = "user#{i}"
        clients[i] = Clients::TestClient.new(name,'seed')
      end
      count.times do |i|
        clients[i].login_nowait
        sleep 0.001
      end
      puts "clients connected"

      1.times do |x|
        puts x
        c = 0
        count.times do |i|
          clients[i].send_remote_echo
          c+= 1
          if c > 10
            sleep 0.001
            c = 0
          end
        end
      end

    end

    xit "multiple client connections" do
      clients = {}
      count = 100

      count.times do |i|
        name = "user#{i}"
        clients[i] = Clients::TestClient.new(name,'seed')
      end

      count.times do |i|
        clients[i].login_nowait
      end

      count.times do |i|
        clients[i].logout
      end

    end

    xit "private chat messages between nodes" do
      player1 = Clients::TestClient.new('player1','seed')
      player1.login

      player2 = Clients::TestClient.new('player2','node1')
      player2.login

      player3 = Clients::TestClient.new('player3','node2')
      player3.login

      100.times do
        player1.send_chat_message('private','player2','hiya player2')
        player2.send_chat_message('private','player1','hiya player1')
        player3.send_chat_message('private','player1','hiya player1')

        if entity = player2.receive_message
          expect(entity.chat_message.message).to eql('hiya player2')
        end

        if entity = player1.receive_message
          expect(entity.chat_message.message).to eql('hiya player1')
        end

        if entity = player1.receive_message
          expect(entity.chat_message.message).to eql('hiya player1')
        end
      end

      player1.logout
      player2.logout
      player3.logout
    end
  end
end