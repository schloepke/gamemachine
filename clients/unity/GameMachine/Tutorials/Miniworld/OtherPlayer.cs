using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using KAM3RA;

namespace GameMachine.Tutorials.Miniworld
{
	public class OtherPlayer : NpcController
	{
		private Vector3 targetLocation ;
		private Vector3 currentTarget;
		private Vector3 currentDirection;
		private int action;
		private float jumpSpeed = 4.0f;
		private float gravity = 20.0f;
		private bool jumping = false;

		public float lastUpdate;
		private CharacterController controller;
		private Terrain terrain;

		protected override void Start ()
		{
			terrain = Terrain.activeTerrain;
			lastUpdate = Time.time;
			base.Start ();
			SetNameTag (name);
			controller = GetComponent<CharacterController> ();
			this.type = Type.Ground;
		}
		
		public Vector3 SpawnLocation (Vector3 vector)
		{
			terrain = Terrain.activeTerrain;
			float height = terrain.SampleHeight (vector);
			Vector3 spawnPoint = new Vector3 (vector.x, (height + 0.05f), vector.z);
			return spawnPoint;
		}
		
		protected override void Update ()
		{
			if (player) {
				return;
			}
			
			if (currentTarget != null) {
				MovePlayer ();
			}
			if (State == "Idle") {
				return;
			}
		}
				
		public void UpdatePlayer (Vector3 target, Vector3 direction, float speed, int action)
		{
			this.action = action;
			currentTarget = target;
			currentDirection = direction;
			lastUpdate = Time.time;
		}

		public void MovePlayer ()
		{
			Quaternion qTo = Quaternion.LookRotation (currentDirection);
			transform.rotation = Quaternion.Slerp (transform.rotation, qTo, 4f * Time.deltaTime);

			float distance = Vector3.Distance (KAM3RA.User.VectorXZ (transform.position), KAM3RA.User.VectorXZ (currentTarget));
			speed = 4.0f;
			SetAnimation (distance, speed);
			if (distance >= 0.3f) {
				Vector3 targetDir = currentTarget - transform.position;

				if (controller.isGrounded) {
					if (jumping) {
						jumping = false;
					}
					if (action == 1) {
						jumping = true;

					}
				}

				if (jumping) {
					targetDir.y += 0.8f * Time.deltaTime;
				}
				controller.Move (targetDir * 0.8f * speed * Time.deltaTime);

				targetDir.y -= gravity * Time.deltaTime;
			}
		}

		private void SetAnimation (float distance, float speed)
		{
			if (jumping) {
				State = "Jump";
				return;
			}

			if (distance >= 0.3f) {
				if (speed > 2.0f) {
					if (animation ["Run"] || animation ["run"]) {
						State = "RunForward";
					} else {
						State = "WalkForward";
					}
				} else {
					State = "WalkForward";
				}
			} else {
				State = "Idle";
			}
		}

		public void UpdateTarget (Vector3 target)
		{
			currentTarget = target;
			lastUpdate = Time.time;
		}

	}
}