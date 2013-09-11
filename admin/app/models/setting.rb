class Setting < ActiveRecord::Base

  validates_presence_of :environment
  validates_uniqueness_of :environment
  validates_presence_of :seeds
  validates_presence_of :game_handler
  validates_presence_of :data_store
  validates_presence_of :cache_write_interval
  validates_presence_of :cache_writes_per_second
  validates_presence_of :world_grid_size
  validates_presence_of :world_grid_cell_size
  validates_presence_of :singleton_manager_router_count
  validates_presence_of :singleton_manager_update_interval

  rails_admin do
      navigation_label 'Configuration'
      weight 5
  end

  def environment_enum
    ['development','production','test']
  end

  def data_store_enum
    ['couchbase','mapdb','memory']
  end
end
