#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#define _USE_MATH_DEFINES
#include <math.h>
#include "Recast.h"
#include "DetourCommon.h"
#include "DetourNavMesh.h"
#include "DetourNavMeshBuilder.h"
#include "DetourNavMeshQuery.h"
#include "pathfind.h"

#define VERTEX_SIZE       3
#define INVALID_POLYREF   0

static const int P_FAILURE = -1;
static const int P_NO_START_POLY = -2;
static const int P_NO_END_POLY = -3;
static const int P_PATH_NOT_FOUND = -4;
static const int NAVMESHSET_MAGIC = 'M'<<24 | 'S'<<16 | 'E'<<8 | 'T'; //'MSET';
static const int NAVMESHSET_VERSION = 1;

static dtNavMesh* meshes[1024];
static const int MAX_POLYS = 256;

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

extern "C" int loadNavMesh(int map, const char *file) {
  if (meshes[map] != 0) {
    return 0;
  }
  dtNavMesh* navMesh;
  navMesh = loadAll(file);
  meshes[map] = navMesh;
  return 1;
}

extern "C" dtNavMeshQuery* getQuery(int map) {
  if (meshes[map] == 0) {
    return 0;
  }

  //dtNavMesh* navMesh = meshes[map];
  dtNavMeshQuery* query = dtAllocNavMeshQuery();
  query->init(meshes[map], 4096);
  return query;
}

extern "C" void freeQuery(dtNavMeshQuery* query) {
  dtFreeNavMeshQuery(query);
}

