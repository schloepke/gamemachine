jars = Dir[File.join(File.dirname(__FILE__), '*.jar')]
jars.each {|jar| require jar}

require_relative 'demo/jme'
require_relative 'demo/npc'

