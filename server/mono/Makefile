
dll:
	mcs -unsafe+ -lib:/home2/chris/game_machine/server/mono -pkg:dotnet -r:protobuf-net.dll -t:library -out:actor.dll messages.cs actor.cs 

all:
	gcc -c -Wall -fpic actor.c `pkg-config --cflags glib-2.0 --libs mono-2` -lm
	gcc -shared -o libactor.so actor.o `pkg-config --cflags glib-2.0 --libs mono-2` -lm
	
clean:
	rm -f actor
	rm -f libactor.so
	rm -f actor.o
	rm actor.dll

