package plugins.inventoryservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.Cost;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.PlayerItems;
import io.gamemachine.messages.RemovePlayerItem;
import io.gamemachine.messages.RequestPlayerItems;
import io.gamemachine.messages.UpdatePlayerItem;
import plugins.core.combat.ClientDbLoader;

public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private Map<String, PlayerItem> catalog = new HashMap<String, PlayerItem>();

    private InventoryService() {
        for (PlayerItem playerItem : ClientDbLoader.getPlayerItems().playerItem) {
            catalog.put(playerItem.name, playerItem);
        }
    }

    private static class LazyHolder {
        private static final InventoryService INSTANCE = new InventoryService();
    }

    public static InventoryService instance() {
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
        return find(id, "global");
    }

    public Boolean hasItem(String id, String characterId) {
        if (find(id, characterId) == null) {
            return false;
        } else {
            return true;
        }
    }

    public PlayerItem find(String id, String characterId) {
        return PlayerItem.db().findFirst("player_item_character_id = ? AND player_item_id = ?", characterId, id);
    }

    public RequestPlayerItems requestPlayerItems(RequestPlayerItems request) {
        String characterId;
        characterId = request.characterId;

        List<PlayerItem> items;

        if (Strings.isNullOrEmpty(request.query)) {
            items = PlayerItem.db().where("player_item_character_id = ?", characterId);
        } else {
            items = PlayerItem.db().where("player_item_character_id = ? AND player_item_id = ?", characterId,
                    request.query);
        }

        PlayerItems playerItems = new PlayerItems();
        for (PlayerItem playerItem : items) {
            playerItems.addPlayerItem(playerItem);
        }
        request.result = 1;
        request.playerItems = playerItems;
        return request;
    }

    public synchronized RemovePlayerItem removePlayerItem(RemovePlayerItem removePlayerItem) {
        logger.warn("PlayerItem remove " + removePlayerItem.id);
        PlayerItem playerItem = find(removePlayerItem.id, removePlayerItem.characterId);
        if (playerItem == null) {
            removePlayerItem.result = 0;
        } else {
            PlayerItem.db().delete(playerItem.recordId);
            removePlayerItem.result = 1;
        }
        return removePlayerItem;
    }

    public synchronized AddPlayerItem addPlayerItem(AddPlayerItem addPlayerItem) {
        if (!catalog.containsKey(addPlayerItem.playerItem.name)) {
            logger.warn("PlayerItem not in catalog " + addPlayerItem.playerItem.name);
            addPlayerItem.result = 0;
            return addPlayerItem;
        }

        PlayerItem playerItem = addPlayerItem.playerItem;
        PlayerItem existing = find(playerItem.id, playerItem.characterId);
        if (existing != null) {
            logger.warn("PlayerItem duplicate not added " + playerItem.id);
            addPlayerItem.result = 0;
            return addPlayerItem;
        }

        playerItem.updatedAt = (int) System.currentTimeMillis();
        PlayerItem.db().save(playerItem);
        //logger.warn("PlayerItem added " + playerItem.id);
        addPlayerItem.result = 1;
        return addPlayerItem;
    }

    public synchronized UpdatePlayerItem updatePlayerItem(UpdatePlayerItem updatePlayerItem) {
        PlayerItem playerItem = updatePlayerItem.playerItem;

        PlayerItem existing = find(playerItem.id, playerItem.characterId);
        if (existing == null) {
            playerItem.updatedAt = (int) System.currentTimeMillis();
            PlayerItem.db().save(playerItem);
            //logger.warn("PlayerItem added " + playerItem.id);
            updatePlayerItem.result = 1;
            return updatePlayerItem;
        }

        playerItem.recordId = existing.recordId;
        playerItem.updatedAt = (int) System.currentTimeMillis();
        PlayerItem.db().save(playerItem);
        updatePlayerItem.result = 1;
        //logger.warn("PlayerItem updated " + playerItem.id);
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
            int quantityNeeded = (int) Math.floor(cost.amount) * quantity;
            if (costItem.quantity >= quantityNeeded) {
                costItem.quantity -= quantityNeeded;
                costItem.updatedAt = (int) System.currentTimeMillis();
                PlayerItem.db().save(costItem);
                return true;
            } else {
                return false;
            }
        }
    }
}
