module Demo
  class Npc < GameMachine::Actor::Base
    
    def post_init(*args)
      #node1 = JavaLib::Node.new(0,0)
      #node2 = JavaLib::Node.new(390,390)
      #finder = JavaLib::Pathfinding.new(JavaLib::Pathfinding::Algorithm::ASTAR,Grid.new)
      #finder.set_eight(true)
      #result = finder.find_path(node1,node2)
    end

    def on_receive(message)
      GameMachine.logger.debug("Npc got #{message}")
      #get_sender.tell(message,get_self)
    end

  end
end
