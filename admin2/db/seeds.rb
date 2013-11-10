User.destroy_all
Cluster.destroy_all
AppServer.destroy_all
user = User.new({
  email: 'template@gamemachine.io',
  password: 'password',
  password_confirmation: 'password',
  is_active: false
})
user.save!

cluster = Cluster.create(:name => 'development')
user.clusters << cluster
app_server = AppServer.create(
  :name => 'default',
  :environment => 'development',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'memory',
  :cache_write_interval => -1,
  :cache_writes_per_second => -1,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => true,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :auth_handler => 'GameMachine::AuthHandlers::Basic',
  :http_enabled => true,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => true,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :tcp_host => '0.0.0.0',
  :tcp_port => 8700,
  :tcp_enabled => true,
  :akka_host => 'localhost',
  :akka_port => 2551
)
cluster.app_servers << app_server
user.app_servers << app_server

app_server = AppServer.create(
  :name => 'seed01',
  :environment => 'development',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'memory',
  :cache_write_interval => -1,
  :cache_writes_per_second => -1,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => true,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :auth_handler => 'GameMachine::AuthHandlers::Basic',
  :http_enabled => true,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => true,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :tcp_host => '0.0.0.0',
  :tcp_port => 8700,
  :tcp_enabled => true,
  :akka_host => 'localhost',
  :akka_port => 2552
)
cluster.app_servers << app_server
user.app_servers << app_server

app_server = AppServer.create(
  :name => 'seed02',
  :environment => 'development',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'memory',
  :cache_write_interval => -1,
  :cache_writes_per_second => -1,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => true,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :auth_handler => 'GameMachine::AuthHandlers::Basic',
  :http_enabled => false,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => false,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => false,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :tcp_host => '0.0.0.0',
  :tcp_port => 8700,
  :tcp_enabled => false,
  :akka_host => 'localhost',
  :akka_port => 2553
)
cluster.app_servers << app_server
user.app_servers << app_server

cluster = Cluster.create(:name => 'development')
user.clusters << cluster
app_server = AppServer.create(
  :name => 'default',
  :environment => 'test',
  :game_handler => 'GameMachine::Handlers::Request',
  :data_store => 'memory',
  :cache_write_interval => -1,
  :cache_writes_per_second => -1,
  :world_grid_size => 2000,
  :world_grid_cell_size => 25,
  :singleton_manager_router_count => 200,
  :singleton_manager_update_interval => 100,
  :mono_enabled => true,
  :couchbase_servers => 'http://217.0.0.1:8091/pools',
  :auth_handler => 'GameMachine::AuthHandlers::Basic',
  :http_enabled => true,
  :http_host => 'localhost',
  :http_port => 8080,
  :udp_enabled => true,
  :udp_host => '0.0.0.0',
  :udp_port => 8100,
  :udt_enabled => true,
  :udt_host => 'localhost',
  :udt_port => 8200,
  :tcp_host => '0.0.0.0',
  :tcp_port => 8700,
  :tcp_enabled => true,
  :akka_host => 'localhost',
  :akka_port => 2551
)
cluster.app_servers << app_server
user.app_servers << app_server

