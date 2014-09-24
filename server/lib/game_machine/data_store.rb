require 'forwardable'
require_relative 'data_stores/memory'
require_relative 'data_stores/couchbase'
require_relative 'data_stores/mapdb'
require_relative 'data_stores/redis'
require_relative 'data_stores/jdbc'
require_relative 'data_stores/gamecloud'

module GameMachine
  class DataStore
    include Singleton
    extend Forwardable

    def_delegators :@store, :delete_all, :shutdown, :keys, :dump, :load

    attr_reader :store, :serialization, :classmap
    def initialize
      @store_name = Application.config.datastore.store
      @serialization = Application.config.datastore.serialization
      @classmap = {}
      connect
    end

    def class_cache(classname)
      return MessageLib::Entity if classname.nil?

      if cached = classmap.fetch(classname,nil)
        return cached
      else
        classmap[classname] = "GameMachine::MessageLib::#{classname}".constantize
        classmap[classname]
      end
    end

    def set_store(store_name)
      @store = nil
      @store_name = store_name
      connect
    end


    # for get/set/delete, backend stores return a value or nil, and raise on any underlying error
    # Propogating the error could mess up actor states so we don't do that.  
    # Callers should treat nil/false as a failure.  We log errors so they
    # can be dealt with
    
    def get(key,classname='Entity')
      value = nil
      begin
        value = @store.get(key)
      rescue Exception => e
         GameMachine.logger.error(e.message+"\n"+e.backtrace.join("\n"))
        return nil
      end

      return nil if value.nil?
      klass = class_cache(classname)

      if serialization == 'json'
        klass.parse_from_json(value)
      else
        klass.parse_from(value)
      end
    end

    def set(key,value)
      begin
        if serialization == 'json'
          @store.set(key,value.to_json)
        else
          @store.set(key,value.to_byte_array)
        end
      rescue Exception => e
        GameMachine.logger.error(e.message+"\n"+e.backtrace.join("\n"))
        return false
      end
    end

    def delete(key)
      begin
        @store.delete(key)
      rescue Exception => e
        GameMachine.logger.error(e.message+"\n"+e.backtrace.join("\n"))
        return false
      end
    end

    private

    def connect
      raise "already connected" if @store
      send("connect_#{@store_name}")
    end

    def connect_gamecloud
      @store = DataStores::Gamecloud.new(serialization)
      @store.connect
    end

    def connect_jdbc
      @store = DataStores::Jdbc.new(serialization)
      @store.connect
    end

    def connect_couchbase
      @store = DataStores::Couchbase.new(serialization)
      @store.connect
    end

    def connect_mapdb
      @store = DataStores::Mapdb.new
      @store.connect
    end

    def connect_redis
      @store = DataStores::Redis.new
      @store.connect
    end

    def connect_memory
      @store = DataStores::Memory.new
    end
  end
end
