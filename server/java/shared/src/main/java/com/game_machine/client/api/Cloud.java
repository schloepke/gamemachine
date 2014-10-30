package com.game_machine.client.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.game_machine.client.agent.Config;
import com.google.common.util.concurrent.RateLimiter;

public class Cloud {

	private static final RateLimiter limiter = RateLimiter.create(Config.getInstance().getRateLimit());
	private String host;
	private String username;
	private String apiKey;
	private String gameId;
	private Config conf = Config.getInstance();

	public Cloud() {
		this.host = conf.getCloudHost();
		this.username = conf.getCloudUser();
		this.apiKey = conf.getCloudApiKey();
		this.gameId = conf.getGameId();
	}

	public class Response {
		public int status = 0;
		public byte[] byteBody;
		public String stringBody;
		public String error;
	}

	public class ByteResponse {
		private int status = 0;
		private byte[] body = null;
		private String error;

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public byte[] getBody() {
			return body;
		}

		public void setBody(byte[] body) {
			this.body = body;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
	}

	public class StringResponse {
		private int status = 0;
		private String body = null;
		private String error;

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
	}

	private static String hash256(String data) {
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			return bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}

	public static String readFully(InputStream inputStream, String encoding) throws IOException {
		return new String(readFully(inputStream), encoding);
	}

	private static byte[] readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}

	private String encodeURIComponent(String s) {
		String result;

		try {
			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

	private String urlFrom(String id, String format) {
		if (format == null) {
			return "http://" + host + "/api/db/" +gameId+"/" + username + "/" + encodeURIComponent(id);
		} else {
			return "http://" + host + "/api/db/" +gameId+"/" + username + "/" + encodeURIComponent(id) + "/" + format;
		}

	}

	public static String scopeId(String scope, String id) {
		return scope + "^^" + id;
	}

    public static String unscopeId(String id) {
    	if (id.contains("^^")) {
    		String[] parts = id.split("^^");
        	return parts[1];
    	} else {
    		throw new RuntimeException("Expected "+id+" to contain ##");
    	}
    }
    
	private void checkLimit() {
		if (!limiter.tryAcquire()) {
			throw new Api.RateLimitExceeded("Cloud rate limit exceeded");
		}
	}

	public <T> boolean save(String id, Class<T> klass,T message) {
		String scopedId = scopeId(klass.getSimpleName(),id);
		StringResponse response = put(scopedId,DynamicMessageUtil.toJson(klass, message));
		if (response.getStatus() == 204) {
			return true;
		} else {
			System.out.println("Error saving message "+scopedId+" status=" + response.getStatus());
			return false;
		}
	}
	
	public <T> T find(String id, Class<T> klass) {
		String scopedId = scopeId(klass.getSimpleName(),id);
		StringResponse response = getString(scopedId);
		if (response.status == 200) {
			T message = DynamicMessageUtil.fromJson(klass, response.body);
			return message;
		} else {
			return null;
		}
	}
	
	public boolean delete(String id, Class<?> klass) {
		String scopedId = scopeId(klass.getSimpleName(),id);
		StringResponse response = deleteUnscoped(scopedId);
		if (response.getStatus() == 204) {
			return true;
		} else {
			System.out.println("Error deleting message "+scopedId+" status=" + response.getStatus());
			return false;
		}
	}
	
	public StringResponse put(String id, String body) {
		checkLimit();
		String urlString = urlFrom(id, "json");
		String token = hash256(username + id + apiKey);
		StringResponse response = new StringResponse();
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("PUT");
			con.setRequestProperty("X-auth", token);
			con.setRequestProperty("Content-Type", "text/plain");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(body);
			wr.flush();
			wr.close();

			response.setStatus(con.getResponseCode());
			response.setBody(readFully(con.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setBody(e.getMessage());
		}
		return response;
	}

	public StringResponse put(String id, byte[] body) {
		checkLimit();
		String urlString = urlFrom(id, "bytes");
		String token = hash256(username + id + apiKey);
		StringResponse response = new StringResponse();
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("PUT");
			con.setRequestProperty("X-auth", token);
			con.setRequestProperty("Content-Type", "application/octet-stream");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(body);
			wr.flush();
			wr.close();

			response.setStatus(con.getResponseCode());
			response.setBody(readFully(con.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setBody(e.getMessage());
		}
		return response;
	}

	public StringResponse getString(String id) {
		StringResponse stringResponse = new StringResponse();
		Response response = get(id, "json");
		stringResponse.setBody(response.stringBody);
		stringResponse.setStatus(response.status);
		return stringResponse;
	}

	public ByteResponse getBytes(String id) {
		ByteResponse byteResponse = new ByteResponse();
		Response response = get(id, "bytes");
		byteResponse.setBody(response.byteBody);
		byteResponse.setStatus(response.status);
		return byteResponse;
	}

	public Response get(String id, String format) {
		checkLimit();
		String urlString = urlFrom(id, format);
		String token = hash256(username + id + apiKey);
		Response response = new Response();

		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("X-auth", token);

			response.status = con.getResponseCode();
			if (response.status != 200) {
				return response;
			}
			
			if (format.equals("bytes")) {
				response.byteBody = readFully(con.getInputStream());
			} else {
				response.stringBody = readFully(con.getInputStream(), "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	
	public StringResponse deleteUnscoped(String id) {
		checkLimit();
		String urlString = urlFrom(id, null);
		String token = hash256(username + id + apiKey);
		StringResponse response = new StringResponse();

		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("DELETE");
			con.setRequestProperty("X-auth", token);

			response.setStatus(con.getResponseCode());
			response.setBody(readFully(con.getInputStream(), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public byte[] getAgents(String playerId) {
		String url = "http://" + host + "/api/agents/" + username + "/" + playerId;
		String token = hash256(username + playerId + apiKey);

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("X-auth", token);

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				return readFully(con.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getAgentJarfile(String playerId) {
		String url = "http://" + host + "/api/agent_jarfile/" + playerId;

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				return readFully(con.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getNode() {
		String url = "http://" + host + "/api/getnode/" + gameId;

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				return readFully(con.getInputStream(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
