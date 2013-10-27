module GameMachine
  module Helpers
    class GameMessage
      CHARS = [*('a'..'z'),*('0'..'9')].flatten
      STR = Array.new(100) {|i| CHARS.sample}.join
      STR2 = Array.new(1000) {|i| CHARS.sample}.join

      attr_reader :player_id, :client_message

      def initialize(player_id,default_entity_id=nil)
        @player_id = player_id
        @current_entity_id = default_entity_id || 'default'
        @client_message = create_client_message(@player_id)
      end

      def to_byte_array
        client_message.to_byte_array
      end

      def send_to_player
        client_message.send_to_player
      end

      def entities
        client_message.get_entity_list.to_a
      end

      def has_entity?(id)
        entities.select {|entity| entity.id == id}.first ? true : false
      end

      def current_entity
        unless has_entity?(@current_entity_id)
          add_entity(@current_entity_id)
        end
        entities.select {|entity| entity.id == @current_entity_id}.first
      end

      def entity(id)
        @current_entity_id = id
        current_entity
      end

      def to_entity
        current_entity.set_player(client_message.player)
      end

      def error_message(code,message)
        client_message.set_error_message(
          MessageLib::ErrorMessage.new.set_code(code).set_message(message)
        )
      end

      def client_disconnect(client_id,gateway)
        client_message.set_client_disconnect(
          MessageLib::ClientDisconnect.new.set_client_connection(
            client_connection(client_id,gateway)
          )
        )
      end

      def player_logout
        client_message.set_player_logout(
          MessageLib::PlayerLogout.new.set_player_id(@player_id)
        )
      end

      def track_entity
        current_entity.set_track_entity(
          MessageLib::TrackEntity.new.set_value(true)
        )
      end

      def get_neighbors(search_radius=nil)
          component = MessageLib::GetNeighbors.new
          component.set_value(true)
          if search_radius
            component.set_search_radius(search_radius)
          end
        current_entity.set_get_neighbors(component)
      end

      def neighbors(players,npcs)
        current_entity.set_neighbors(
          MessageLib::Neighbors.new.set_player_list(players).set_npc_list(npcs)
        )
      end

      def player_move(x,y)
        current_entity.set_player_move(
          MessageLib::PlayerMove.new.set_target(
            MessageLib::Target.new.set_x(x).set_y(y)
          )
        )
      end

      def client_connection(client_id,gateway)
        MessageLib::ClientConnection.new.set_id(client_id).set_gateway(gateway)
      end

      def chat_channels(names)
        channels = MessageLib::ChatChannels.new
        names.each do |name|
          channels.add_chat_channel(chat_channel(name))
        end
        current_entity.set_chat_channels(channels)
      end

      def chat_channel(topic)
        MessageLib::ChatChannel.new.set_name(topic)
      end

      def chat_message(type,message_text,topic)
        message = MessageLib::ChatMessage.new
        message.set_chat_channel(
          chat_channel(topic)
        )
        message.set_message(message_text)
        message.set_type(type)
        current_entity.set_chat_message(message)
      end

      def join_chat(topic)
        current_entity.set_join_chat(
          MessageLib::JoinChat.new.add_chat_channel(chat_channel(topic))
        )
      end

      def leave_chat(topic)
        current_entity.set_leave_chat(
          MessageLib::LeaveChat.new.add_chat_channel(chat_channel(topic))
        )
      end

      def echo_test(value)
        current_entity.set_echo_test(MessageLib::EchoTest.new.set_message(value))
      end

      private

      def create_client_message(player_id)
        client_message = MessageLib::ClientMessage.new
        client_message.set_player(player(player_id))
      end

      def add_entity(id)
        client_message.add_entity(MessageLib::Entity.new.set_id(id))
      end

      def player(player_id)
        player = MessageLib::Player.new.
          set_id(player_id).
          set_name(player_id).
          set_authtoken('authorized')
      end

    end
  end
end
