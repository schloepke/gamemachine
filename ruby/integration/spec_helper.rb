ENV['GAME_ENV'] = 'test'
require 'rubygems'
require 'game_machine'

RSpec.configure do |config|
  config.before(:suite) do
    GameMachine::Server.instance.init!
    puts "before suite"
  end

  config.before(:each) do
    GameMachine::Server.instance.start_actor_system
    GameMachine::Server.instance.start_game_systems
  end

  config.after(:each) do
    GameMachine::Server.instance.stop_actor_system

  end

  config.after(:suite) do
    puts "after suite"
  end
end

