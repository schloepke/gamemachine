module GameMachine
  module Helpers
    class GameMessage
      CHARS = [*('a'..'z'),*('0'..'9')].flatten
      STR = Array.new(100) {|i| CHARS.sample}.join
      STR2 = Array.new(1000) {|i| CHARS.sample}.join

      attr_reader :player_id, :client_message

      def initialize(player_id,client_id=nil,gateway=nil)
        @current_entity_id = 0
        @entities = {}
        @current_entity_name = :default
        @client_message = create_client_message(player_id,client_id,gateway)
        set_entity(@current_entity_name)
      end

      def create_client_message(player_id,client_id,gateway)
        client_message = ClientMessage.new
        client_message.set_player(player(player_id))
        if client_id && gateway
          client_message.set_client_connection(
            ClientConnection.new.set_id(client_id).set_gateway(gateway)
          )
        end
        client_message
      end

      def current_entity
        @entities.fetch(@current_entity_name)
      end

      def set_entity(name)
        unless @entities.fetch(name,nil)
          @entities[name] = Entity.new.set_id(next_entity_id)
          client_message.add_entity(@entities.fetch(name))
        end
      end

      def use_entity(name)
        set_entity(name)
        @current_entity_name = name
      end

      def to_entity
        current_entity.set_client_connection(client_message.client_connection)
        current_entity.set_player(client_message.player)
      end

      def client_connection(client_connection)
        client_message.set_client_connection(client_connection)
      end

      def chat_channels(names)
        channels = ChatChannels.new
        names.each do |name|
          channels.add_chat_channel(chat_channel(name))
        end
        current_entity.set_chat_channels(channels)
      end

      def chat_channel(topic)
        ChatChannel.new.set_name(topic)
      end

      def chat_message(type,message_text,topic)
        message = ChatMessage.new
        message.set_chat_channel(
          chat_channel(topic)
        )
        message.set_message(message_text)
        message.set_type(type)
        current_entity.set_chat_message(message)
      end

      def join_chat(topic)
        current_entity.set_join_chat(
          JoinChat.new.add_chat_channel(chat_channel(topic))
        )
      end

      def leave_chat(topic)
        current_entity.set_leave_chat(
          LeaveChat.new.add_chat_channel(chat_channel(topic))
        )
      end

      def echo_test(value)
        current_entity.set_echo_test(EchoTest.new.set_message(value))
      end

      private

      def player(player_id)
        player = Player.new.
          set_id(player_id).
          set_name(player_id).
          set_authtoken('authorized')
      end

      def next_entity_id
        (@current_entity_id += 1).to_s
      end

    end
  end
end
