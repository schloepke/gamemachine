#include "DetourNavMesh.h"

static const int NAVMESHSET_MAGIC = 'M'<<24 | 'S'<<16 | 'E'<<8 | 'T'; //'MSET';
static const int NAVMESHSET_VERSION = 1;

struct NavMeshSetRcnHeader
{
  long version;
  int numTiles;
  dtNavMeshParams params;
};
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

void save_navmesh(const char* path, const dtNavMesh* mesh);
dtNavMesh* load_navmesh(const char* path);

