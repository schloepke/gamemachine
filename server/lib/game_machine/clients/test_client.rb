require_relative 'udp_client'
module GameMachine
  module Clients
    class TestClient
      include GameMachine

      def self.nodes
        akka_port = 2551
        udp_port = 24130
        tcp_port = 8910
        www_port = 3001
        nodes = {}
        nodes[:seed] = {:host => host, :akka_port => akka_port, :udp_port => udp_port, :tcp_port => tcp_port, :www_port => www_port}
        10.times do |i|
          name = "node#{i+1}".to_sym
          akka_port += 1
          tcp_port += 1
          udp_port += 1
          www_port += 1
          nodes[name] = {:host => host, :akka_port => akka_port, :udp_port => udp_port, :tcp_port => tcp_port, :www_port => www_port}
        end
        nodes
      end

      def self.host
        ENV['CLUSTER_TEST_HOST'] || '127.0.0.1'
      end

      def self.node_for(name)
        nodes.fetch(name.to_sym)
      end

      attr_reader :client, :username, :authtoken
      def initialize(username,name)
        @username = username
        @authtoken = 'authtoken'
        node = self.class.node_for(name)
        @client = JavaLib::UdpClient.new(node[:udp_port].to_i,3000)
        unless client.connect(node[:host],node[:udp_port].to_i)
          raise "Unable to connect to #{node[:host]} #{node[:udp_port]}"
        end
      end

      def send_message(message)
        client.send(message.to_byte_array)
      end

      def receive_message
        if bytes = client.receive
          client_message = MessageLib::ClientMessage.parse_from(bytes)
          if client_message.get_entity_count >= 1
            entity = client_message.get_entity(0)
            puts "Received #{entity.component_names.to_a.inspect}"
            entity
          else
            puts "Received message without entity"
          end
        else
          nil
        end
      end

      def login
        send_message(connect_message)
        return true if receive_message
        3.times do
          return true if remote_echo
        end
        false
      end

      def login_nowait
        send_message(connect_message)
      end

      def logout
        send_message(logout_message)
      end

      def remote_echo
        entity = new_entity.set_echo_test(MessageLib::EchoTest.new.set_message('test'))
        send_message(entity_message(entity))
        receive_message
      end

      def kill_node
        login
        send_message(kill_message)
      end

      def kill_message
        entity = new_entity.set_poison_pill(MessageLib::PoisonPill.new)
        entity.set_destination("GameMachine/Killswitch")
        message = entity_message(entity)
      end

      def new_entity
        MessageLib::Entity.new.set_id('test')
      end

      def entity_message(entity)
        client_message.add_entity(entity)
      end

      def logout_message
        client_message.set_player_logout(
          MessageLib::PlayerLogout.new.set_authtoken(authtoken).set_player_id(username)
        )
      end

      def connect_message
        client_message.set_player_connect(MessageLib::PlayerConnect.new)
      end

      def client_message
        player = MessageLib::Player.new.set_id(username).set_authtoken(authtoken)
        client_message = MessageLib::ClientMessage.new
        client_message.set_connection_type(0)
        client_message.set_player(player)
      end

      def send_chat_message(type,channel,message)
        entity = new_entity
        entity.set_id("chatmessage")
                
        chat_message = MessageLib::ChatMessage.new
        chat_channel = MessageLib::ChatChannel.new

        chat_channel.set_name(channel)
        chat_message.set_chat_channel(chat_channel)
        chat_message.set_message(message)

        chat_message.set_type(type)

        chat_message.set_sender_id(username)
        entity.set_chat_message(chat_message)
        send_message(entity_message(entity))
      end

    end
  end
end
