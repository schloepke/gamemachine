package com.game_machine.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserApi {
	private PoolingHttpClientConnectionManager cm;
	private String apiKey;
	private String username;
	private String host;
	private ObjectMapper mapper;
	CloseableHttpClient httpClient;

	public class Response {
		public int status = 0;
		public String body;
		public JsonNode params;
	}

	public UserApi(String host, String username, String apiKey) {
		this.apiKey = apiKey;
		this.username = username;

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
			this.host = address.getHostAddress() + ":" + port;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		}

		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(5);
		cm.setDefaultMaxPerRoute(5);
		httpClient = HttpClients.custom().setConnectionManager(cm).build();

		mapper = new ObjectMapper();
	}

	
	@SuppressWarnings("unchecked")
	public Response updateCounts(String gameId, Integer messageCount, Integer connectionCount, Integer bytesTransferred) {
		String url = "http://" + host + "/api/user/update_counts/" + encodeURIComponent(gameId) + "/" + messageCount + "/" + connectionCount + "/" + bytesTransferred;
		String token = hash256(username + gameId + apiKey);
		HttpGet request = new HttpGet(url);
		request.setHeader("X-Auth", token);
		Response response = new Response();
		try {
			request.setHeader("X-Auth", token);
			response = httpClient.execute(request, new StringResponseHandler());
			if (response.status == 200) {
				response.params = mapper.readTree(response.body);
			}
		} catch (UnsupportedCharsetException | IOException e) {
			e.printStackTrace();
			response.body = e.getMessage();
		}
		return response;
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

	private class StringResponseHandler implements ResponseHandler<Response> {
		public Response handleResponse(final HttpResponse response) {
			Response apiResponse = new Response();
			int status = response.getStatusLine().getStatusCode();
			apiResponse.status = status;
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					apiResponse.body = EntityUtils.toString(entity);
				} catch (ParseException | IOException e) {
					e.printStackTrace();
					apiResponse.status = 500;
					apiResponse.body = e.getMessage();
				}
			}
			return apiResponse;
		}
	}
}
