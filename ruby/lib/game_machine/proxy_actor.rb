module GameMachine
  class ProxyActor < UntypedActor
    class << self
      alias_method :apply, :new
      alias_method :create, :new
    end

    def self.test(entity_id, &blk)
      actor_system = GameMachineLoader.get_actor_system
      actor = actor_system.actor_of(Props.new(ProxyActor), ProxyActor.name)
      actor.tell([blk,entity_id],nil)
    end

    def initialize
      @count = 0
      @blk = nil
    end

    def onReceive(message)
      if @blk.nil?
        @blk = message[0]
        Query.get(message[1],self.get_self)
      else
        puts "ProxyActor calling block #{@blk.inspect}"
        @blk.call(message)
      end
    end
  end
end
