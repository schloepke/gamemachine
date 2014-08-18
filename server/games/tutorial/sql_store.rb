module Tutorial
  class SqlStore
    include GameMachine
    
    def all_for_player(player_id)
      MessageLib::PlayerItem.orm_find_all(player_id)
    end

    def save_player_item(player_item,player_id)
      player_item.orm_save_async(player_id)
    end

    def delete_player_item(id,player_id)
      MessageLib::PlayerItem.orm_delete_async(id,player_id)
    end

  end
end