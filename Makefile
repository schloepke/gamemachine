binary:
	gcc -Wall -o recast recast.c -L. -ldetour

clean:
	rm -f recast

