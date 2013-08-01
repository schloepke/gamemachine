
dll:
	mcs -t:library actor.cs

all:
	gcc -c -Wall -fpic actor.c `pkg-config --cflags --libs mono-2` -lm
	gcc -shared -o libactor.so actor.o
	
binary:
	gcc -Wall -o actor actor.c `pkg-config --cflags --libs mono-2` -lm

clean:
	rm -f actor
	rm -f libactor.so
	rm -f actor.o
	rm actor.dll

