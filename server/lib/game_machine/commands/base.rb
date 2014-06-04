module GameMachine
  module Commands
    class Base

      attr_reader :player, :grid, :chat, :datastore, :navigation
      def initialize
        @player = PlayerCommands.new
        @grid = GridCommands.new
        @chat = ChatCommands.new
        @datastore = DatastoreCommands.new
        #@navigation = NavigationCommands.new
      end
    end
  end
end
