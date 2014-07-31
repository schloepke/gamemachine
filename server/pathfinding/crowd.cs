

using UnityEngine;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;

namespace pathfinder
{
    public class Crowd
    {
        
        public int id;

        public Crowd (int id, int NavmeshId)
        {
            this.id = id;
            addCrowd (id,navmeshId);
        }

        public void UpdateTick(float step)
        {
            updateTick(id,step);
        }

        public int AddAgent(Vector3 position, float accel, float speed)
        {
            float[3] p;
            float[0] = position.z;
            float[1] = position.y;
            float[2] = position.x;
            return addAgent(id,p, accel, speed);
        }

        public void RemoveAgent(int agentIdx)
        {
            removeAgent(id,agentIdx)
        }

        public void SetMoveTarget(float position, bool adjust, int agentIdx = -1)
        {
            setMoveTarget(id,position,adjust,agentIdx);
        }

        public Vector3 GetAgentPosition(int agentIdx)
        {
            float[] resultPath = new float[3];
            getAgentPosition(id,agentIdx,resultPath);
            Vector3 position = new Vector3 (resultPath [2], resultPath [1], resultPath [0]);
            return position;
        }

        [DllImport("detour_path")]
        public static extern void updateTick (int id, float step);

        [DllImport("detour_path")]
        public static extern int addAgent (int id, [In] float[] position, float accel, float speed);

        [DllImport("detour_path")]
        public static extern void removeAgent (int id, int agentIdx);

        [DllImport("detour_path")]
        public static extern void setMoveTarget (int id, float position, bool adjust, int agentIdx);

        [DllImport("detour_path")]
        public static extern void getAgentPosition (int id, int agentIdx, [In, Out]  float[] resultPath);

        [DllImport("detour_path")]
        public static extern void addCrowd (int id, int navmeshId);

    }
}
