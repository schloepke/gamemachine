module Tutorial
  class SqlStore
    include GameMachine
    
    def all_for_player(player_id)
      MessageLib::PlayerItem.db.find_all(player_id)
    end

    def find_by_id(id,player_id,in_transaction=false)
      if in_transaction
        MessageLib::PlayerItem.db.find(id,player_id,true)
      else
        MessageLib::PlayerItem.db.find(id,player_id)
      end
    end

    def save_player_item(player_item,player_id,in_transaction=false)
      if in_transaction
        MessageLib::PlayerItem.db.save(player_id,player_item,true)
      else
        MessageLib::PlayerItem.db.save_async(player_id,player_item)
      end
    end

    def delete_player_item(id,player_id)
      MessageLib::PlayerItem.db.delete_async(id,player_id)
    end

  end
end