#include <string.h>
#include <stdlib.h>

#define _USE_MATH_DEFINES
#include <math.h>
#include <stdio.h>
#include "DetourNavMesh.h"
#include "DetourNavMeshBuilder.h"
#include "DetourNavMeshQuery.h"
#define VERTEX_SIZE       3
#define INVALID_POLYREF   0
static const int NAVMESHSET_MAGIC = 'M'<<24 | 'S'<<16 | 'E'<<8 | 'T'; //'MSET';
static const int NAVMESHSET_VERSION = 1;

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

dtNavMesh* loadNavMesh() {
  dtNavMesh* navMesh;
  navMesh = loadAll("test2.bin");
  return navMesh;
}

dtNavMeshQuery* getQuery() {
  dtNavMesh* navMesh = loadNavMesh();
  dtNavMeshQuery* query = dtAllocNavMeshQuery();
  query->init(navMesh, 2048);
  return query;
}

float *findPath(float startx, float starty, float startz,
    float endx, float endy, float endz) {

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

  dtNavMeshQuery* query = getQuery();

  res = query->findNearestPoly(spos, polyPickExt, &filter, &startRef, 0);
  if (res == DT_SUCCESS) {
    if (startRef == 0) {
      fprintf (stderr, "start poly not found \n");
    } else {
      fprintf (stderr, "start poly found \n");
    }
  }
  res = query->findNearestPoly(epos, polyPickExt, &filter, &endRef, 0);
  if (res == DT_SUCCESS) {
    if (startRef == 0) {
      fprintf (stderr, "end poly not found \n");
    } else {
      fprintf (stderr, "end poly found \n");
    }
  }

  res = query->findPath(startRef, endRef, spos, epos, &filter, polys, &npolys, MAX_POLYS);
  if (res == DT_SUCCESS) {

    fprintf (stderr, "path found \n");
    query->findStraightPath(spos, epos, polys, npolys, straight, 0, 0, &straightPathCount, MAX_POLYS);

    fprintf (stderr, "straight paths found %d\n", straightPathCount);
    for (int i = 0; i < straightPathCount; ++i) {
      const float* v = &straight[i*3];
      fprintf (stderr, "%f.%f.%f\n", v[0], v[1], v[2]);
    }

  } else {
    fprintf (stderr, "path not found \n");
  }

  fprintf (stderr, "npolys %d\n", npolys);
  return straight;
}

int main (int argc, char* argv[]) {

findPath(10.0,0,10.0,109.0,0,109.0);





  return 1;
}

