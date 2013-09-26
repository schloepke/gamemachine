#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#define _USE_MATH_DEFINES
#include <math.h>
#include "DetourNavMesh.h"
#include "DetourNavMeshBuilder.h"
#include "DetourNavMeshQuery.h"

#define VERTEX_SIZE       3
#define INVALID_POLYREF   0

extern "C" { 
  int findPath(int map,float startx, float starty, float startz, float endx, float endy, float endz, float* path);
  int loadNavMesh(int idx, const char *file);
}

static const int P_FAILURE = -1;
static const int P_NO_START_POLY = -2;
static const int P_NO_END_POLY = -3;
static const int P_PATH_NOT_FOUND = -4;
static const int NAVMESHSET_MAGIC = 'M'<<24 | 'S'<<16 | 'E'<<8 | 'T'; //'MSET';
static const int NAVMESHSET_VERSION = 1;

static dtNavMesh* meshes[1024];

struct NavMeshSetHeader
{
	int magic;
	int version;
	int numTiles;
	dtNavMeshParams params;
};

struct NavMeshTileHeader
{
	dtTileRef tileRef;
	int dataSize;
};

dtNavMesh* loadAll(const char* path)
{
	FILE* fp = fopen(path, "rb");
	if (!fp) return 0;

	// Read header.
	NavMeshSetHeader header;
	fread(&header, sizeof(NavMeshSetHeader), 1, fp);
	if (header.magic != NAVMESHSET_MAGIC)
	{
		fclose(fp);
		return 0;
	}
	if (header.version != NAVMESHSET_VERSION)
	{
		fclose(fp);
		return 0;
	}

	dtNavMesh* mesh = dtAllocNavMesh();
	if (!mesh)
	{
		fclose(fp);
		return 0;
	}
	dtStatus status = mesh->init(&header.params);
	if (dtStatusFailed(status))
	{
		fclose(fp);
		return 0;
	}

	// Read tiles.
	for (int i = 0; i < header.numTiles; ++i)
	{
		NavMeshTileHeader tileHeader;
		fread(&tileHeader, sizeof(tileHeader), 1, fp);
		if (!tileHeader.tileRef || !tileHeader.dataSize)
			break;

		unsigned char* data = (unsigned char*)dtAlloc(tileHeader.dataSize, DT_ALLOC_PERM);
		if (!data) break;
		memset(data, 0, tileHeader.dataSize);
		fread(data, tileHeader.dataSize, 1, fp);

   //fprintf (stderr, "Adding tile %s\n", data);
		mesh->addTile(data, tileHeader.dataSize, DT_TILE_FREE_DATA, tileHeader.tileRef, 0);
	}

	fclose(fp);

	return mesh;
}


int loadNavMesh(int map, const char *file) {
  if (meshes[map] != 0) {
    return 0;
  }
  dtNavMesh* navMesh;
  navMesh = loadAll(file);
  meshes[map] = navMesh;
  return 1;
}

dtNavMeshQuery* getQuery(int map) {
  if (meshes[map] == 0) {
    return 0;
  }

  dtNavMesh* navMesh = meshes[map];
  dtNavMeshQuery* query = dtAllocNavMeshQuery();
  query->init(navMesh, 2048);
  return query;
}


int findPath(int map, float startx, float starty, float startz,
    float endx, float endy, float endz, float* result) {

  float spos[3] = {startx,starty,startz};
  float epos[3] = {endx,endy,endz};

  static const int MAX_POLYS = 256;
  dtPolyRef polys[MAX_POLYS];
  float straight[MAX_POLYS*3];
  int straightPathCount = 0;
  const float polyPickExt[3] = {20,40,20};
  int includeFlags = 0x3;
  int excludeFlags = 0x0;
  int npolys = 0;
  dtQueryFilter filter;

  //filter.setIncludeFlags((unsigned short)includeFlags);
  //filter.setExcludeFlags((unsigned short)excludeFlags);

  dtPolyRef startRef, endRef;

  dtStatus res;

  dtNavMeshQuery* query = getQuery(map);
  if (query == 0) {
    return P_FAILURE;
  }

  res = query->findNearestPoly(spos, polyPickExt, &filter, &startRef, 0);
  if (res == DT_SUCCESS) {
    if (startRef == 0) {
      return P_NO_START_POLY;
    }
  } else {
    return P_NO_START_POLY;
  }

  res = query->findNearestPoly(epos, polyPickExt, &filter, &endRef, 0);
  if (res == DT_SUCCESS) {
    if (endRef == 0) {
      return P_NO_END_POLY;
    }
  } else {
    return P_NO_END_POLY;
  }

  res = query->findPath(startRef, endRef, spos, epos, &filter, polys, &npolys, MAX_POLYS);
  if (res != DT_SUCCESS) {
    return P_PATH_NOT_FOUND;
  }

  query->findStraightPath(spos, epos, polys, npolys, straight, 0, 0, &straightPathCount, MAX_POLYS);
  
  memcpy(result, straight, sizeof(float)*3*straightPathCount);
  return straightPathCount;
}

int main (int argc, char* argv[]) {
  float newPath[256*3];
  const char *file = "/home2/chris/game_machine/server/detour/test2.bin";

  int loadRes = loadNavMesh(1,file);
  fprintf (stderr, "loadNavMesh returned %d\n", loadRes);

  if (loadRes == 1) {
    for (int i = 0; i < 1; ++i) {
      int res = findPath(1, 10.0, 0.0, 10.0, 109.0, 0.0, 109.0, newPath);
      fprintf (stderr, "findPath returned %d\n", res);
      for (int i = 0; i < res; ++i) {
        const float* v = &newPath[i*3];
        fprintf (stderr, "%f.%f.%f\n", v[0], v[1], v[2]);
      }
    }
  }
  return 1;
}

