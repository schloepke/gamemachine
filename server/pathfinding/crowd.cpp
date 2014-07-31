#include "crowd.h"

Crowd::Crowd(int mi, Navmesh* n) : meshindex(mi)

{
	navmesh = n;
	crowd = dtAllocCrowd();
	
	nav = navmesh->getMesh();

	crowd->init(MAX_AGENTS, 0.6f, nav);

	// Make polygons with 'disabled' flag invalid.
	crowd->getEditableFilter(0)->setExcludeFlags(navmesh->SAMPLE_POLYFLAGS_DISABLED);

	// Setup local avoidance params to different qualities.
	dtObstacleAvoidanceParams params;
	// Use mostly default settings, copy from dtCrowd.
	memcpy(&params, crowd->getObstacleAvoidanceParams(0), sizeof(dtObstacleAvoidanceParams));

	// Low (11)
	params.velBias = 0.5f;
	params.adaptiveDivs = 5;
	params.adaptiveRings = 2;
	params.adaptiveDepth = 1;
	crowd->setObstacleAvoidanceParams(0, &params);

	// Medium (22)
	params.velBias = 0.5f;
	params.adaptiveDivs = 5; 
	params.adaptiveRings = 2;
	params.adaptiveDepth = 2;
	crowd->setObstacleAvoidanceParams(1, &params);

	// Good (45)
	params.velBias = 0.5f;
	params.adaptiveDivs = 7;
	params.adaptiveRings = 2;
	params.adaptiveDepth = 3;
	crowd->setObstacleAvoidanceParams(2, &params);

	// High (66)
	params.velBias = 0.5f;
	params.adaptiveDivs = 7;
	params.adaptiveRings = 3;
	params.adaptiveDepth = 3;

	crowd->setObstacleAvoidanceParams(3, &params);

	memset(m_trails, 0, sizeof(m_trails));

	m_vod = dtAllocObstacleAvoidanceDebugData();
	m_vod->init(2048);

	memset(&m_agentDebug, 0, sizeof(m_agentDebug));
	m_agentDebug.idx = -1;
	m_agentDebug.vod = m_vod;
	fprintf (stderr, "Crowd initialized\n");
}

void Crowd::addAgent(const float* p)
{
	dtCrowdAgentParams ap;
	memset(&ap, 0, sizeof(ap));
	ap.radius = 0.6f;
	ap.height = 2.0f;
	ap.maxAcceleration = 8.0f;
	ap.maxSpeed = 3.5f;
	ap.collisionQueryRange = ap.radius * 12.0f;
	ap.pathOptimizationRange = ap.radius * 30.0f;
	ap.updateFlags = 0; 
	ap.updateFlags |= DT_CROWD_ANTICIPATE_TURNS;
	ap.updateFlags |= DT_CROWD_OPTIMIZE_VIS;
	ap.updateFlags |= DT_CROWD_OPTIMIZE_TOPO;
	ap.updateFlags |= DT_CROWD_OBSTACLE_AVOIDANCE;
	ap.updateFlags |= DT_CROWD_SEPARATION;
	ap.obstacleAvoidanceType = (unsigned char)3.0f;
	ap.separationWeight = 2.0f;

	int idx = crowd->addAgent(p, &ap);
	if (idx != -1)
	{
		if (m_targetRef)
			crowd->requestMoveTarget(idx, m_targetRef, m_targetPos);

		// Init trail
		AgentTrail* trail = &m_trails[idx];
		for (int i = 0; i < AGENT_MAX_TRAIL; ++i)
			dtVcopy(&trail->trail[i*3], p);
		trail->htrail = 0;
	}
}

void Crowd::removeAgent(const int idx)
{
	crowd->removeAgent(idx);
}

static void calcVel(float* vel, const float* pos, const float* tgt, const float speed)
{
	dtVsub(vel, tgt, pos);
	vel[1] = 0.0;
	dtVnormalize(vel);
	dtVscale(vel, vel, speed);
}

void Crowd::setMoveTarget(const float* p, bool adjust, int agentIdx)
{

	// Find nearest point on navmesh and set move request to that location.
	dtNavMeshQuery* navquery = dtAllocNavMeshQuery();
  	navquery->init(nav, 4096);

	myQueryFilter filter;
	filter.setNavmesh(navmesh);
	filter.setIncludeFlags(navmesh->SAMPLE_POLYFLAGS_ALL ^ navmesh->SAMPLE_POLYFLAGS_DISABLED);
  	filter.setExcludeFlags(0);
	const float* ext = crowd->getQueryExtents();

	if (adjust)
	{
		float vel[3];
		// Request velocity
		if (agentIdx != -1)
		{
			const dtCrowdAgent* ag = crowd->getAgent(agentIdx);
			if (ag && ag->active)
			{
				calcVel(vel, ag->npos, p, ag->params.maxSpeed);
				crowd->requestMoveVelocity(agentIdx, vel);
			}
		}
		else
		{
			for (int i = 0; i < crowd->getAgentCount(); ++i)
			{
				const dtCrowdAgent* ag = crowd->getAgent(i);
				if (!ag->active) continue;
				calcVel(vel, ag->npos, p, ag->params.maxSpeed);
				crowd->requestMoveVelocity(i, vel);
			}
		}
	}
	else
	{
		fprintf (stderr, "one\n");
		navquery->findNearestPoly(p, ext, &filter, &m_targetRef, m_targetPos);
		fprintf (stderr, "two\n");

		if (agentIdx != -1)
		{
			const dtCrowdAgent* ag = crowd->getAgent(agentIdx);
			if (ag && ag->active)
				crowd->requestMoveTarget(agentIdx, m_targetRef, m_targetPos);
		}
		else
		{
			for (int i = 0; i < crowd->getAgentCount(); ++i)
			{
				const dtCrowdAgent* ag = crowd->getAgent(i);
				if (!ag->active) continue;
				crowd->requestMoveTarget(i, m_targetRef, m_targetPos);
			}
		}
	}
}

void Crowd::updateTick(const float dt)
{
	crowd->update(dt, &m_agentDebug);

	// Update agent trails
	for (int i = 0; i < crowd->getAgentCount(); ++i)
	{
		const dtCrowdAgent* ag = crowd->getAgent(i);
		AgentTrail* trail = &m_trails[i];
		if (!ag->active)
			continue;
		// Update agent movement trail.
		trail->htrail = (trail->htrail + 1) % AGENT_MAX_TRAIL;
		dtVcopy(&trail->trail[trail->htrail*3], ag->npos);
	}
}
