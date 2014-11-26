require 'rubygems'

require 'benchmark'
require 'java'

ENV['APP_ROOT'] = File.expand_path(File.dirname(__FILE__))
jars = Dir[File.join(ENV['APP_ROOT'], '../lib', '*.jar')]
jars.each do |jar|
  if !File.exists?(jar)
    puts "#{jar} not found"
  end
  require jar
end

java_import 'io.gamemachine.pathfinding.Node'
java_import 'io.gamemachine.pathfinding.MeshImporter'
