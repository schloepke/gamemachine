require 'rubygems'
require 'game_machine'

RSpec.configure do |config|
  config.before(:suite) do
    GameMachine::Server.instance.init!
    GameMachine::Server.instance.start_actor_system
    GameMachine::Server.instance.start_game_systems
    puts "before suite"
  end

  config.before(:each) do
  end

  config.after(:each) do
  end

  config.after(:suite) do
    puts "after suite"
    GameMachine::Server.instance.stop_actor_system
  end
end

