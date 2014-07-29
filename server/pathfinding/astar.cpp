
#include <ctype.h>
#include <stdio.h>
#include <memory.h>
#include <math.h>

#include <vector>
#include <iostream>
#include <map>


#include "astar.h"
using namespace std;

map<int, Astar*> pathfinders;

extern "C" EXPORT_API void addPathfinder(int i, int width, int height) {
	Astar* pathfinder = new Astar(width,height);
	pathfinders[i] = pathfinder;
}

extern "C" EXPORT_API void setBlocked(int pathfinder, int x, int y) {
	Astar *astar = pathfinders[pathfinder];
	astar->setBlocked(x,y);
}

extern "C" EXPORT_API void setPassable(int pathfinder, int x, int y) {
	Astar *astar = pathfinders[pathfinder];
	astar->setPassable(x,y);
}

extern "C" EXPORT_API int getXAt(int pathfinder, int index) {
	Astar *astar = pathfinders[pathfinder];
	return astar->getXAt(index);
}

extern "C" EXPORT_API int getYAt(int pathfinder, int index) {
	Astar *astar = pathfinders[pathfinder];
	return astar->getYAt(index);
}

extern "C" EXPORT_API int findpath2d(int pathfinder, int startx, int starty, int endx, int endy)
{
	Astar *astar = pathfinders[pathfinder];
	astar->ClearPath();
	int res = astar->findPath(startx,starty,endx,endy);
	astar->Print();
	return res;
}

