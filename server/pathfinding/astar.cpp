

#include <map>


#include "astar.h"
using namespace std;

Astar::Astar(int a, int b) : width(a), height(b), gmap(width*height+1), pather(0)
{
	pather = new MicroPather( this, 100);
}

void Astar::setPassable(int x, int y)
{
	gmap[ y*width + x] = '\0';
}

void Astar::setBlocked(int x, int y)
{
	gmap[ y*width + x] = 'D';
}

int Astar::Passable( int nx, int ny ) 
{
	if (    nx >= 0 && nx < width 
		 && ny >= 0 && ny < height )
	{
		int index = ny*width+nx;
		char c = gmap[ index ];
		if ( c == '\0' )
			return 1;
		else if ( c == 'D' )
			return 2;
	}		
	return 0;
}

void Astar::ClearPath()
{
	path.resize( 0 );
	pather->Reset();
}

int Astar::findPath(int startx, int starty, int endx, int endy)
{
	int result = 0;
	float totalCost;
	
	if (Passable(startx,starty) && Passable(endx,endy))
	{
		result = pather->Solve( XYToNode( startx, starty ), XYToNode( endx, endy ), &path, &totalCost );
		if ( result == MicroPather::SOLVED ) {
			return path.size();
		}
	}
	return result;
}

void Astar::NodeToXY( void* node, int* x, int* y ) 
{
	intptr_t index = (intptr_t)node;
	*y = index / width;
	*x = index - *y * width;
}

void* Astar::XYToNode( int x, int y )
{
	return (void*) ( y*width + x );
}

int Astar::getYAt(int i)
{
	int y;
	intptr_t index = (intptr_t)path[i];
	y = index / width;
	return y;
}

int Astar::getXAt(int i)
{
	int x,y;
	intptr_t index = (intptr_t)path[i];
	y = index / width;
	x = index - y * width;
	return x;
}

float Astar::LeastCostEstimate( void* nodeStart, void* nodeEnd ) 
{
	int xStart, yStart, xEnd, yEnd;
	NodeToXY( nodeStart, &xStart, &yStart );
	NodeToXY( nodeEnd, &xEnd, &yEnd );

	/* Compute the minimum path cost using distance measurement. It is possible
	   to compute the exact minimum path using the fact that you can move only 
	   on a straight line or on a diagonal, and this will yield a better result.
	*/
	int dx = xStart - xEnd;
	int dy = yStart - yEnd;
	return (float) sqrt( (double)(dx*dx) + (double)(dy*dy) );
}

void Astar::AdjacentCost( void* node, micropather::MPVector< StateCost > *neighbors ) 
{
	int x, y;
	const int dx[8] = { 1, 1, 0, -1, -1, -1, 0, 1 };
	const int dy[8] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	const float cost[8] = { 1.0f, 1.41f, 1.0f, 1.41f, 1.0f, 1.41f, 1.0f, 1.41f };

	NodeToXY( node, &x, &y );

	for( int i=0; i<8; ++i ) {
		int nx = x + dx[i];
		int ny = y + dy[i];

		int pass = Passable( nx, ny );
		if ( pass > 0 ) {
			if ( pass == 1) 
			{
				// Normal floor
				StateCost nodeCost = { XYToNode( nx, ny ), cost[i] };
				neighbors->push_back( nodeCost );
			}
			else 
			{
				// Normal floor
				StateCost nodeCost = { XYToNode( nx, ny ), FLT_MAX };
				neighbors->push_back( nodeCost );
			}
		}
	}
}

void Astar::PrintStateInfo( void* node ) 
{
	int x, y;
	NodeToXY( node, &x, &y );
	printf( "(%d,%d)", x, y );
}

void Astar::Print() 
{
	unsigned k;
	// Wildly inefficient demo code.
	unsigned size = path.size();
	for( k=0; k<size; ++k ) {
		int x, y;
		NodeToXY( path[k], &x, &y );
		fprintf (stderr, "%d.%d\n", x,y);
	}
}

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

