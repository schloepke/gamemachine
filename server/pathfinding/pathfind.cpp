#include "pathfind.h"

extern "C" EXPORT_API int loadNavMesh(int map, const char *file) {
  if (meshes[map] != 0) {
    return 0;
  }
  dtNavMesh* navMesh;
  navMesh = load_navmesh(file);
  meshes[map] = navMesh;
  return 1;
}

extern "C" EXPORT_API dtNavMeshQuery* getQuery(int map) {
  if (meshes[map] == 0) {
    fprintf (stderr, "Unable to load navmesh\n");
    return 0;
  }

  dtNavMeshQuery* query = dtAllocNavMeshQuery();
  query->init(meshes[map], 4096);
  return query;
}

extern "C" void EXPORT_API freeQuery(dtNavMeshQuery* query) {
  dtFreeNavMeshQuery(query);
}

static int fixupShortcuts(dtPolyRef* path, int npath, dtNavMeshQuery* navQuery)
{
  if (npath < 3)
    return npath;

  // Get connected polygons
  static const int maxNeis = 16;
  dtPolyRef neis[maxNeis];
  int nneis = 0;

  const dtMeshTile* tile = 0;
  const dtPoly* poly = 0;
  if (dtStatusFailed(navQuery->getAttachedNavMesh()->getTileAndPolyByRef(path[0], &tile, &poly)))
    return npath;
  
  for (unsigned int k = poly->firstLink; k != DT_NULL_LINK; k = tile->links[k].next)
  {
    const dtLink* link = &tile->links[k];
    if (link->ref != 0)
    {
      if (nneis < maxNeis)
        neis[nneis++] = link->ref;
    }
  }

  // If any of the neighbour polygons is within the next few polygons
  // in the path, short cut to that polygon directly.
  static const int maxLookAhead = 6;
  int cut = 0;
  for (int i = dtMin(maxLookAhead, npath) - 1; i > 1 && cut == 0; i--) {
    for (int j = 0; j < nneis; j++)
    {
      if (path[i] == neis[j]) {
        cut = i;
        break;
      }
    }
  }
  if (cut > 1)
  {
    int offset = cut-1;
    npath -= offset;
    for (int i = 1; i < npath; i++)
      path[i] = path[i+offset];
  }

  return npath;
}

extern "C" EXPORT_API int findPath(dtNavMeshQuery* query, float startx, float starty,
    float startz, float endx, float endy, float endz, int find_straight_path,
    float* resultPath) {

  if (query == NULL) {
    return P_QUERY_NOT_FOUND;
  }

  float m_spos[3] = {startx,starty,startz};
  float m_epos[3] = {endx,endy,endz};


  dtPolyRef m_polys[MAX_POLYS];

  dtQueryFilter m_filter;
  float straight[MAX_POLYS*3];
  int straightPathCount = 0;
  float polyPickExt[3] = {2,4,2};
  int m_npolys = 0;

  float m_steerPoints[MAX_STEER_POINTS*3];
  int m_steerPointCount;
  float m_smoothPath[MAX_SMOOTH*3];
  int m_nsmoothPath = 0;

  m_filter.setIncludeFlags(SAMPLE_POLYFLAGS_ALL ^ SAMPLE_POLYFLAGS_DISABLED);
  m_filter.setExcludeFlags(0);

  dtPolyRef m_startRef, m_endRef;

  dtStatus res;

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
    query->closestPointOnPoly(m_startRef, m_spos, iterPos,0);
    query->closestPointOnPoly(polys[npolys-1], m_epos, targetPos,0);

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
      npolys = fixupShortcuts(polys, npolys, query);
      
      float h = 0;
      query->getPolyHeight(polys[0], result, &h);
      result[1] = h;
      dtVcopy(iterPos, result);

      if (endOfPath && inRange(iterPos, steerPos, SLOP, 1.0f)) {
        // Reached end of path.
        fprintf (stderr, "End of path reached\n");
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

extern "C" EXPORT_API float* getPathPtr(int max_paths) {
  float *path;
  path = new float[max_paths*3];
  return path;
}

extern "C" EXPORT_API void freePath(float* path) {
  delete [] path;
}


