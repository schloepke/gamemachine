package io.gamemachine.net.http;

// Bridge to call into ruby
public interface HttpHelper {
	String client_auth_response(Integer authtoken);
	String clusterinfo();
}
