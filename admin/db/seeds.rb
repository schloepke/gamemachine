# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
Setting.create(
  :environment => 'development',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'memory',
  :cache_write_interval => -1,
  :cache_writes_per_second => -1,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => false,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :seeds => "seed01,seed02"
)
Setting.create(
  :environment => 'production',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'couchbase',
  :cache_write_interval => 10,
  :cache_writes_per_second => 100,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => false,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :seeds => "seed01,seed02"
)
Setting.create(
  :environment => 'test',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'memory',
  :cache_write_interval => -1,
  :cache_writes_per_second => -1,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => false,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :seeds => "seed01,seed02"
)

#development servers
Server.create(
  :environment => 'development',
  :name => 'default',
  :http_enabled => true,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => true,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :akka_host => 'localhost',
  :akka_port => 2551
)
Server.create(
  :environment => 'development',
  :name => 'seed01',
  :http_enabled => true,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => true,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :akka_host => 'localhost',
  :akka_port => 2552
)
Server.create(
  :environment => 'development',
  :name => 'seed02',
  :http_enabled => false,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => false,
  :udp_host => '0.0.0.0',
  :udp_port => 8101,
  :udt_enabled => false,
  :udt_host => 'localhost',
  :udt_port => 8201,
  :akka_host => 'localhost',
  :akka_port => 2553
)
Server.create(
  :environment => 'development',
  :name => 'member01',
  :http_enabled => false,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8102,
  :udt_enabled => false,
  :udt_host => 'localhost',
  :udt_port => 8202,
  :akka_host => 'localhost',
  :akka_port => 0
)

# test servers
Server.create(
  :environment => 'test',
  :name => 'default',
  :http_enabled => true,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => true,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :akka_host => 'localhost',
  :akka_port => 2551
)
