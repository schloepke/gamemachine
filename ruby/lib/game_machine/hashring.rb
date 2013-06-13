module GameMachine
  class Hashring

    attr_reader :buckets, :servers
    def initialize(servers,bucket_name,bucket_count)
      @servers = servers
      @bucket_name = bucket_name
      @bucket_count = bucket_count
      @buckets = {}
      @buckets_for_server = nil
    end

    def hash!
      i = 0
      @servers.cycle do |server|
        if i < @bucket_count
          @buckets["#{@bucket_name}#{i}"] = server
          i+= 1
        else
          break
        end
      end
    end

    def buckets_for_server(server)
      @buckets_for_server ||= buckets.keys.select {|bucket_name| @buckets.fetch(bucket_name) == server}
    end

    def remove_server(server)
      @servers.delete_if {|s| s == server}
      @buckets.keys.each do |bucket_name|
        if @buckets[bucket_name] == server
          @buckets[bucket_name] = @servers[rand(@servers.size)]
        end
      end
      @buckets_for_server = nil
    end

    def bucket_for_string(value)
      "#{@bucket_name}#{string_to_bucket(value)}"
    end

    private

    def string_to_bucket(value)
      Hashing.consistentHash(Hashing.md5.hashString(value), @bucket_count)
    end
  end
end
