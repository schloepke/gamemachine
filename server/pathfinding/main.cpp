#include "pathfind.h"

void testnavmesh() {
  int find_straight_path = 1;
  float *newPath;
  newPath = getPathPtr(MAX_SMOOTH);

  //const char *file = "/home2/chris/game_machine/server/detour/meshes/terrain.bin";
  const char *file = "/home2/chris/game_machine/data/meshes/all_tiles_navmesh.bin";


  int loadRes = loadNavMesh(1,file);
  fprintf (stderr, "loadNavMesh returned %d\n", loadRes);
  dtNavMeshQuery* query = getQuery(1);

  if (loadRes == 1) {
    for (int j = 0; j < 1; ++j) {
      for (int i = 0; i < 1; ++i) {
        //int res = findPath(query, 563.0, 0.2, 504.0, 509.0, 0.2, 528.0,
        int res = findPath(query, 563.0f, 0.2f, 504.0f, 563.0f, 0.2f, 541.0f,
            find_straight_path, newPath);
        fprintf (stderr, "findPath returned %d\n", res);
        for (int i = 0; i < res; ++i) {
          //fprintf (stderr, "%d\n", i);
          const float* v = &newPath[i*3];
          fprintf (stderr, "%f.%f.%f\n", v[0], v[1], v[2]);
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

  
  return 1;
}
