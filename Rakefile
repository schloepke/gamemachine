require "bundler/gem_tasks"
require 'rspec/core/rake_task'
require 'rspec/core/rake_task'
require 'game_machine'

RSpec::Core::RakeTask.new

task :default => :spec

task :run do
  GameMachine::Server.start
end
