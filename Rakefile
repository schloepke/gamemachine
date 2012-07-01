require "bundler/gem_tasks"
require 'rspec/core/rake_task'

RSpec::Core::RakeTask.new

task :default => :spec

task :build_proto do
  system("protoc src/main/java/com/game_machine/socket_server/GameSocketProtocol.proto --java_out=src/main/java")
end
