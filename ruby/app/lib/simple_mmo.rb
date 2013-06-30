
require_relative 'simple_mmo/echo'

GameMachine::SystemManager.register(SimpleMmo::Echo)

GameMachine::ActorBuilder.new(SimpleMmo::Echo).start
