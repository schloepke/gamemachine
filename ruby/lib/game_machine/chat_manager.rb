module GameMachine
    class ChatManager < Actor

    register_components %w(ChatMessage JoinChat LeaveChat)

    def post_init(*args)
      @players = {}
    end

    def on_receive(entity)
      unless @players.has_key?(entity.player.id)
        create_chat_child(entity.player.id)
      end
      forward_chat_request(entity)
    end

    private

    def child_name(player_id)
      "chat#{player_id}"
    end

    def forward_chat_request(entity)
      @players[entity.player.id].tell(entity,nil)
    end

    def create_chat_child(player_id)
      child = ActorBuilder.new(Systems::Chat).with_parent(context).with_name(child_name(player_id)).start
      @players[player_id] = child
    end
  end
end
