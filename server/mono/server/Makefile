proxy:
	mcs -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:NLog.dll -r:protobuf-net.dll -out:server proxy_server.cs proxy_client.cs messages.cs message_util.cs message_router.cs actor.cs iactor.cs test_actor.cs

clean:
	rm -f actor
	rm -f libactor.so
	rm -f actor.o
	rm -f test_actor.dll
	rm -f test_actor.mdb

