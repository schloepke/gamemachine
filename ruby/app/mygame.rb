
require_relative 'mygame/echo'

GameMachine::GameActor.register_system(Mygame::Echo)

GameMachine::ActorBuilder.new(Mygame::Echo).start
