//////////////////////////////////////////////////////////////
//  KAM3RA Third-Person Camera System
// 	Copyright © 2013 Regress Software
//////////////////////////////////////////////////////////////

using UnityEngine;
using System.Collections;
using KAM3RA;

public class Prop : MonoBehaviour 
{
	float drag = 10f;
	void Start()
	{
		if (rigidbody != null && collider != null)
		{
			rigidbody.mass = User.CalculateMass(collider.bounds.extents.magnitude * 2f);
			rigidbody.drag = 0f;
			if (collider is SphereCollider)
			{
				rigidbody.angularDrag 	= 1f;
				drag 					= 1f;
				rigidbody.isKinematic   = true;
				collider.isTrigger 		= true;
			}
			else 
			{
				rigidbody.angularDrag 	= 5f;
				drag 					= 10f;
			}
		}
	}
	void OnTriggerEnter(Collider other)
	{
		rigidbody.isKinematic = false;
		collider.isTrigger    = false;
		rigidbody.AddRelativeForce(new Vector3(0, 10, 0));
	}
	void OnCollisionEnter()
	{
		rigidbody.drag = drag;
	}
	void OnCollisionExit()
	{
		rigidbody.drag = 0f;
	}
}
