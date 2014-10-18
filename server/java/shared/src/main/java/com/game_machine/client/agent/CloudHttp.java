package com.game_machine.client.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CloudHttp {

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

	public static byte[] getAgents(String host, String playerId, String username, String apiKey) {
		
		String url = "http://" + host + "/api/agents/" + username + "/" + playerId;
		String token = hash256(username + playerId + apiKey);

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("X-auth",token);

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				return readFully(con.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNode(String host, String gameId)  {

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
