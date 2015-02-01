module Tutorial
  class SqlStore
    include GameMachine
    
    def all_for_player(player_id)
      MessageLib::PlayerItem.db.where('player_item_player_id = ?',player_id)
    end

    def find_by_id(id,player_id,in_transaction=false)
      where = 'player_item_id = ? AND player_item_player_id = ?'

      if in_transaction
        MessageLib::PlayerItem.db.where(true,where,id,player_id).first
      else
        MessageLib::PlayerItem.db.where(where,id,player_id).first
      end
    end

    def save_player_item(player_item,player_id,in_transaction=false)

      # Ugly.  This is here because of an api change.  item manager needs to be refactored a bit.
      player_item.set_player_id(player_id)
      if existing_item = find_by_id(player_item.id,player_id,in_transaction)
        player_item.set_record_id(existing_item.get_record_id)
      end

      GameMachine.logger.debug "save_player_item #{player_item.id} #{player_item.quantity}"
      if in_transaction
        MessageLib::PlayerItem.db.save(player_item,true)
      else
        MessageLib::PlayerItem.db.save_async(player_item)
      end
    end

    def delete_player_item(id,player_id)
      MessageLib::PlayerItem.db.delete_where_async("player_item_id = ? AND player_item_player_id = ?", id,player_id)
    end

  end
end