extern "C" int findPath(dtNavMeshQuery* query, float startx, float starty, float startz,
    float endx, float endy, float endz, float* resultPath) {

  float m_spos[3] = {startx,starty,startz};
  float m_epos[3] = {endx,endy,endz};

  static const int MAX_STEER_POINTS = 10;
  static const int MAX_SMOOTH = 2048;
  static const float STEP_SIZE = 0.5f;
  static const float SLOP = 0.01f;

  dtPolyRef m_polys[MAX_POLYS];

  dtQueryFilter m_filter;
  float straight[MAX_POLYS*3];
  int straightPathCount = 0;
  float polyPickExt[3] = {20,40,20};
  int includeFlags = 0x3;
  int excludeFlags = 0x0;
  int m_npolys = 0;

  float m_steerPoints[MAX_STEER_POINTS*3];
  int m_steerPointCount;
  float m_smoothPath[MAX_SMOOTH*3];
  int m_nsmoothPath = 0;

  m_filter.setIncludeFlags((unsigned short)includeFlags);
  m_filter.setExcludeFlags((unsigned short)excludeFlags);

  dtPolyRef m_startRef, m_endRef;

  dtStatus res;

  if (query == 0) {
    return P_FAILURE;
  }

  res = query->findNearestPoly(m_spos, polyPickExt, &m_filter, &m_startRef, 0);
  if (res == DT_SUCCESS) {
    if (m_startRef == 0) {
      return P_NO_START_POLY;
    }
  } else {
    return P_NO_START_POLY;
  }

  res = query->findNearestPoly(m_epos, polyPickExt, &m_filter, &m_endRef, 0);
  if (res == DT_SUCCESS) {
    if (m_endRef == 0) {
      return P_NO_END_POLY;
    }
  } else {
    return P_NO_END_POLY;
  }

  res = query->findPath(m_startRef, m_endRef, m_spos, m_epos, &m_filter, m_polys, &m_npolys, MAX_POLYS);
  if (res != DT_SUCCESS) {
    return P_PATH_NOT_FOUND;
  }


  if (m_polys) {
    dtPolyRef polys[MAX_POLYS];
    memcpy(polys, m_polys, sizeof(dtPolyRef)*m_npolys); 
    int npolys = m_npolys;

    float iterPos[3], targetPos[3];
    query->closestPointOnPoly(m_startRef, m_spos, iterPos);
    query->closestPointOnPoly(polys[npolys-1], m_epos, targetPos);

    m_nsmoothPath = 0;
    
    dtVcopy(&m_smoothPath[m_nsmoothPath*3], iterPos);
    m_nsmoothPath++;

    while (npolys && m_nsmoothPath < MAX_SMOOTH) {
      // Find location to steer towards.
      float steerPos[3];
      unsigned char steerPosFlag;
      dtPolyRef steerPosRef;

      if (!getSteerTarget(query, iterPos, targetPos, SLOP, polys, npolys, steerPos, steerPosFlag, steerPosRef)) {
        break;
      }

      bool endOfPath = (steerPosFlag & DT_STRAIGHTPATH_END) ? true : false;
      bool offMeshConnection = (steerPosFlag & DT_STRAIGHTPATH_OFFMESH_CONNECTION) ? true : false;

      // Find movement delta.
      float delta[3], len;
      dtVsub(delta, steerPos, iterPos);
      len = dtSqrt(dtVdot(delta,delta));
      // If the steer target is end of path or off-mesh link, do not move past the location.
      if ((endOfPath || offMeshConnection) && len < STEP_SIZE) {
        len = 1;
      } else {
        len = STEP_SIZE / len;
      }

      float moveTgt[3];
      dtVmad(moveTgt, iterPos, delta, len);

      // Move
      float result[3];
      dtPolyRef visited[16];
      int nvisited = 0;
      query->moveAlongSurface(polys[0], iterPos, moveTgt, &m_filter, result, visited, &nvisited, 16);

      npolys = fixupCorridor(polys, npolys, MAX_POLYS, visited, nvisited);
      float h = 0;
      query->getPolyHeight(polys[0], result, &h);
      result[1] = h;
      dtVcopy(iterPos, result);

      if (endOfPath && inRange(iterPos, steerPos, SLOP, 1.0f)) {
        // Reached end of path.
        dtVcopy(iterPos, targetPos);
        if (m_nsmoothPath < MAX_SMOOTH) {
          dtVcopy(&m_smoothPath[m_nsmoothPath*3], iterPos);
          m_nsmoothPath++;
        }
        break;
      }

      if (m_nsmoothPath < MAX_SMOOTH) {
        dtVcopy(&m_smoothPath[m_nsmoothPath*3], iterPos);
        m_nsmoothPath++;
      }
    }

    memcpy(resultPath, m_smoothPath, sizeof(float)*3*m_nsmoothPath);
    return m_nsmoothPath;
  }


  //query->findStraightPath(m_spos, m_epos, m_polys, m_npolys, straight, 0, 0, &straightPathCount, MAX_POLYS);
  //memcpy(resultPath, straight, sizeof(float)*3*straightPathCount);
  //return straightPathCount;
}

extern "C" float* getPathPtr() {
  float *path;
  path = new float[2048*3];
  return path;
}

extern "C" void freePath(float* path) {
  delete [] path;
}

extern "C" void testStruct() {

}

int main (int argc, char* argv[]) {
  float *newPath;
  newPath = getPathPtr();

  const char *file = "/home2/chris/game_machine/server/detour/test2.bin";

  int loadRes = loadNavMesh(1,file);
  fprintf (stderr, "loadNavMesh returned %d\n", loadRes);
  dtNavMeshQuery* query = getQuery(1);

  if (loadRes == 1) {
    for (int i = 0; i < 1; ++i) {
      int res = findPath(query, 10.0, 0.0, 10.0, 109.0, 0.0, 109.0, newPath);
      fprintf (stderr, "findPath returned %d\n", res);
      for (int i = 0; i < res; ++i) {
        //fprintf (stderr, "%d\n", i);
        const float* v = &newPath[i*3];
        fprintf (stderr, "%f.%f.%f\n", v[0], v[1], v[2]);
      }
    }
  }

  fprintf (stderr, "endLoop\n");
  freePath(newPath);
  fprintf (stderr, "freePath\n");
  freeQuery(query);
  fprintf (stderr, "freeQuery\n");
  return 1;
}

