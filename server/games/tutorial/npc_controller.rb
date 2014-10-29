require_relative 'npc'
module Tutorial
  class NpcController < GameMachine::Actor::Base
    include GameMachine
    include GameMachine::Commands

    attr_reader :name, :npc
    def post_init(*args)
      @name = args.first
      @npc = Npc.new(name)
      @eid = rand(100000).to_s
      @entity = MessageLib::Entity.new.set_id(@eid)
      #commands.misc.client_manager_register(name)
      game_message = MessageLib::GameMessage.new.set_player_id('update')
      schedule_message(game_message,30)
      #self.class.logger.info("Npc #{name} started")
    end

    def preStart
      
    end

    def on_receive(game_message)
      if game_message.get_player_id == 'update'
        npc.update
      end
    end

  end
end