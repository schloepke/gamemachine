module GameMachine
  class PlayerAuthentication < GameActor

    def post_init
      @authenticated_players = {}
    end

    def authenticated?(player)
      (player && @authenticated_players.fetch(player.get_id,nil)) ? true : false
    end

    def authenticate(player)
      true
    end

    def on_receive(entity_list)
      player = entity_list.get_player
      if authenticated?(player)
        player.set_authenticated true
        sender.tell(entity_list,get_self)
      elsif authenticate(player)
        @authenticated_players[player.get_id] = true
        player.set_authenticated true
        sender.tell(entity_list,get_self)
      end
    end

  end
end
