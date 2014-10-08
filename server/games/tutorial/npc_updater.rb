module Tutorial
  class NpcUpdater < GameMachine::Actor::Base
    include GameMachine::Commands
    include GameMachine

    attr :refs, :game_message
    def post_init(*args)
      names = args.first
      @refs = names.map {|name| GameMachine::Actor::Base.find(name)}
      @game_message = MessageLib::GameMessage.new.set_player_id('update')
      schedule_message('update',30)
    end


    def on_receive(message)
      if message == 'update'
        refs.each do |ref|
          ref.tell(game_message)
        end
      end
    end

  end
end