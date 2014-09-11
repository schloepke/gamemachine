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
      MessageLib::Entity.parse_from(value)
    end

    def set(key,value)
      @store.set(key,value.to_byte_array)
    end

    private

    def connect
      raise "already connected" if @store
      send("connect_#{@store_name}")
    end

    def connect_gamecloud
      @store = DataStores::Gamecloud.new
      @store.connect
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
