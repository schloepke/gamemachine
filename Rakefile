require 'rspec/core/rake_task'

specfile = File.join(File.dirname(__FILE__), 'game_machine.gemspec')
if File.exists?(specfile)
  require "bundler/gem_tasks"
end

RSpec::Core::RakeTask.new

task :default => :spec

namespace :game do
  task :demo do
    cp 'lib/demo/boot.rb', 'boot.rb'
  end
  
  task :none do
    rm 'boot.rb'
  end
end

namespace :java do

  task :clean do
    sh 'rm -f java/lib/*.jar'
    sh 'rm -f java/src/main/java/com/game_machine/entity_system/generated/*.java'
  end

  task :build => [:clean] do
    sh 'cd java && gradle clean && gradle codegen && gradle build && gradle install_libs'
  end
end

