require 'forwardable'

module GameMachine
  module DataStores
    include_package 'org.mapdb'
    class Mapdb
      extend Forwardable

      def_delegators :@client, :get

      def shutdown
        @client.close
      end

      def set(key,value)
        @client.put(key,value)
      end

      def connect
        unless @client
          db = DataStores::DBMaker.newFileDB(java.io.File.new("/tmp/game_machine.db")).
            closeOnJvmShutdown.
            make
          @client = db.getTreeMap("entities")
        end
        @client
      end

    end
  end
end

