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

      def keys
        @client.keys
      end

      def dump(path,format=:yaml)
        raise NotImplementedError
      end

      def load(path,format=:yaml)
        raise NotImplementedError
      end

      def set(key,value)
        @client.put(key,value)
        @db.commit
      end

      def dbfile
        File.join(GameMachine.app_root,'db','mapdb.db')
      end

      def connect
        unless @client
          @db = DataStores::DBMaker.newFileDB(java.io.File.new(dbfile)).
            closeOnJvmShutdown.
            make
          @client = @db.getTreeMap("entities")
        end
        @client
      end

    end
  end
end

