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
  gradlew = File.join(File.dirname(__FILE__), 'java','gradlew')

  task :clean do
    FileUtils.rm_f 'java/lib/*.jar'
    FileUtils.rm_f 'java/src/main/java/com/game_machine/entity_system/generated/*.java'
  end

  task :all => [:clean] do
    system "cd java && #{gradlew} clean && #{gradlew} codegen && #{gradlew} build && #{gradlew} install_libs"
  end

  task :build do
    system "cd java && #{gradlew} build && #{gradlew} install_libs"
  end
end

