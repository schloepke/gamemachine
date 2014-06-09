server:
	mcs -pkg:dotnet -r:NLog.dll -r:protobuf-net.dll -out:server.exe proxy_server.cs proxy_client.cs messages.cs message_util.cs message_router.cs actor.cs iactor.cs test_actor.cs

clean:
	rm -f server.exe

