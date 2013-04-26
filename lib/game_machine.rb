require 'rubygems'
require 'socket'
require 'java'

Dir["#{File.dirname(__FILE__)}/../java_lib/*.jar"].each { |jar| require jar }
  
  module GameMachine
  end
  
  #java_import 'com.game_machine.systems.Root'
  #java_import 'com.game_machine.server.UdpServer'
  
  #require 'game_machine/server.rb'

