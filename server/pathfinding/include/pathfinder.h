
#include "crowd.h"

extern "C" EXPORT_API void addCrowd(int id, int navmeshid);
extern "C" EXPORT_API int addAgent(int crowdId, const float* p);
extern "C" EXPORT_API void removeAgent(int crowdId, const int idx);
extern "C" EXPORT_API void setMoveTarget(int crowdId, const float* p, bool adjust, int agentIdx);
extern "C" EXPORT_API void updateTick(int crowdId, const float dt);
extern "C" EXPORT_API void getAgentPosition(int crowdId, int idx, float* resultPath);

extern "C" EXPORT_API void addNavmesh(int id, const char *file);
extern "C" EXPORT_API void gmapSetPassable(int navmeshId, int a, int b, int radius);
extern "C" EXPORT_API void gmapSetBlocked(int navmeshId, int a, int b, int radius);
extern "C" EXPORT_API int findPath(int navmeshId, float startx, float starty,
 float startz, float endx, float endy, float endz, int find_straight_path, float* resultPath);