module Tutorial
  class ObjectStore
    include GameMachine
    include GameMachine::Commands

    attr_reader :player_items_cache

    def initialize
      @player_items_cache = {}
    end

    def all_for_player(player_id)
      if entity = MessageLib::Entity.db_get(player_items_key(player_id),5000)
        player_items_cache[player_id] = entity.get_player_items
      else
        player_items_cache[player_id] = MessageLib::PlayerItems.new
        save_player_items(player_items_cache[player_id],player_id)
      end

      player_items_cache[player_id].get_player_item_list || []
    end

    def delete_player_item(id,player_id)
      ensure_player_cache(player_id)
      player_items = player_items_cache[player_id]
      player_item = MessageLib::PlayerItem.new.set_id(id)
      player_items.remove_player_item_by_id(player_item)
    end

    def save_player_item(player_item,player_id)
      ensure_player_cache(player_id)
      player_items = player_items_cache[player_id]
      player_items.remove_player_item_by_id(player_item)
      player_items.add_player_item(player_item)
      save_player_items(player_items,player_id)
    end

    private

    def ensure_player_cache(player_id)
      unless player_items_cache.has_key?(player_id)
        all_for_player(player_id)
      end
    end

    def save_player_items(player_items,player_id)
      entity = MessageLib::Entity.new
      entity.set_player_items(player_items)
      entity.set_id(player_items_key(player_id))
      entity.db_put
    end

    def player_items_key(player_id)
      "player_items_#{player_id}"
    end

  end
end