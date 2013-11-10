class CreateAppServers < ActiveRecord::Migration
  def change

    create_table :app_servers do |t|
      t.string :name
      t.integer :user_id
      t.integer :cluster_id
      t.string    :environment
      t.string    :game_handler
      t.string    :data_store
      t.integer   :cache_write_interval, :default => -1
      t.integer   :cache_writes_per_second, :default => -1
      t.integer   :world_grid_size, :default => 2000
      t.integer   :world_grid_cell_size, :default => 50
      t.boolean   :mono_enabled, :default => false
      t.integer   :singleton_manager_router_count, :default => 10
      t.integer   :singleton_manager_update_interval, :default => 100
      t.string    :couchbase_servers
      t.string    :auth_handler
      t.boolean   :http_enabled, :default => false
      t.string    :http_host
      t.integer   :http_port
      t.boolean   :udp_enabled, :default => false
      t.string    :udp_host
      t.integer   :udp_port
      t.boolean   :udt_enabled, :default => false
      t.string    :udt_host
      t.integer   :udt_port
      t.boolean   :tcp_enabled, :default => false
      t.string    :tcp_host
      t.integer   :tcp_port
      t.string    :akka_host
      t.integer   :akka_port
      t.timestamps
    end
    add_index :app_servers, [:name, :environment], :unique => true
  end
end
