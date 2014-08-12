

using UnityEngine;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;

namespace GameMachine.Pathfinding
{
    public class Crowd
    {
        
        public int id;
        
        public Crowd (int id, int navmeshId)
        {
            this.id = id;
            addCrowd (id, navmeshId);
        }
        
        public void UpdateTick (float step)
        {
            updateTick (id, step);
        }

        public int AddAgent (Vector3 position, float accel, float speed, float radius, float height, int optflag, float separationWeight)
        {
            float[] p = new float[3];
            p [0] = position.z;
            p [1] = position.y;
            p [2] = position.x;
            return addAgent (id, p, accel, speed, radius, height, optflag, separationWeight);
        }
        
        public void RemoveAgent (int agentIdx)
        {
            removeAgent (id, agentIdx);
        }
        
        public void SetMoveTarget (Vector3 position, bool adjust, int agentIdx = -1)
        {
            float[] p = new float[3];
            p [0] = position.z;
            p [1] = position.y;
            p [2] = position.x;
            setMoveTarget (id, p, adjust, agentIdx);
        }
        
        public Vector3 GetAgentPosition (int agentIdx)
        {
            float[] resultPath = new float[3];
            getAgentPosition (id, agentIdx, resultPath);
            Vector3 position = new Vector3 (resultPath [2], resultPath [1], resultPath [0]);
            return position;
        }
        
        [DllImport("detour_path")]
        public static extern void updateTick (int id, float step);
        
        [DllImport("detour_path")]
        public static extern int addAgent (int id, [In] float[] position, float accel, float speed,
                                           float radius, float height, int optflag, float separationWeight);
        
        [DllImport("detour_path")]
        public static extern void removeAgent (int id, int agentIdx);
        
        [DllImport("detour_path")]
        public static extern void setMoveTarget (int id, [In] float[] position, bool adjust, int agentIdx);
        
        [DllImport("detour_path")]
        public static extern void getAgentPosition (int id, int agentIdx, [In, Out]  float[] resultPath);
        
        [DllImport("detour_path")]
        public static extern void addCrowd (int id, int navmeshId);
        
    }
}
