require 'fileutils'

module Web
  module Controllers
    class MessagesController < BaseController

      def initialize
        @combined_messages_file = File.join(
          GameMachine.app_root,'config','combined_messages.proto')
        @game_messages_file = File.join(
          GameMachine.app_root,'config','game_messages.proto')
      end

      def all
        read_file(@combined_messages_file)
      end

      def game
        read_file(@game_messages_file)
      end

      def update
        FileUtils.cp(@game_messages_file,"#{@game_messages_file}.bak")
        content = params['game_messages']
        write_file(@game_messages_file,content)
        GameMachine::Console::Build.new([]).generate_code
        game_messages = read_file(@game_messages_file)
      end

      private

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
