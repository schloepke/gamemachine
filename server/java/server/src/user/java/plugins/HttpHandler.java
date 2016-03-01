package plugins;

import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.RemovePlayerItem;
import io.gamemachine.messages.RequestPlayerItems;
import io.gamemachine.messages.UpdatePlayerItem;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import plugins.inventoryservice.InventoryService;

public class HttpHandler {

    public static abstract class Response {
        public enum Status {
            OK,
            NOT_AUTHORIZED
        }

        public Status status;
    }

    public static class StringResponse extends Response {
        public StringResponse(Status status, String content) {
            this.status = status;
            this.content = content;
        }

        public String content;
    }

    public static class ByteResponse extends Response {
        public ByteResponse(Status status, byte[] content) {
            this.status = status;
            this.content = content;
        }

        public byte[] content;
    }

    // All requests that get here have a path that start with /api/game
    public static Response processRequest(String path, Map<String, String> params, boolean authenticated) throws Exception {
        if (path.startsWith("/api/game/inventory")) {
            byte[] bytes = Base64.decodeBase64(params.get("content"));

            if (path.startsWith("/api/game/inventory/request_player_items")) {
                RequestPlayerItems request = RequestPlayerItems.parseFrom(bytes);
                request = InventoryService.instance().requestPlayerItems(request);
                return new ByteResponse(Response.Status.OK, request.toByteArray());
            } else if (path.startsWith("/api/game/inventory/add_player_item")) {
                AddPlayerItem request = AddPlayerItem.parseFrom(bytes);
                request = InventoryService.instance().addPlayerItem(request);
                return new ByteResponse(Response.Status.OK, request.toByteArray());
            } else if (path.startsWith("/api/game/inventory/update_player_item")) {
                UpdatePlayerItem request = UpdatePlayerItem.parseFrom(bytes);
                request = InventoryService.instance().updatePlayerItem(request);
                return new ByteResponse(Response.Status.OK, request.toByteArray());
            } else if (path.startsWith("/api/game/inventory/remove_player_item")) {
                RemovePlayerItem request = RemovePlayerItem.parseFrom(bytes);
                request = InventoryService.instance().removePlayerItem(request);
                return new ByteResponse(Response.Status.OK, request.toByteArray());
            } else {
                return new StringResponse(Response.Status.NOT_AUTHORIZED, "bad_request");
            }

        } else {
            return new StringResponse(Response.Status.OK, "test");
        }

    }
}
