require 'forwardable'
require_relative 'data_stores/memory'
require_relative 'data_stores/couchbase'
require_relative 'data_stores/mapdb'
require_relative 'data_stores/redis'

module GameMachine
  class DataStore
    include Singleton
    extend Forwardable

    def_delegators :@store, :get, :set, :delete, :delete_all, :shutdown, :keys, :dump, :load

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

    private

    def connect
      raise "already connected" if @store
      send("connect_#{@store_name}")
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
