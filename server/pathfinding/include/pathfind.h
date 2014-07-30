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

static const int P_FAILURE = -1;
static const int P_NO_START_POLY = -2;
static const int P_NO_END_POLY = -3;
static const int P_PATH_NOT_FOUND = -4;
static const int P_MESH_NOT_FOUND = -5;
static const int P_QUERY_NOT_FOUND = -6;

static dtNavMesh* meshes[1024];
static dtNavMeshQuery* queries[4096];
static const int MAX_POLYS = 512;
static const int MAX_STEER_POINTS = 10;
static const int MAX_SMOOTH = 2048;
static const float STEP_SIZE = 0.5f;
static const float SLOP = 0.01f;

static const int gwidth = 2000;
static const int gheight = 2000;
static vector<int> gmap (gwidth*gheight+1);

extern "C" EXPORT_API void gmapSetPassable(int a, int b, int radius);
extern "C" EXPORT_API void gmapSetBlocked(int a, int b, int radius);
extern "C" EXPORT_API int loadNavMesh(int map, const char *file);

extern "C" EXPORT_API dtNavMeshQuery* getQuery(int map);

extern "C" void EXPORT_API freeQuery(dtNavMeshQuery* query);

extern "C" EXPORT_API int findPath(dtNavMeshQuery* query, float startx,
    float starty, float startz, float endx, float endy, float endz,
    int find_straight_path, float* resultPath);

extern "C" EXPORT_API float* getPathPtr(int max_paths);

extern "C" EXPORT_API void freePath(float* path);

enum SamplePolyFlags
{
	SAMPLE_POLYFLAGS_WALK		= 0x01,		// Ability to walk (ground, grass, road)
	SAMPLE_POLYFLAGS_SWIM		= 0x02,		// Ability to swim (water).
	SAMPLE_POLYFLAGS_DOOR		= 0x04,		// Ability to move through doors.
	SAMPLE_POLYFLAGS_JUMP		= 0x08,		// Ability to jump.
	SAMPLE_POLYFLAGS_DISABLED	= 0x10,		// Disabled polygon
	SAMPLE_POLYFLAGS_ALL		= 0xffff	// All abilities.
};


class myQueryFilter : public dtQueryFilter {

private:	
	float m_areaCost[DT_MAX_AREAS];		///< Cost per area type. (Used by default implementation.)
	unsigned short m_includeFlags;		///< Flags for polygons that can be visited. (Used by default implementation.)
	unsigned short m_excludeFlags;		///< Flags for polygons that should not be visted. (Used by default implementation.)
public:
	myQueryFilter();
	~myQueryFilter();

	bool isPassable(const float* pa, const float* pb) const;
	bool passFilter(const dtPolyRef /*ref*/,
							   const dtMeshTile* /*tile*/,
							   const dtPoly* poly) const;
	

    float getCost(const float* pa, const float* pb,
							 const dtPolyRef /*prevRef*/, const dtMeshTile* /*prevTile*/, const dtPoly* /*prevPoly*/,
							 const dtPolyRef /*curRef*/, const dtMeshTile* /*curTile*/, const dtPoly* curPoly,
							 const dtPolyRef /*nextRef*/, const dtMeshTile* /*nextTile*/, const dtPoly* /*nextPoly*/) const;
	

};



