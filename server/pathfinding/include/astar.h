
#include <ctype.h>
#include <stdio.h>
#include <memory.h>
#include <math.h>

#include <vector>
#include <iostream>


#include "micropather.h"
using namespace micropather;
using namespace std;

struct MapNode {
	int x;
	int y;
	int state;
};

const int BLOCKED = 0;
const int OPEN = 1;
const int MAPX = 2000;
const int MAPY = 2000;



class Astar : public Graph {
	int width, height;
	vector<vector<MapNode> > gmap;
	private:
		Astar( const Astar& );
		void operator=( const Astar& );
		MPVector<void*> path;
		MicroPather* pather;


	public:
		Astar(int a, int b) : width(a), height(b), gmap(MAPX, std::vector<MapNode>(MAPY)), pather(0)
		{
			pather = new MicroPather( this, 100 );
		}

		virtual ~Astar() {
			delete pather;
		}

		void setPassable(int x, int y)
		{
			setNode(x,y,OPEN);
		}

		void setBlocked(int x, int y)
		{
			setNode(x,y,BLOCKED);
		}

		void setNode(int x, int y, int state) {
			MapNode node;
			node.x = x;
			node.y = y;
			node.state = state;
			gmap[x][y] = node;
		}

		int Passable( int nx, int ny ) 
		{
			if (    nx >= 0 && nx < width 
				 && ny >= 0 && ny < height )
			{
				MapNode node = gmap[nx][ny];
				return node.state;
			}		
			return 0;
		}

		void ClearPath()
		{
			path.resize( 0 );
		}

		int findPath(int startx, int starty, int endx, int endy)
		{
			int result = 0;
			float totalCost;
			MapNode startNode = gmap[startx][starty];
			MapNode endNode = gmap[endx][endy];

			if (Passable(startx,starty) && Passable(endx,endy))
			{
				result = pather->Solve( &startNode, &endNode, &path, &totalCost );
			}
			return result;
		}

		virtual float LeastCostEstimate( void* nodeStart, void* nodeEnd ) 
		{
			struct MapNode* mapNodeStart = (struct MapNode *)nodeStart;
			struct MapNode* mapNodeEnd = (struct MapNode *)nodeEnd;
			
			/* Compute the minimum path cost using distance measurement. It is possible
			   to compute the exact minimum path using the fact that you can move only 
			   on a straight line or on a diagonal, and this will yield a better result.
			*/
			int dx = mapNodeStart->x - mapNodeEnd->x;
			int dy = mapNodeStart->y - mapNodeEnd->y;
			return (float) sqrt( (double)(dx*dx) + (double)(dy*dy) );
		}

		virtual void AdjacentCost( void* node, micropather::MPVector< StateCost > *neighbors ) 
		{
			int x, y;
			const int dx[8] = { 1, 1, 0, -1, -1, -1, 0, 1 };
			const int dy[8] = { 0, 1, 1, 1, 0, -1, -1, -1 };
			const float cost[8] = { 1.0f, 1.41f, 1.0f, 1.41f, 1.0f, 1.41f, 1.0f, 1.41f };

			struct MapNode* mapNode = (struct MapNode *)node;
			x = mapNode->x;
			y = mapNode->y;

			for( int i=0; i<8; ++i ) {
				int nx = x + dx[i];
				int ny = y + dy[i];

				int pass = Passable( nx, ny );
				if ( pass > 0 ) {

					MapNode mnode = gmap[nx][ny];
					if ( pass == 1) 
					{
						// Normal floor
						StateCost nodeCost = { &mnode, cost[i] };
						neighbors->push_back( nodeCost );
					}
					else 
					{
						// Normal floor
						StateCost nodeCost = { &mnode, FLT_MAX };
						neighbors->push_back( nodeCost );
					}
				}
			}
		}

		virtual void PrintStateInfo( void* node ) 
	{
		
	}

};


