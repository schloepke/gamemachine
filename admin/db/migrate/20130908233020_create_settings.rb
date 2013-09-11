class CreateSettings < ActiveRecord::Migration
  def change
    create_table :settings do |t|
      t.boolean   :mono_enabled, :default => false
      t.string    :environment
      t.string    :game_handler
      t.string    :data_store
      t.string    :couchbase_servers
      t.integer   :cache_write_interval
      t.integer   :cache_writes_per_second
      t.integer   :world_grid_size
      t.integer   :world_grid_cell_size
      t.integer   :singleton_manager_router_count
      t.integer   :singleton_manager_update_interval
      t.string    :seeds
    end
  end
end
