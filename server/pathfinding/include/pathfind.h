#define _USE_MATH_DEFINES
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Recast.h"
#include "DetourCommon.h"
#include "DetourNavMesh.h"
#include "DetourNavMeshBuilder.h"
#include "DetourNavMeshQuery.h"
#include "mesh_loader.h"

#if _MSC_VER    // TRUE for Microsoft compiler.
#define EXPORT_API __declspec(dllexport) // Required for VC++
#else
#define EXPORT_API // Otherwise don't define.
#endif

#define VERTEX_SIZE       3
#define INVALID_POLYREF   0

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



