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

GameUser.create(:user => 'test_user', :pass => 'test_pass')

def add_proto_message(name)
  message = ProtobufMessage.new(:name => name)
  message.save ? message : false
end

def add_proto_field(message,name,value_type,field_type)
  message.protobuf_fields << ProtobufField.new(
    :name => name, :value_type => value_type, :field_type => field_type
  )
end

if message = add_proto_message('Health')
  add_proto_field(message,'health','int32','required')
end

if message = add_proto_message('Effect')
  add_proto_field(message,'length','int32','optional')
  add_proto_field(message,'name','string','optional')
  add_proto_field(message,'healthDiff','int32','optional')
  add_proto_field(message,'damageDiff','int32','optional')
  add_proto_field(message,'timePeriod','int32','optional')
  add_proto_field(message,'type','string','optional')
end

if message = add_proto_message('EffectList')
  add_proto_field(message,'effect','Effect','repeated')
end

if message = add_proto_message('CombatAbility')
  add_proto_field(message,'name','string','required')
  add_proto_field(message,'damage','int32','required')
  add_proto_field(message,'hitChance','int32','optional')
  add_proto_field(message,'range','int32','required')
  add_proto_field(message,'type','string','optional')
end

if message = add_proto_message('Attack')
  add_proto_field(message,'attacker','string','required')
  add_proto_field(message,'target','string','required')
  add_proto_field(message,'combatAbilityId','int32','optional')
end
