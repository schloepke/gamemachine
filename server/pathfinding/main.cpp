#if _MSC_VER
#else
#include "pathfinder.h"
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
  const char *file = "/home/chris/game_machine/server/pathfinding/all_tiles_navmesh.bin";
  Navmesh* navmesh = new Navmesh(1,file);

  int find_straight_path = 1;
  float *newPath;
  newPath = navmesh->getPathPtr(Navmesh::MAX_SMOOTH);
  timestamp_t t0;
  timestamp_t t1;
  double secs;

  t0 = get_timestamp();
  for (int j = 0; j < 15; ++j) {
    navmesh->gmapSetPassable(200,200,3);
    navmesh->gmapSetBlocked(130,95,3);
  }
  t1 = get_timestamp();
  secs = (t1 - t0) / 1000000.0L;
  fprintf (stderr, "setPassable %f\n", secs);

  if (navmesh->meshLoaded()) {
    for (int j = 0; j < 1; ++j) {
      for (int i = 0; i < 1; ++i) {
        t0 = get_timestamp();
        int res = navmesh->findPath(10.0f, 0.2f, 10.0f, 300.0f, 0.2f, 300.0f,
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
  navmesh->freePath(newPath);
  fprintf (stderr, "freePath\n");
  

  

  Crowd* crowd = new Crowd(navmesh);
  float delta = 1.01f;
  float agentPos[3] = {10.0f,0.0f,10.0f};
  float targetPos[3] = {300.0f,0.0f,300.0f};
  const float* ap = &agentPos[0];
  const float* tp = &targetPos[0];
  fprintf (stderr, "createCrowd\n");

  
  crowd->setMoveTarget(tp, false, -1);
  fprintf (stderr, "setMoveTarget\n");

  for (int j = 0; j < 100; ++j) {
    crowd->addAgent(ap, 8.0f, 3.5f);
  }
  
  fprintf (stderr, "addAgent\n");

  for (int j = 0; j < 900; ++j) {
    crowd->updateTick(delta);
    for (int i = 0; i < crowd->getCrowd()->getAgentCount(); ++i)
    {
      const dtCrowdAgent* ag = crowd->getCrowd()->getAgent(i);
      if (!ag->active)
        continue;
      const float* v = ag->npos;
      fprintf (stderr, "%d = %f %f %f\n", i, v[0], v[1], v[2]);
    }

  }
  fprintf (stderr, "updateTick\n");

  
}

#endif

int main (int argc, char* argv[]) {
  #if _MSC_VER
  #else
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
  
  #endif

  return 1;
}
