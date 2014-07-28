
#include <ctype.h>
#include <stdio.h>
#include <memory.h>
#include <math.h>

#include <vector>
#include <iostream>
#include <map>


#include "astar.h"
#include "pathfind.h"
using namespace std;

map<int, void*> pathfinders;

extern "C" EXPORT_API void addPathfinder(int i, int width, int height) {
	Astar pathfinder(width,height);
	pathfinders[i] = &pathfinder;
}

extern "C" EXPORT_API void setNode(int pathfinder, int x, int y, int state) {
	Astar *astar = static_cast<Astar*>(pathfinders[pathfinder]);
	astar->setNode(x,y,state);
}

extern "C" EXPORT_API void findpath2d(int pathfinder, int startx, int starty, int endx, int endy)
{
	Astar *astar = static_cast<Astar*>(pathfinders[pathfinder]);
}

