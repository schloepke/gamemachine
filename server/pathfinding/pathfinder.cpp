
#include "pathfinder.h"

using namespace std;

static map<int, Crowd*> crowds;
static map<int, Navmesh*> navmeshes;
static map<int, Astar*> pathfinders;

// Navmesh externs
extern "C" EXPORT_API void addNavmesh(int id, const char *file) {
	if (navmeshes[id] == 0) {
		Navmesh* navmesh = new Navmesh(id,file);
		navmeshes[id] = navmesh;
	}
}

extern "C" EXPORT_API int findPath(int navmeshId, float startx, float starty,
    float startz, float endx, float endy, float endz, int find_straight_path,
    float* resultPath)
{
	Navmesh* navmesh = navmeshes[navmeshId];
	return navmesh->findPath(startx, starty, startz, endx, endy, endz, find_straight_path, resultPath);
}

extern "C" EXPORT_API void gmapSetPassable(int navmeshId, int a, int b, int radius)
{
	Navmesh* navmesh = navmeshes[navmeshId];
	navmesh->gmapSetPassable(a, b, radius);
}
extern "C" EXPORT_API void gmapSetBlocked(int navmeshId, int a, int b, int radius)
{
	Navmesh* navmesh = navmeshes[navmeshId];
	navmesh->gmapSetBlocked(a, b, radius);
}

// Crowd externs

// Add crowd using existing navmesh
extern "C" EXPORT_API void addCrowd(int id, int navmeshid) {
	if (crowds[id] == 0 && navmeshes[navmeshid] != 0)
	{
		Navmesh* navmesh = navmeshes[navmeshid];
		Crowd* crowd = new Crowd(navmesh);
		crowds[id] = crowd;
	}
}

extern "C" EXPORT_API int addAgent(int crowdId, const float* p, float accel,
 float speed, float radius, float height, int optflag, float sepWeight)
{
	Crowd* crowd = crowds[crowdId];
	return crowd->addAgent(p, accel, speed, radius, height, optflag, sepWeight);
}

extern "C" EXPORT_API void removeAgent(int crowdId, const int idx)
{
	Crowd* crowd = crowds[crowdId];
	crowd->removeAgent(idx);
}
extern "C" EXPORT_API void setMoveTarget(int crowdId, const float* p, bool adjust, int agentIdx)
{
	Crowd* crowd = crowds[crowdId];
	crowd->setMoveTarget(p, adjust, agentIdx);
}

extern "C" EXPORT_API void getAgentPosition(int crowdId, int idx, float* resultPath)
{
	Crowd* crowd = crowds[crowdId];
	const dtCrowdAgent* ag = crowd->getCrowd()->getAgent(idx);
	if (!ag->active)
        return;
    memcpy(resultPath, ag->npos, 3*sizeof(float));
}

extern "C" EXPORT_API void updateTick(int crowdId, const float dt)
{
	Crowd* crowd = crowds[crowdId];
	crowd->updateTick(dt);
}


// Astar 2d grid pathfinding externs

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