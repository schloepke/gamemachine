

require 'entity_system/game_request.rb'
require 'entity_system/entity.rb'
Dir["#{File.dirname(__FILE__)}/entity_system/components/*.rb"].each { |rb| require rb }
Dir["#{File.dirname(__FILE__)}/entity_system/systems/*.rb"].each { |rb| require rb }