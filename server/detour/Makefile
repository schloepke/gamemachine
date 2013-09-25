bin:
	g++ -Wall -o recast -fpic recast.c -L. -lDetour

lib:
	g++ -c -Wall -fpic recast.c -L. -lDetour
	g++ -shared -o libpath.so recast.o
clean:
	rm -f recast

