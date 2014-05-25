
client:
	mcs -debug+ -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:System.Configuration.dll -r:System.Net.Http.dll -out:client.exe client.cs

nancy:
	mcs -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:System.Configuration.dll -r:Nancy.dll -r:Nancy.Hosting.Self.dll -r:System.Net.Http.dll -r:protobuf-net.dll -out:server.exe resource_module.cs actor.cs messages.cs iactor.cs test_actor.cs server.cs

dll:
#	mcs -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:protobuf-net.dll -t:library -out:actor.dll messages.cs iactor.cs actor.cs 
	mcs -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:System.Configuration.dll -r:System.Net.Http.dll -r:protobuf-net.dll -t:library -out:test_actor.dll actor.cs messages.cs iactor.cs test_actor.cs 

akka:
	mcs -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:System.Configuration.dll -r:Google.ProtocolBuffers.dll -r:Google.ProtocolBuffers.Serialization.dll -r:Helios.dll -r:Newtonsoft.Json.dll -r:Akka.Remote.dll -r:Akka.dll -out:akka_test.exe akka_test.cs

proxy:
	mcs -lib:/home/chris/game_machine/server/mono -pkg:dotnet -r:protobuf-net.dll -out:proxy.exe proxy_server.cs proxy_client.cs messages.cs message_util.cs message_router.cs actor.cs iactor.cs test_actor.cs

all:
	gcc  -g -c -Wall -fpic actor.c `pkg-config --cflags glib-2.0 --libs mono-2` -lm
	gcc  -g -shared -o libactor.so actor.o `pkg-config --cflags glib-2.0 --libs mono-2` -lm
	
clean:
	rm -f actor
	rm -f libactor.so
	rm -f actor.o
	rm -f test_actor.dll
	rm -f test_actor.mdb

