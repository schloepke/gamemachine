require 'integration_helper'
ENV['GAME_ENV'] = 'test'
require 'rubygems'

begin
  require 'game_machine'
rescue LoadError
  require_relative '../lib/game_machine'
end

def echo_test
  GameMachine::MessageLib::EchoTest.new.set_message('testing')
end

def entity
  GameMachine::MessageLib::Entity.new.set_id('1').set_echo_test(echo_test)
end

def message_for(client_message)
  String.from_java_bytes(client_message.to_prefixed_byte_array)
end

def player_for(id)
  GameMachine::MessageLib::Player.new.set_id(id).set_name(id).
    set_authtoken('authorized')
end

def client_message_for(player)
  GameMachine::MessageLib::ClientMessage.new.add_entity(entity).
    set_player(player)
end

module GameMachine

  describe 'tcp client' do 


    describe "sending and receiving messages" do
      it "should receive reply" do
        threads = []
        1.times do |i|
          threads << Thread.new do
            player = player_for("player_#{i}")
            Application.auth_handler.add_user(player.id,player.authtoken)
            message = message_for(client_message_for(player))
            client = Clients::TcpClient.new('localhost',8700)
            results = []
            count = 0
            100000.times do
              #sleep 0.100
              results << Benchmark.realtime do
                client.send_message(message)
                if bytes = client.receive_message.to_java_bytes
                  #client_message = MessageLib::ClientMessage.parse_from(bytes)
                end
              end
              count += 1
              if count > 1000
                puts "Number = #{results.number} Average #{results.mean} Standard deviation #{results.standard_deviation}"
                count = 0
                results = []
              end
            end
            puts "Number = #{results.number} Average #{results.mean} Standard deviation #{results.standard_deviation}"
          end
        end
        threads.map(&:join)
      end
    end
  end
end
