require 'forwardable'
require_relative 'data_stores/memory'
require_relative 'data_stores/couchbase'
require_relative 'data_stores/mapdb'
require_relative 'data_stores/redis'
require_relative 'data_stores/jdbc'

module GameMachine
  class DataStore
    include Singleton
    extend Forwardable

    def_delegators :@store, :delete, :delete_all, :shutdown, :keys, :dump, :load

    attr_reader :store
    def initialize
      @store_name = Application.config.data_store
      connect
    end

    def set_store(store_name)
      @store = nil
      @store_name = store_name
      connect
    end


    def get(key)
      value = @store.get(key)
      return nil if value.nil?
      if value.is_a?(String)
        MessageLib::Entity.new.set_id(key).set_json_storage(
          MessageLib::JsonStorage.new.set_json(value)
        )
      else
        MessageLib::Entity.parse_from(value)
      end
    end

    def set(key,value)
      if value.has_json_storage
        @store.set(key,value.json_storage.json)
      else
        @store.set(key,value.to_byte_array)
      end
    end

    private

    def connect
      raise "already connected" if @store
      send("connect_#{@store_name}")
    end

    def connect_jdbc
      @store = DataStores::Jdbc.new
      @store.connect
    end

    def connect_couchbase
      @store = DataStores::Couchbase.new
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
