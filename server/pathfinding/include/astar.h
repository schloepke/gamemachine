
#include <ctype.h>
#include <stdio.h>
#include <memory.h>
#include <math.h>

#include <vector>
#include <iostream>


#include "common.h"
#include "micropather.h"
using namespace micropather;
using namespace std;

extern "C" EXPORT_API void addPathfinder(int i, int width, int height);
extern "C" EXPORT_API void setPassable(int pathfinder, int x, int y);
extern "C" EXPORT_API void setBlocked(int pathfinder, int x, int y);
extern "C" EXPORT_API int getXAt(int pathfinder, int index);
extern "C" EXPORT_API int getYAt(int pathfinder, int index);
extern "C" EXPORT_API int findpath2d(int pathfinder, int startx, int starty, int endx, int endy);


class Astar : public Graph {
	int width, height;
	vector<char> gmap;
	private:
		Astar( const Astar& );
		void operator=( const Astar& );
		MPVector<void*> path;
		MicroPather* pather;


	public:
		Astar(int a, int b);
		virtual ~Astar() {
			delete pather;
		}
		void setPassable(int x, int y);
		void setBlocked(int x, int y);
		int Passable( int nx, int ny ); 
		void ClearPath();
		int findPath(int startx, int starty, int endx, int endy);
		void NodeToXY( void* node, int* x, int* y );
		void* XYToNode( int x, int y );
		int getYAt(int i);
		int getXAt(int i);
		virtual float LeastCostEstimate( void* nodeStart, void* nodeEnd );
		virtual void AdjacentCost( void* node, micropather::MPVector< StateCost > *neighbors );
		virtual void PrintStateInfo( void* node );
		void Print();
};




