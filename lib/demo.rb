jars = Dir[File.join(File.dirname(__FILE__), 'demo','*.jar')]
jars.each {|jar| require jar}

require_relative 'demo/jme'
require_relative 'demo/grid'
require_relative 'demo/npc_manager'

