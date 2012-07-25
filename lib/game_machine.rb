require 'rubygems'
require 'socket'
require 'rack'
#require 'sinatra'
require 'java'

Dir["#{File.dirname(__FILE__)}/../jars/*.jar"].each { |jar| require jar }

require 'protobuf/message/message'
require 'protobuf/message/enum'
require 'protobuf/message/service'
require 'protobuf/message/extend'
require 'game_machine/server/socket_server'
require 'game_machine/server/http_server'