module GameMachine
  module Commands
    class Base

      attr_reader :player, :grid
      def initialize
        @player = PlayerCommands.new
        @grid = GridCommands.new
      end
    end
  end
end
