module GameMachine
  module Commands
    class Base

      attr_reader :player, :grid, :chat, :datastore
      def initialize
        @player = PlayerCommands.new
        @grid = GridCommands.new
        @chat = ChatCommands.new
        @datastore = DatastoreCommands.new
      end
    end
  end
end
