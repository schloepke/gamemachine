#define _USE_MATH_DEFINES
#include <ctype.h>
#include <stdio.h>
#include <memory.h>
#include <math.h>

#include <vector>
#include <iostream>


#include "common.h"
#include "navmesh.h"

#include <math.h>
#include <stdio.h>
#include "Recast.h"
#include "DetourNavMesh.h"
#include "DetourNavMeshQuery.h"
#include "DetourCrowd.h"


extern "C" EXPORT_API void addAgent(const float* p);
extern "C" EXPORT_API void removeAgent(const int idx);
extern "C" EXPORT_API void setMoveTarget(const float* p, bool adjust, int agentIdx);
extern "C" EXPORT_API void updateTick(const float dt);

class Crowd {
	int meshindex;
	Navmesh* navmesh;
	dtNavMesh* nav;
	dtCrowd* crowd;
	dtCrowdAgentDebugInfo m_agentDebug;
	dtObstacleAvoidanceDebugData* m_vod;
	float m_targetPos[3];
	dtPolyRef m_targetRef;

	static const int AGENT_MAX_TRAIL = 64;
	static const int MAX_AGENTS = 128;
	struct AgentTrail
	{
		float trail[AGENT_MAX_TRAIL*3];
		int htrail;
	};
	AgentTrail m_trails[MAX_AGENTS];

public:
	Crowd(int mi, Navmesh* n);
	void addAgent(const float* p);
	void removeAgent(const int idx);
	void setMoveTarget(const float* p, bool adjust, int agentIdx);
	void updateTick(const float dt);
};