require 'rubygems'
require 'java'

dir = File.join(Dir.getwd,'lib').gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
Dir.entries(dir).each do |jar|
  file = File.join(dir,jar).gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
  unless File.directory?(file)
    puts "Loading #{file}"
    require file
  end
end

java_import 'com.game_machine.core.GameMachine'
java_import 'akka.actor.ActorRef'
java_import 'akka.actor.ActorSystem'
java_import 'akka.actor.Props'
java_import 'akka.actor.UntypedActor'

class TestActor < UntypedActor
  class << self
      alias_method :apply, :new
      alias_method :create, :new
    end
    
  def on_receive
  end
end

GameMachine.start

actor_system = GameMachine.get_actor_system
actor_system.actor_of(Props.new(TestActor), 'TestActor');
  
