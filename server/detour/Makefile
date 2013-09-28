bin:
	g++ -Wall -o pathfind -fpic pathfind.c path2.c -L. -lDetour

lib:
	g++ -c -Wall -fpic pathfind.c path2.c -L. -lDetour
	g++ -shared -o libpathfind.so pathfind.o path2.o -L. -lDetour

clean:
	rm -f pathfind
	rm -f pathfind.o
	rm -f libpathfind.so

