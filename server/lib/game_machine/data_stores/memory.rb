module GameMachine
  module DataStores
    class Memory

      def initialize
        @data = {}
      end

      def set(key,value)
        @data[key] = value
      end

      def get(key)
        @data.fetch(key,nil)
      end

      def delete(key)
        @data.delete(key)
      end

      def delete_all
        @data = {}
      end

      def shutdown
      end

      private

      def connect
      end

    end
  end
end
