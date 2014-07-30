#include "pathfind.h"
#include "astar.h"
#include <cstdlib>
#include <stdio.h>
#include <float.h>
#include <time.h>
#include <stdint.h>

#include <sys/time.h>
#include <ctime>
using namespace std;

 typedef unsigned long long timestamp_t;

    static timestamp_t  get_timestamp ()
    {
      struct timeval now;
      gettimeofday (&now, NULL);
      return  now.tv_usec + (timestamp_t)now.tv_sec * 1000000;
    }


    

void testnavmesh() {
  int find_straight_path = 1;
  float *newPath;
  newPath = getPathPtr(MAX_SMOOTH);
  timestamp_t t0;
  timestamp_t t1;
  double secs;

  t0 = get_timestamp();
  for (int j = 0; j < 15; ++j) {
    gmapSetPassable(200,200,3);
    gmapSetBlocked(130,95,3);
  }
  t1 = get_timestamp();
  secs = (t1 - t0) / 1000000.0L;
  fprintf (stderr, "setPassable %f\n", secs);

  //const char *file = "/home2/chris/game_machine/server/detour/meshes/terrain.bin";
  const char *file = "/home/chris/game_machine/server/pathfinding/all_tiles_navmesh.bin";


  int loadRes = loadNavMesh(1,file);
  fprintf (stderr, "loadNavMesh returned %d\n", loadRes);
  dtNavMeshQuery* query = getQuery(1);
  
  if (loadRes == 1) {
    for (int j = 0; j < 1; ++j) {
      for (int i = 0; i < 1; ++i) {
        t0 = get_timestamp();
        int res = findPath(query, 10.0f, 0.2f, 10.0f, 300.0f, 0.2f, 300.0f,
            find_straight_path, newPath);
        t1 = get_timestamp();
        secs = (t1 - t0) / 1000000.0L;
        fprintf (stderr, "end time %f\n", secs);

        fprintf (stderr, "findPath returned %d\n", res);
        for (int i = 0; i < res; ++i) {
          //fprintf (stderr, "%d\n", i);
          const float* v = &newPath[i*3];
          fprintf (stderr, "%f %f %f\n", v[0], v[1], v[2]);
        }
      }
    }
  }
  
  

  fprintf (stderr, "endLoop\n");
  freePath(newPath);
  fprintf (stderr, "freePath\n");
  freeQuery(query);
  fprintf (stderr, "freeQuery\n");
}

int main (int argc, char* argv[]) {
  testnavmesh();
  return 1;

  addPathfinder(1,500,500);
  for (int j = 0; j < 500; ++j) {
      for (int i = 0; i < 500; ++i) {
        //setPassable(1,j,i);
      }
  }
fprintf (stderr, "grid created\n");

  int res = findpath2d(1,0,0,100,100);
  fprintf (stderr, "findpath2d returned %d\n", res);
  return 1;
}
