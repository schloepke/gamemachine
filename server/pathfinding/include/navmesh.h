#define _USE_MATH_DEFINES
#define DT_VIRTUAL_QUERYFILTER 1

#include <cmath>
#include <limits>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
#include <math.h>
#include <map>
#include <float.h>
#include "Recast.h"
#include "DetourCommon.h"
#include "DetourNavMesh.h"
#include "DetourNavMeshBuilder.h"
#include "DetourNavMeshQuery.h"

#include "common.h"
#include "mesh_loader.h"

#define VERTEX_SIZE       3
#define INVALID_POLYREF   0

using namespace std;

class Navmesh {
	int meshId;
	dtNavMesh* mesh;
	dtNavMeshQuery* query;

public:
	int gwidth;
	int gheight;
	vector<int> gmap;
	

	static const int P_FAILURE = -1;
	static const int P_NO_START_POLY = -2;
	static const int P_NO_END_POLY = -3;
	static const int P_PATH_NOT_FOUND = -4;
	static const int P_MESH_NOT_FOUND = -5;
	static const int P_QUERY_NOT_FOUND = -6;


	static const int MAX_POLYS = 512;
	static const int MAX_STEER_POINTS = 10;
	static const int MAX_SMOOTH = 2048;
	static float const STEP_SIZE;
	static float const SLOP;

	enum SamplePolyFlags
	{
		SAMPLE_POLYFLAGS_WALK		= 0x01,		// Ability to walk (ground, grass, road)
		SAMPLE_POLYFLAGS_SWIM		= 0x02,		// Ability to swim (water).
		SAMPLE_POLYFLAGS_DOOR		= 0x04,		// Ability to move through doors.
		SAMPLE_POLYFLAGS_JUMP		= 0x08,		// Ability to jump.
		SAMPLE_POLYFLAGS_DISABLED	= 0x10,		// Disabled polygon
		SAMPLE_POLYFLAGS_ALL		= 0xffff	// All abilities.
	};

	Navmesh(int id, const char *file);
	void gmapSetPassable(int a, int b, int radius);
	void gmapSetBlocked(int a, int b, int radius);
	dtNavMeshQuery* getQuery();
	void EXPORT_API freeQuery(dtNavMeshQuery* query);
	int findPath(float startx,
	    float starty, float startz, float endx, float endy, float endz,
	    int find_straight_path, float* resultPath);
	float* getPathPtr(int max_paths);
	void freePath(float* path);
	dtNavMesh* getMesh();
	bool meshLoaded();

	int fixupCorridor(dtPolyRef* path, const int npath, const int maxPath,
             const dtPolyRef* visited, const int nvisited);

	bool getSteerTarget(dtNavMeshQuery* navQuery, const float* startPos, const float* endPos,
               const float minTargetDist,
               const dtPolyRef* path, const int pathSize,
               float* steerPos, unsigned char& steerPosFlag, dtPolyRef& steerPosRef,
               float* outPoints = 0, int* outPointCount = 0);

	int fixupShortcuts(dtPolyRef* path, int npath, dtNavMeshQuery* navQuery);
	inline bool inRange(const float* v1, const float* v2, const float r, const float h);
};

class myQueryFilter : public dtQueryFilter {

private:	
	float m_areaCost[DT_MAX_AREAS];		///< Cost per area type. (Used by default implementation.)
	unsigned short m_includeFlags;		///< Flags for polygons that can be visited. (Used by default implementation.)
	unsigned short m_excludeFlags;		///< Flags for polygons that should not be visted. (Used by default implementation.)
	Navmesh* navmesh;
public:
	myQueryFilter();
	~myQueryFilter();

	void setNavmesh(Navmesh* n);
	bool isPassable(const float* pa, const float* pb) const;
	bool passFilter(const dtPolyRef /*ref*/,
							   const dtMeshTile* /*tile*/,
							   const dtPoly* poly) const;
	

    float getCost(const float* pa, const float* pb,
							 const dtPolyRef /*prevRef*/, const dtMeshTile* /*prevTile*/, const dtPoly* /*prevPoly*/,
							 const dtPolyRef /*curRef*/, const dtMeshTile* /*curTile*/, const dtPoly* curPoly,
							 const dtPolyRef /*nextRef*/, const dtMeshTile* /*nextTile*/, const dtPoly* /*nextPoly*/) const;
	

};

