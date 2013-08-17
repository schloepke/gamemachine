require 'rspec/core/rake_task'

specfile = File.join(File.dirname(__FILE__), 'game_machine.gemspec')
if File.exists?(specfile)
  require "bundler/gem_tasks"
end

RSpec::Core::RakeTask.new

task :default => :spec

namespace :java do

  task :clean do
    sh 'rm java/lib/*.jar'
  end

  task :build do
    sh 'cd java && gradle codegen && gradle build && gradle install_libs'
  end
end

