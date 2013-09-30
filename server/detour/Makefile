bin:
	g++ -Wall -o pathfind -fpic pathfind.c -L./lib -I./include -lDetour

detour_lib:
	g++ -c -Wall -fpic pathfind.c -L./lib -I./include -lDetour
	g++ -shared -o libpathfind.so pathfind.o -L./lib -I./include -lDetour

load:
	g++ -Wall -o load -fpic tilemesh.cpp unitytilemesh.cpp main.cpp -L./lib -I./include -lDetour

all: detour_lib bin load

clean:
	rm -f pathfind
	rm -f pathfind.o
	rm -f libpathfind.so
	rm -f load

