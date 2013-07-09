module GameMachine
  module DataStores
    class Couchbase
      extend Forwardable

      def_delegators :@client, :get, :set, :shutdown

      def connect
        @client ||= JavaLib::CouchbaseClient.new(servers, 'default'.to_java_string, ''.to_java_string)
      end

      def servers
        Settings.couchbase_servers.map {|server| java.net.URI.new(server)}
      end
    end
  end
end
