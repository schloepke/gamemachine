module GameMachine
  class Hashring

    RING_SIZE = 160
    REPLICAS = 160
    attr_reader :buckets
    def initialize(servers,bucket_name)
      @buckets = (0..RING_SIZE).map {|r| "#{bucket_name}#{r}"}
      @server_ring = ConsistentHashing::Ring.new(servers,REPLICAS)
      @bucket_ring = ConsistentHashing::Ring.new(buckets,REPLICAS)
    end

    def remove_server(server)
      @server_ring.delete(server)
    end

    def server_for(value)
      @server_ring.node_for(value)
    end

    def bucket_for(value)
      @bucket_ring.node_for(value)
    end

  end
end
