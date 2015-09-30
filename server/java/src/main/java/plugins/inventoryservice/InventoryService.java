package plugins.inventoryservice;

import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.Cost;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.PlayerItems;
import io.gamemachine.messages.RemovePlayerItem;
import io.gamemachine.messages.RequestPlayerItems;
import io.gamemachine.messages.UpdatePlayerItem;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class InventoryService {

	private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
	
	private static class LazyHolder {
		private static final InventoryService INSTANCE = new InventoryService();
	}

	public static InventoryService getInstance() {
		return LazyHolder.INSTANCE;
	}

	public Boolean exists(String id, String characterId) {
		if (find(id, characterId) == null) {
			return false;
		} else {
			return true;
		}
	}

	public PlayerItem catalogItem(String id) {
		return find(id,"global");
	}
	
	public PlayerItem find(String id, String characterId) {
		return PlayerItem.db().findFirst("player_item_character_id = ? AND player_item_id = ?", characterId, id);
	}

	public RequestPlayerItems requestPlayerItems(RequestPlayerItems request) {
		String characterId;
		if (request.characterId.equals("catalog")) {
			characterId = "global";
		} else {
			characterId = request.characterId;
		}
		
		List<PlayerItem> items;
		
		if (Strings.isNullOrEmpty(request.query)) {
			items = PlayerItem.db().where("player_item_character_id = ?", characterId);
		} else {
			items = PlayerItem.db().where("player_item_character_id = ? AND player_item_id = ?", characterId,request.query);
		}
		
		PlayerItems playerItems = new PlayerItems();
		for (PlayerItem playerItem : items) {
			playerItems.addPlayerItem(playerItem);
		}
		request.result = 1;
		request.playerItems = playerItems;
		return request;
	}
	
	public RemovePlayerItem removePlayerItem(RemovePlayerItem removePlayerItem) {
		PlayerItem playerItem = find(removePlayerItem.id, removePlayerItem.characterId);
		if (playerItem == null) {
			removePlayerItem.result = 0;
		} else {
			playerItem.quantity -= removePlayerItem.quantity;
			if (playerItem.quantity < 0) {
				PlayerItem.db().delete(playerItem.recordId);
			} else {
				playerItem.updatedAt = (int)System.currentTimeMillis();
				PlayerItem.db().save(playerItem);
			}
			
			removePlayerItem.result = 1;
		}
		return removePlayerItem;
	}
	
	public AddPlayerItem addPlayerItem(AddPlayerItem addPlayerItem) {
		int quantity = addPlayerItem.quantity;
		
		PlayerItem playerItem = find(addPlayerItem.id, addPlayerItem.characterId);
		if (playerItem == null) {
			playerItem = catalogItem(addPlayerItem.id);
			playerItem.characterId = addPlayerItem.characterId;
			playerItem.quantity = 0;
		}

		if (deductCost(playerItem.cost, addPlayerItem.characterId, quantity)) {
			playerItem.quantity += quantity;
			playerItem.updatedAt = (int)System.currentTimeMillis();
			PlayerItem.db().save(playerItem);
			addPlayerItem.playerItem = playerItem;
			addPlayerItem.result = 1;
			logger.warn("PlayerItem added "+addPlayerItem.id);
			return addPlayerItem;
		} else {
			addPlayerItem.result = 0;
			logger.warn("PlayerItem add failed "+addPlayerItem.id);
			return addPlayerItem;
		}
	}

	public UpdatePlayerItem updatePlayerItem(UpdatePlayerItem updatePlayerItem) {
		PlayerItem playerItem = updatePlayerItem.playerItem;
		
		PlayerItem existing = find(playerItem.id, playerItem.characterId);
		if (existing == null) {
			updatePlayerItem.result = 0;
			logger.warn("PlayerItem update failed does not exist "+playerItem.id);
			return updatePlayerItem;
		}

		if (playerItem.quantity > existing.quantity) {
			int quantityToAdd = playerItem.quantity - existing.quantity;
			if (deductCost(existing.cost, existing.characterId, quantityToAdd)) {
				existing.quantity += quantityToAdd;
			} else {
				updatePlayerItem.result = 0;
				logger.warn("PlayerItem update failed "+existing.id);
				return updatePlayerItem;
			}
		}
		
		existing.updatedAt = (int)System.currentTimeMillis();
		PlayerItem.db().save(existing);
		updatePlayerItem.playerItem = existing;
		updatePlayerItem.result = 1;
		logger.warn("PlayerItem added "+existing.id);
		return updatePlayerItem;
		
	}
	
	public Boolean deductCost(Cost cost, String characterId, int quantity) {
		if (cost == null) {
			return true;
		}

		PlayerItem costItem = find(cost.currency, characterId);
		if (costItem == null) {
			return false;
		} else {
			int quantityNeeded =  (int)Math.floor(cost.amount) * quantity;
			if (costItem.quantity >= quantityNeeded) {
				costItem.quantity -= quantityNeeded;
				costItem.updatedAt = (int)System.currentTimeMillis();
				PlayerItem.db().save(costItem);
				return true;
			} else {
				return false;
			}
		}
	}
}
