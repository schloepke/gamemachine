module GameMachine
  module RestApi
    class ProtobufCompiler < Actor::Base

      def post_init(*args)
        @combined_messages_file = File.join(
          GameMachine.app_root,'config','combined_messages.proto')
        @game_messages_file = File.join(
          GameMachine.app_root,'config','game_messages.proto')
      end

      def on_receive(message)
        uri = message.fetch(:uri)
        if uri == '/protobuf/combined_messages'
          response = read_file(@combined_messages_file)
        elsif uri == '/protobuf/game_messages'
          response = read_file(@game_messages_file)
        elsif uri == '/protobuf/update_game_messages'
          generate_combined
          response = read_file(@combined_messages_file)
        else
          response = 'Invalid request'
        end
        getSender.tell(response,get_self)
      end

      def write_file(filename,content)
        File.open(filename,'wb') {|f| f.write(content)} 
      end

      def read_file(filename)
        if File.exists?(filename)
          File.read(filename)
        else
          nil
        end
      end

      def generate_combined
        GameMachine::Protobuf::Generate.new(
          GameMachine.app_root
        ).generate
      end
    end
  end
end
