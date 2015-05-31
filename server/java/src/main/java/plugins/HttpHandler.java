package plugins;

import java.util.Map;

public class HttpHandler {
	
	public static abstract class Response {
		public enum Status {
			OK,
			NOT_AUTHORIZED
		}
		public Status status;
	}
	
	public static class StringResponse extends Response {
		public StringResponse(Status status,String content) {
			this.status = status;
			this.content = content;
		}
		public String content;
	}
	
	public static class ByteResponse extends Response {
		public ByteResponse(Status status,byte[] content) {
			this.status = status;
			this.content = content;
		}
		public byte[] content;
	}
	
	// All requests that get here have a path that start with /api/game
	public static Response processRequest(String path,Map<String,String> params, boolean authenticated) {
		return new StringResponse(Response.Status.OK,"test");
	}
}
