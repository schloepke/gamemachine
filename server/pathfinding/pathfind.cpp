#include "pathfind.h"


inline bool inRange(const float* v1, const float* v2, const float r, const float h)
{
  const float dx = v2[0] - v1[0];
  const float dy = v2[1] - v1[1];
  const float dz = v2[2] - v1[2];
  return (dx*dx + dz*dz) < r*r && fabsf(dy) < h;
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

static bool getSteerTarget(dtNavMeshQuery* navQuery, const float* startPos, const float* endPos,
               const float minTargetDist,
               const dtPolyRef* path, const int pathSize,
               float* steerPos, unsigned char& steerPosFlag, dtPolyRef& steerPosRef,
               float* outPoints = 0, int* outPointCount = 0)               
{
  // Find steer target.
  static const int MAX_STEER_POINTS = 3;
  float steerPath[MAX_STEER_POINTS*3];
  unsigned char steerPathFlags[MAX_STEER_POINTS];
  dtPolyRef steerPathPolys[MAX_STEER_POINTS];
  int nsteerPath = 0;
  navQuery->findStraightPath(startPos, endPos, path, pathSize,
                 steerPath, steerPathFlags, steerPathPolys, &nsteerPath, MAX_STEER_POINTS);
  if (!nsteerPath)
    return false;

  if (outPoints && outPointCount)
  {
    *outPointCount = nsteerPath;
    for (int i = 0; i < nsteerPath; ++i)
      dtVcopy(&outPoints[i*3], &steerPath[i*3]);
  }


  // Find vertex far enough to steer to.
  int ns = 0;
  while (ns < nsteerPath)
  {
    // Stop at Off-Mesh link or when point is further than slop away.
    if ((steerPathFlags[ns] & DT_STRAIGHTPATH_OFFMESH_CONNECTION) ||
      !inRange(&steerPath[ns*3], startPos, minTargetDist, 1000.0f))
      break;
    ns++;
  }
  // Failed to find good point to steer to.
  if (ns >= nsteerPath)
    return false;

  dtVcopy(steerPos, &steerPath[ns*3]);
  steerPos[1] = startPos[1];
  steerPosFlag = steerPathFlags[ns];
  steerPosRef = steerPathPolys[ns];

  return true;
}

static int fixupCorridor(dtPolyRef* path, const int npath, const int maxPath,
             const dtPolyRef* visited, const int nvisited)
{
  int furthestPath = -1;
  int furthestVisited = -1;

  // Find furthest common polygon.
  for (int i = npath-1; i >= 0; --i)
  {
    bool found = false;
    for (int j = nvisited-1; j >= 0; --j)
    {
      if (path[i] == visited[j])
      {
        furthestPath = i;
        furthestVisited = j;
        found = true;
      }
    }
    if (found)
      break;
  }

  // If no intersection found just return current path. 
  if (furthestPath == -1 || furthestVisited == -1)
    return npath;

  // Concatenate paths. 

  // Adjust beginning of the buffer to include the visited.
  const int req = nvisited - furthestVisited;
  const int orig = rcMin(furthestPath+1, npath);
  int size = rcMax(0, npath-orig);
  if (req+size > maxPath)
    size = maxPath-req;
  if (size)
    memmove(path+req, path+orig, size*sizeof(dtPolyRef));

  // Store visited
  for (int i = 0; i < req; ++i)
    path[i] = visited[(nvisited-1)-i];        

  return req+size;
}

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


