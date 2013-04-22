require 'rubygems'
require 'socket'
require 'java'

Dir["#{File.dirname(__FILE__)}/../../java/game_machine/bin/*.jar"].each { |jar| require jar }
  
  module GameMachine
  end

