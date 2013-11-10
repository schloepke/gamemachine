require 'forwardable'
module GameMachine
  module DataStores
    class Redis
      extend Forwardable

      def connect
        @pool = RedisLib::JedisPool.new(RedisLib::JedisPoolConfig.new, "localhost");
      end

      def delete(key)
        query(:del,key)
      end

      def get(key)
       query(:get,key)
      end

      def set(key,value)
        query(:set,key, value)
      end

      def query(command,key,value=nil)
        jedis = @pool.get_resource
        case command
        when :get
          jedis.get(key)
        when :set
          res = jedis.set(key,value)
          res == 'OK' ? true : false
        when :del
          res = jedis.del(key)
          res == 1 ? true : false
        end
      rescue RedisLib::JedisConnectionException => e
        @pool.return_broken_resource(jedis)
      ensure
        @pool.return_resource(jedis)
      end

      def shutdown
        @pool.destroy
      end
    end
  end
end
