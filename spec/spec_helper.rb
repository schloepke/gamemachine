
ENV['GAME_ENV'] = 'test'
require 'rubygems'

begin
  require 'game_machine'
rescue LoadError
  require_relative '../lib/game_machine'
end

RSpec.configure do |config|
  config.before(:suite) do
    GameMachine::Application.initialize!('default',true)
  end

  config.before(:each) do
    GameMachine::Application.start_actor_system
    GameMachine::Application.start_core_systems
    GameMachine::Application.start_handlers
    GameMachine::Application.start_game_systems
  end

  config.after(:each) do
    GameMachine::Application.stop_actor_system
  end

  config.after(:suite) do
    puts "after suite"
  end
end

begin
  require_relative 'message_expectations'
rescue LoadError
end
