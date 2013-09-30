/*
 * @summary: recast loader for unity3d NavMesh.asset
 * @date: 2013-08-08
 * @author: zl
 */

#ifndef UNITYTILEMESH_H_
#define UNITYTILEMESH_H_
#include "DetourAlloc.h"
#include "DetourAssert.h"
#include "DetourCommon.h"
#include "DetourNavMesh.h"
#include "DetourNavMeshBuilder.h"
#include "DetourNavMeshQuery.h"
#include "DetourNode.h"
#include "DetourStatus.h"

dtNavMesh* dtLoadUnityTileMesh(const char* path);


#endif // UNITYTILEMESH_H_
