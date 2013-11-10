require 'forwardable'
module GameMachine
  module DataStores
    class Couchbase
      extend Forwardable

      def_delegators :@client, :get, :set, :delete, :shutdown

      def connect
        @client ||= JavaLib::CouchbaseClient.new(servers, 'default'.to_java_string, ''.to_java_string)
      end

      def servers
        Application.config.couchbase_servers.map {|server| java.net.URI.new(server)}
      end
    end
  end
end
