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

static dtNavMesh* meshes[1024];
static const int MAX_POLYS = 256;
static const int MAX_STEER_POINTS = 10;
static const int MAX_SMOOTH = 256;
static const float STEP_SIZE = 1.0f;
static const float SLOP = 0.01f;


extern "C" int loadNavMesh(int map, const char *file) {
  if (meshes[map] != 0) {
    return 0;
  }
  dtNavMesh* navMesh;
  navMesh = load_navmesh(file);
  meshes[map] = navMesh;
  return 1;
}

extern "C" dtNavMeshQuery* getQuery(int map) {
  if (meshes[map] == 0) {
    return 0;
  }

  dtNavMeshQuery* query = dtAllocNavMeshQuery();
  query->init(meshes[map], 4096);
  return query;
}

extern "C" void freeQuery(dtNavMeshQuery* query) {
  dtFreeNavMeshQuery(query);
}


extern "C" int findPath(dtNavMeshQuery* query, float startx, float starty,
    float startz, float endx, float endy, float endz, int find_straight_path,
    float* resultPath) {

  if (query == NULL) {
    return P_FAILURE;
  }

  float m_spos[3] = {startx,starty,startz};
  float m_epos[3] = {endx,endy,endz};


  dtPolyRef m_polys[MAX_POLYS];

  dtQueryFilter m_filter;
  float straight[MAX_POLYS*3];
  int straightPathCount = 0;
  float polyPickExt[3] = {40,40,40};
  int m_npolys = 0;

  float m_steerPoints[MAX_STEER_POINTS*3];
  int m_steerPointCount;
  float m_smoothPath[MAX_SMOOTH*3];
  int m_nsmoothPath = 0;

  m_filter.setIncludeFlags(SAMPLE_POLYFLAGS_ALL ^ SAMPLE_POLYFLAGS_DISABLED);
  m_filter.setExcludeFlags(0);

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


  if (find_straight_path == 0) {
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


  query->findStraightPath(m_spos, m_epos, m_polys, m_npolys, straight, 0, 0, &straightPathCount, MAX_POLYS);
  memcpy(resultPath, straight, sizeof(float)*3*straightPathCount);
  return straightPathCount;
}

extern "C" float* getPathPtr(int max_paths) {
  float *path;
  path = new float[max_paths*3];
  return path;
}

extern "C" void freePath(float* path) {
  delete [] path;
}

int main (int argc, char* argv[]) {

  int find_straight_path = 1;
  float *newPath;
  newPath = getPathPtr(MAX_SMOOTH);

  //const char *file = "/home2/chris/game_machine/server/detour/meshes/terrain.bin";
  const char *file = "/home2/chris/game_machine/server/detour/meshes/test.bin";


  int loadRes = loadNavMesh(1,file);
  fprintf (stderr, "loadNavMesh returned %d\n", loadRes);
  dtNavMeshQuery* query = getQuery(1);
  
  if (loadRes == 1) {
    for (int i = 0; i < 1; ++i) {
      //int res = findPath(query, 501.0, 0.2, 526.0, 528.0, 0.2, 509.0,
      //int res = findPath(query, 526.0, 0.2, 501.0, 509.0, 0.2, 528.0,
      int res = findPath(query, 500.0, 0.2, 526.0, 528.0, 0.2, 511.0,
          find_straight_path, newPath);
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

