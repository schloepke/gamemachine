module GameMachine
  module DataStores
    class Couchbase
      include Singleton

      attr_accessor :client
      def initialize
        hosts ||= [java.net.URI.new("http://127.0.0.1:8091/pools")]
        @client ||= JavaLib::CouchbaseClient.new(hosts, 'default'.to_java_string, ''.to_java_string)
      end

      def shutdown
        @client.shutdown
      end

    end
  end
end
