require 'forwardable'
module GameMachine
  module DataStores
    class Couchbase
      extend Forwardable

      def_delegators :@client, :get, :set, :delete, :shutdown

      attr_reader :serialization
      def initialize(serialization)
        @serialization = serialization
      end
      
      def connect
        @client ||= JavaLib::CouchbaseClient.new(builder)
      end

      def bucket
       Application.config.couchbase.bucket || 'default'
      end

      def password
       Application.config.couchbase.password || ''
      end

      def builder
        @builder ||= JavaLib::CouchbaseConnectionFactoryBuilder.new.
          buildCouchbaseConnection(
            servers,
            bucket.to_java_string,
            password.to_java_string
        )
      end

      def servers
        Application.config.couchbase.servers.map {|server| java.net.URI.new(server)}
      end
    end
  end
end
