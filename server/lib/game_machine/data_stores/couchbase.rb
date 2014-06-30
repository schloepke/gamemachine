require 'forwardable'
module GameMachine
  module DataStores
    class Couchbase
      extend Forwardable

      def_delegators :@client, :get, :set, :delete, :shutdown

      def connect
        @client ||= JavaLib::CouchbaseClient.new(builder)
      end

      def bucket
       Application.config.couchbase_bucket || 'default'
      end

      def password
       Application.config.couchbase_password || ''
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
        Application.config.couchbase_servers.map {|server| java.net.URI.new(server)}
      end
    end
  end
end
