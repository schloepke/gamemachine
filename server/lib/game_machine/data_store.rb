
require 'forwardable'

module GameMachine
  class DataStore
    include Singleton
    extend Forwardable

    def_delegators :@store, :get, :set, :shutdown

    def initialize
      @store_name = Settings.data_store
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

    def connect_memory
      @store = DataStores::Memory.new
    end
  end
end
