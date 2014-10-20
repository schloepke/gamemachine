package com.game_machine.objectdb;

import java.io.IOException;

import com.game_machine.core.AppConfig;
import com.game_machine.core.CloudClient;

public class CloudStore implements Storable {

	private CloudClient client;

	public CloudStore() {
		this.client = CloudClient.getInstance();
	}

	@Override
	public boolean delete(String id) {
		CloudClient.CloudResponse response;
		try {
			response = client.delete(id);
			if (response.status == 200 || response.status == 204) {
				return true;
			} else if (response.status == 404) {
				return false;
			} else {
				throw new RuntimeException("GameCloud.get returned "+response.status);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void connect() {
		CloudClient.getInstance().setCredentials(AppConfig.Gamecloud.getHost(), AppConfig.Gamecloud.getUser(),
				AppConfig.Gamecloud.getApiKey());
	}

	@Override
	public boolean setString(String id, String message) {
		CloudClient.CloudResponse response;
		try {
			response = client.putString(id, message);
			if (response.status == 200 || response.status == 204) {
				return true;
			} else if (response.status == 404) {
				return false;
			} else {
				throw new RuntimeException("GameCloud.setString returned "+response.status);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean setBytes(String id, byte[] message) {
		CloudClient.CloudResponse response;
		try {
			response = client.putBytes(id, message);
			if (response.status == 200 || response.status == 204) {
				return true;
			} else if (response.status == 404) {
				return false;
			} else {
				throw new RuntimeException("GameCloud.setBytes returned "+response.status);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String getString(String id) {
		CloudClient.CloudResponse response;
		try {
			response = client.getString(id);
			if (response.status == 200) {
				return response.stringBody;
			} else if (response.status == 404) {
				return null;
			} else {
				throw new RuntimeException("GameCloud.getString returned "+response.status);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public byte[] getBytes(String id) {
		CloudClient.CloudResponse response;
		try {
			response = client.getBytes(id);
			if (response.status == 200) {
				return response.byteBody;
			} else if (response.status == 404) {
				return null;
			} else {
				throw new RuntimeException("GameCloud.getBytes returned "+response.status);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
	

}
