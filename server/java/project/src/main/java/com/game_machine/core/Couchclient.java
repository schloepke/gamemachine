package com.game_machine.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Couchclient {

	public ConcurrentHashMap<String, CloseableHttpClient> clients = new ConcurrentHashMap<String, CloseableHttpClient>();
	private PoolingHttpClientConnectionManager cm;
	private String gamecloudApiKey;
	private String gamecloudUser;
	private String host;
	private static final Logger logger = LoggerFactory.getLogger(Couchclient.class);

	public class CouchResponse {
		public int status;
		public byte[] body;
		public String error;
	}

	private Couchclient() {
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(10);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(10);
		// Increase max connections for localhost:80 to 50
		// HttpHost localhost = new HttpHost("locahost", 80);
		// cm.setMaxPerRoute(new HttpRoute(localhost), 50);
	}

	private static class LazyHolder {
		private static final Couchclient INSTANCE = new Couchclient();
	}

	public static Couchclient getInstance() {
		return LazyHolder.INSTANCE;
	}

	public void setCredentials(String host, String username, String apiKey) {
		gamecloudApiKey = apiKey;
		gamecloudUser = username;
		this.host = host;
	}

	public CloseableHttpClient getClient(String id) {
		CloseableHttpClient httpClient;
		if (clients.containsKey(id)) {
			httpClient = clients.get(id);
		} else {
			httpClient = HttpClients.custom().setConnectionManager(cm).build();
			clients.put(id, httpClient);
		}
		return httpClient;
	}

	public static String hash256(String data) {
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

	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}

	public  String encodeURIComponent(String s) {
	    String result;

	    try {
	        result = URLEncoder.encode(s, "UTF-8")
	                .replaceAll("\\+", "%20")
	                .replaceAll("\\%21", "!")
	                .replaceAll("\\%27", "'")
	                .replaceAll("\\%28", "(")
	                .replaceAll("\\%29", ")")
	                .replaceAll("\\%7E", "~");
	    } catch (UnsupportedEncodingException e) {
	        result = s;
	    }

	    return result;
	}
	
	public String urlFrom(String id, String format) throws UnsupportedEncodingException {
		if (format == null) {
			return "http://"+host+"/db/" + gamecloudUser + "/" + encodeURIComponent(id);
		} else {
			return "http://"+host+"/db/" + gamecloudUser + "/" + encodeURIComponent(id) + "/" + format;
		}
		
	}
	
	public void putBytes(String id, byte[] bytes) throws ClientProtocolException, IOException {
		String url = urlFrom(id,"bytes");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpPut request = new HttpPut(url);
		ByteArrayEntity input = new ByteArrayEntity(bytes, ContentType.APPLICATION_OCTET_STREAM);
		request.setEntity(input);
		request.setHeader("X-Auth", token);
		httpClient.execute(request, new ByteArrayResponseHandler());
	}
	
	public void putString(String id, String value) throws ClientProtocolException, IOException {
		String url = urlFrom(id,"json");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpPut request = new HttpPut(url);
		StringEntity input = new StringEntity(value,ContentType.TEXT_PLAIN);
		request.setEntity(input);
		request.setHeader("X-Auth", token);
		httpClient.execute(request, new StringResponseHandler());
	}

	public void delete(String id) throws ClientProtocolException, IOException {
		String url = urlFrom(id,null);
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpDelete request = new HttpDelete(url);
		request.setHeader("X-Auth", token);
		httpClient.execute(request, new ByteArrayResponseHandler());
	}

	public byte[] getBytes(String id) throws ClientProtocolException, IOException {
		String url = urlFrom(id, "bytes");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new ByteArrayResponseHandler());
	}
	
	public String getString(String id) throws ClientProtocolException, IOException {
		String url = urlFrom(id, "json");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new StringResponseHandler());
	}

	private class StringResponseHandler implements ResponseHandler<String> {
		public String handleResponse(final HttpResponse response) throws IOException {
			int status = response.getStatusLine().getStatusCode();
			if (status == 200 || status == 204) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity);
				} else {
					return null;
				}
			} else if (status == 404) {
				return null;
			} else {
				throw new RuntimeException("Unexpected response status: " + status);
			}
		}
	}
	
	private class ByteArrayResponseHandler implements ResponseHandler<byte[]> {
		public byte[] handleResponse(final HttpResponse response) throws IOException {
			int status = response.getStatusLine().getStatusCode();
			if (status == 200 || status == 204) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				} else {
					return null;
				}
			} else if (status == 404) {
				return null;
			} else {
				throw new RuntimeException("Unexpected response status: " + status);
			}
		}
	}
	
}
