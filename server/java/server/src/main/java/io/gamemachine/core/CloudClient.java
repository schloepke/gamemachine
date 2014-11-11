package io.gamemachine.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

import GameMachine.Messages.NodeStatus;

public class CloudClient {

	public ConcurrentHashMap<String, CloseableHttpClient> clients = new ConcurrentHashMap<String, CloseableHttpClient>();
	private PoolingHttpClientConnectionManager cm;
	private String gamecloudApiKey;
	private String gamecloudUser;
	private String host;
	private static final Logger logger = LoggerFactory.getLogger(CloudClient.class);

	public class CloudResponse {
		public int status;
		public byte[] byteBody;
		public String stringBody;
	}

	private CloudClient() {
		// Some freaking moron at apache.org decided it was a good idea to
		// ignore log level and force WIRE
		// level whenever Hyperic sigar is detected.
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(10);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(10);
		// Increase max connections for localhost:80 to 50
		// HttpHost localhost = new HttpHost("locahost", 80);
		// cm.setMaxPerRoute(new HttpRoute(localhost), 50);
	}

	private static class LazyHolder {
		private static final CloudClient INSTANCE = new CloudClient();
	}

	public static CloudClient getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Sets the credentials for the gamecloud that are used in all requests
	 * 
	 * @param host
	 * @param username
	 * @param apiKey
	 */
	public void setCredentials(String host, String username, String apiKey) {
		gamecloudApiKey = apiKey;
		gamecloudUser = username;
		
		String[] hostPort = host.split(":");
		String port;
		if (hostPort.length == 1) {
			port = "80";
		} else {
			port = hostPort[1];
		}
		InetAddress address;
		try {
			address = InetAddress.getByName(hostPort[0]);
			this.host = address.getHostAddress()+":"+port;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public CloudResponse getAgents(String gameId) throws ClientProtocolException, IOException {
		String url = "http://" + host + "/api/agents/" + gamecloudUser + "/" + gameId;
		String token = hash256(gamecloudUser + gameId + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new ByteArrayResponseHandler());
	}
	
	public CloudResponse getNode(String gameId) throws ClientProtocolException, IOException {
		String url = "http://" + host + "/api/getnode/"+gameId;
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		return httpClient.execute(request, new StringResponseHandler());
	}
	
	/**
	 * Performs a left anchored fuzzy search by id
	 * 
	 * @param query
	 * @param limit
	 * @param format
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse query(String query, int limit, String format) throws ClientProtocolException, IOException {
		String url = "http://" + host + "/api/db-query/" + gamecloudUser + "/" + encodeURIComponent(query) + "/"
				+ limit + "/" + format;
		String token = hash256(gamecloudUser + query + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new ByteArrayResponseHandler());
	}

	/**
	 * Performs mass delete based on a left anchored fuzzy search by id
	 * 
	 * @param query
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse deleteMatching(String query) throws ClientProtocolException, IOException {
		String url = "http://" + host + "/api/db-delete-matching/" + gamecloudUser + "/" + encodeURIComponent(query);
		String token = hash256(gamecloudUser + query + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpDelete request = new HttpDelete(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new StringResponseHandler());
	}

	/**
	 * 
	 * @param nodeStatus
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse updateNodeStatus(NodeStatus nodeStatus) throws ClientProtocolException, IOException {
		String url = "http://" + host + "/api/update_node_status";
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpPost request = new HttpPost(url);
		ByteArrayEntity input = new ByteArrayEntity(nodeStatus.toByteArray(), ContentType.APPLICATION_OCTET_STREAM);
		request.setEntity(input);
		return httpClient.execute(request, new StringResponseHandler());
	}

	/**
	 * 
	 * @param id
	 * @param bytes
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse putBytes(String id, byte[] bytes) throws ClientProtocolException, IOException {
		String url = urlFrom(id, "bytes");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpPut request = new HttpPut(url);
		ByteArrayEntity input = new ByteArrayEntity(bytes, ContentType.APPLICATION_OCTET_STREAM);
		request.setEntity(input);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new StringResponseHandler());
	}

	/**
	 * 
	 * @param id
	 * @param value
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse putString(String id, String value) throws ClientProtocolException, IOException {
		String url = urlFrom(id, "json");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpPut request = new HttpPut(url);
		StringEntity input = new StringEntity(value, ContentType.TEXT_PLAIN);
		request.setEntity(input);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new StringResponseHandler());
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse delete(String id) throws ClientProtocolException, IOException {
		String url = urlFrom(id, null);
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpDelete request = new HttpDelete(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new StringResponseHandler());
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse getBytes(String id) throws ClientProtocolException, IOException {
		String url = urlFrom(id, "bytes");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new ByteArrayResponseHandler());
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloudResponse getString(String id) throws ClientProtocolException, IOException {
		String url = urlFrom(id, "json");
		String token = hash256(gamecloudUser + id + gamecloudApiKey);
		CloseableHttpClient httpClient = getClient("gamemachine");
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		return httpClient.execute(request, new StringResponseHandler());
	}

	private CloseableHttpClient getClient(String id) {
		CloseableHttpClient httpClient;
		if (clients.containsKey(id)) {
			httpClient = clients.get(id);
		} else {
			httpClient = HttpClients.custom().setConnectionManager(cm).build();
			clients.put(id, httpClient);
		}
		return httpClient;
	}

	private String hash256(String data) {
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

	private String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
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

	private String urlFrom(String id, String format) throws UnsupportedEncodingException {
		if (format == null) {
			return "http://" + host + "/api/db/" + gamecloudUser + "/" + encodeURIComponent(id);
		} else {
			return "http://" + host + "/api/db/" + gamecloudUser + "/" + encodeURIComponent(id) + "/" + format;
		}

	}

	private class StringResponseHandler implements ResponseHandler<CloudResponse> {
		public CloudResponse handleResponse(final HttpResponse response) throws IOException {
			CloudResponse couchResponse = new CloudResponse();
			int status = response.getStatusLine().getStatusCode();
			couchResponse.status = status;
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				couchResponse.stringBody = EntityUtils.toString(entity);
			}
			return couchResponse;
		}
	}

	private class ByteArrayResponseHandler implements ResponseHandler<CloudResponse> {
		public CloudResponse handleResponse(final HttpResponse response) throws IOException {
			int status = response.getStatusLine().getStatusCode();
			CloudResponse couchResponse = new CloudResponse();
			couchResponse.status = status;
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				couchResponse.byteBody = EntityUtils.toByteArray(entity);
			}
			return couchResponse;
		}
	}

}
