module GameMachine
  module Commands
    class Base

      attr_reader :player, :grid, :chat, :datastore, :navigation, :misc

      def self.commands
        @commands ||= new
      end

      def initialize
        @player = PlayerCommands.new
        @grid = GridCommands.new
        @chat = ChatCommands.new
        @datastore = DatastoreCommands.new
        @misc = MiscCommands.new
        #@navigation = NavigationCommands.new
      end
    end
  end
end
