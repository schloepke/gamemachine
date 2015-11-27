module GameMachine
  module Commands
    class Base

      attr_reader :player, :chat, :datastore, :misc

      def self.commands
        @commands ||= new
      end

      def initialize
        @player = PlayerCommands.new
        @chat = ChatCommands.new
        @datastore = DatastoreCommands.new
        @misc = MiscCommands.new
      end
    end
  end
end
