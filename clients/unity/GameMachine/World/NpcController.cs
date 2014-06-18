///////////////////////////////////////////////////////////////
//  KAM3RA Third-Person Camera System
//  Copyright © 2013 Regress Software
//////////////////////////////////////////////////////////////

using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine.World
{ 
    public class NpcController : MonoBehaviour
    {
        //////////////////////////////////////////////////////////////
        // Public Variables
        //////////////////////////////////////////////////////////////
        // actor type -- we do things slightly different depending on whether we're ground, fly or hover
        public Type type = Type.Ground;
        
        // true if attached to User
        public bool player = false;
        
        // rigidbody we will find or create
        new public Rigidbody rigidbody = null;
        
        // CapsuleCollider we will find or create
        new public CapsuleCollider collider = null;
        
        // main actor body, retrieved in InitRenderers()
        new public Renderer renderer = null;
        
        // main actor animation, retrieved in InitAnimation() -- this the first Animation component we can find
        new public Animation animation = null;
        
        // hide or show name tag
        public bool showNameTag = true;
        
        // name tag name
        public string nameTag = "NpcController";
        
        // name tag default color
        public Color nameTagColor = Color.green;
        
        // maximum angle we collide at and still keep moving 
        // NOTE this is unsigned -- use with altitudeScale (below) 
        // to know whether you're on an upward or downward slope
        public float maxSlope = 80f;
        
        // sets rigidbody.drag when not 1) Idling (velocity == 0, drag = idleDrag) or 2) In-air (drag = 0)
        public float drag = 10f;
        
        // how high the actor can jump 
        public float jumpHeight = 2f;
        
        // where the camera is looking through the actor, 0-1 is from feet (presumed to be the actor's local position) to top of the head
        public float eyeHeightScale = 0.9f;
        
        // maximum speed of the actor we're controlling
        public float maxSpeed = 4f;
        
        // accleration - ramp up to speed, expressed as a percentage (0-1) of maxSpeed
        public float acceleration = 1f;
        
        // momentum -- dead-stop is zero 
        public float momentum = 0f;
        
        // current collision state -- None, Grounded or Blocked
        public CollisionState collisionState = CollisionState.None;
        
        // current target, if any
        public NpcController target = null; 
        
        // animation mapper and current state output for actor info and animation
        public StateMap state = new StateMap();
        
        // 3D text mesh above the actor's name
        public TextMesh nameTagMesh = null;
        
        // 3D text mesh offset
        public Vector3 nameTagOffset = Vector3.zero;
        
        // collide radius
        public float radius = 0.3f;
        
        //////////////////////////////////////////////////////////////
        // Protected Variables
        //////////////////////////////////////////////////////////////      
        // list of all Renderers that are children of this gameObject -- we need all Renderers to toggle visiblity on/off
        protected List<Renderer> renderers = new List<Renderer>();
        
        // highest collision point for the current collision
        protected ContactPoint maxCollisionPoint = new ContactPoint();
        
        // lowest collision point for the current collision
        protected ContactPoint minCollisionPoint = new ContactPoint();
        
        // if we're currently colliding with something > maxSlope AND lower than us
        protected bool stuck = false;
        
        // per-frame velocity 
        protected Vector3 velocity = Vector3.zero;  
        
        // current non-scaled input speed
        protected float speed = 0f; 
        
        // whether or not to look at the target when there is one
        protected bool watchTarget = false;
        
        // tracks velocity.y after after a jump 
        protected float fallVelocity = 0f;  
        
        //if fallVelocity is below this veloocity, automatically go into a "fall" state
        protected float fallVelocityCutoff = -3f;   
        
        // sets rigidbody.drag when idle -- trumps drag to prevent "slippage" of the actor on slopes when idling
        protected float idleDrag = 100f;
        
        // current distance from last collision while "in the air"
        protected float relativeAltitude = 0f;
        
        // tracks how long we're in a non-collision state, applies when setting user input only
        protected float airTime = 0f;
        
        // time at which we're eligible to fly 
        protected float canFlyAtTime = 0.5f;
        
        // current y position change
        protected AltitudeState altitudeState = AltitudeState.Level;
        
        //////////////////////////////////////////////////////////////
        // Private Variables
        //////////////////////////////////////////////////////////////      
        // name tag start color -- save
        private Color nameTagStartColor = Color.green;
        
        // name tag material -- for editor only
        private Material nameTagMaterial = null;
        
        //////////////////////////////////////////////////////////////
        // Init 
        //////////////////////////////////////////////////////////////
        // not currently implemented by NpcController
        protected virtual void Awake()
        {
            // sub
        }
        // init functions as well as a check to see if we're staring out as the player
        protected virtual void Start()
        {
            // store name tag color
            nameTagStartColor = nameTagColor;
            
            // required
            if (!InitRenderers())
            {
                Debug.Log(name + "(NpcController) could not initialize Renderers.");
                return;
            }
           
            // optional
            if (!InitNameTag())
            {
                Debug.Log(name + "(NpcController) does not have TextMesh, OK...");
            }
            // optional
            if (!InitAnimation())
            {
                Debug.Log(name + "(NpcController) does not have Animation, OK...");
            }
            
           
        }
        // first, fill the renderers list with all renderers 
        // for hiding them when the camera is too close to the actor
        // next, find the main actor's Renderer -- we're using a simple
        // heuristic here which is  1) model has bones (SkinnedMeshRenderer), and
        //                          2) model has the greatest number of bones, or
        //                          3) if model has no bones, the largest Renderer present
        protected virtual bool InitRenderers()
        {
            renderers = new List<Renderer>(GetComponentsInChildren<Renderer>(true)); 
            if (renderers.Count > 0)
            {
                int maxBones = 0; // we're looking for the SkinnedMeshRenderer with the most bones
                foreach (Renderer m in renderers)
                {
                    if (m is SkinnedMeshRenderer)
                    {
                        int b = ((SkinnedMeshRenderer)m).bones.Length;
                        if (b > maxBones)
                        {
                            maxBones = b;
                            renderer = m;
                        }
                    }
                }
                // no SkinnedMeshRenderers, look for the largest renderer
                if (renderer == null)
                {
                    float h = 0;
                    foreach (Renderer m in renderers)
                    {
                        if (m.bounds.size.y > h)
                        {
                            h = m.bounds.size.y; 
                            renderer = m;
                        }
                    }
                }
                // this should never happen
                if (renderer == null)
                {
                    renderer = renderers [0];
                }
            }
            return (renderer != null);
        }   
        // find the first Animation we come to -- null is legal
        protected virtual bool InitAnimation()
        {
            animation = GetComponent<Animation>();
            if (animation == null)
            {
                foreach (Transform t in transform)
                {
                    animation = t.GetComponent<Animation>();
                    if (animation != null)
                        break;
                }
            }
            return (animation != null);
        }
        // setup Physics - Rigidbody and CapsuleCollider objects

        // name tag that floats above this object
        protected virtual bool InitNameTag()
        {
            nameTagMesh = GetComponentInChildren<TextMesh>();   
            if (nameTagMesh != null)
            {
                nameTagMesh.offsetZ = 0f;
                nameTagMesh.characterSize = 0.05f;
                nameTagMesh.lineSpacing = 0f;
                nameTagMesh.anchor = TextAnchor.MiddleCenter;
                nameTagMesh.alignment = TextAlignment.Center;
                nameTagMesh.tabSize = 0f;
                nameTagMesh.fontSize = 32;
                nameTagMesh.fontStyle = FontStyle.Normal;
                nameTagMesh.richText = false;
                SetNameTagColor(nameTagColor);
                SetNameTag(nameTag);
                nameTagMesh.renderer.enabled = true; // we need to set this explicity here since it might get hidden by the inspector
            }
            return (nameTagMesh != null);
        }
        
       
        
        //////////////////////////////////////////////////////////////
        // Updates 
        //////////////////////////////////////////////////////////////
        protected virtual void Update()
        {   
           
            // note that name tags are 3D text whose parent is this transform, and 
            // we're NOT offsetting scale... so the name tag gets larger for larger scales
            if (nameTagMesh != null)
            {
                nameTagMesh.gameObject.SetActive(showNameTag);
                if (showNameTag)
                {
                    nameTagMesh.transform.rotation = KAM3RA.User.Instance.camera.transform.rotation;
                    nameTagMesh.transform.position = KAM3RA.User.SetVectorY(transform.position, 
                                                                     transform.position.y + collider.height * transform.localScale.y + nameTagMesh.renderer.bounds.size.y * 2f) + nameTagOffset;
                    
                }
            }
        }
        protected virtual void LateUpdate()
        {
            // set the animation state here
            if (animation != null)
                animation.CrossFade(States.Name);
            
            // update sound -- not implemented in NpcController but subs would implement
            UpdateSound();
        }
        
        //////////////////////////////////////////////////////////////
        // Sound 
        //////////////////////////////////////////////////////////////
        protected virtual void UpdateSound()
        {
            // sub
        }
       
        
        //////////////////////////////////////////////////////////////
        // Other
        //////////////////////////////////////////////////////////////
        public virtual void SetEnabled(bool enabled)
        { 
            foreach (Renderer m in renderers)
                m.enabled = enabled;
        }
        public virtual void SetTarget(NpcController target)
        {
            SetTarget(target, false);
        }
        public virtual void SetTarget(NpcController target, bool watch)
        {
            this.target = target;
            watchTarget = watch;
        }
        public virtual float Distance(NpcController other)
        {
            return Vector3.Distance(transform.position, other.transform.position);
        }
        public virtual void ResetNameTagColor()
        {
            SetNameTagColor(nameTagStartColor);
        }
        public TextMesh GetNameTagMesh()
        {
            return nameTagMesh;
        }
        public virtual void ShowNameTag(bool show)
        {
            showNameTag = show;
            // for custom inspector only
            if (!Application.isPlaying)
            {
                TextMesh m = GetComponentInChildren<TextMesh>();
                if (m != null)
                    m.renderer.enabled = showNameTag;
            }
        }
        public virtual void SetNameTagColor(Color color)
        {
            nameTagColor = color;
            if (nameTagMesh != null)
            {
                nameTagMesh.renderer.material.color = nameTagColor;
            }
            // for custom inspector only
            if (!Application.isPlaying)
            {
                TextMesh m = GetComponentInChildren<TextMesh>();
                if (m != null)
                {
                    if (nameTagMaterial == null)
                        nameTagMaterial = new Material(m.renderer.sharedMaterial);
                    nameTagMaterial.color = nameTagColor;
                    m.renderer.material = nameTagMaterial;
                }
            }
        }
        public virtual void SetNameTag(string name)
        {
            nameTag = name;
            if (nameTagMesh != null)
                nameTagMesh.text = nameTag;
            // for custom inspector only
            if (!Application.isPlaying)
            {
                TextMesh m = GetComponentInChildren<TextMesh>();
                if (m != null)
                    m.text = nameTag;
            }
        }
        public virtual void AddUniformScale(float scale)
        {
            transform.localScale += new Vector3(scale, scale, scale);
        }
        public virtual void PrintPositions()
        {
            if (player)
                KAM3RA.User.PrintPositions(transform);
        }
        
        //////////////////////////////////////////////////////////////
        // State Properties 
        //////////////////////////////////////////////////////////////
        public StateMap States                      { get { return state; } }
        public string State                         { get { return state.state; } set { state.state = value; } }
        
        //////////////////////////////////////////////////////////////
        // Other Properties 
        //////////////////////////////////////////////////////////////
        // current bounding radius based on renderer -- keep in mind this could change depending on animation
        // also NOTE that ParticleSystems and TextMesh are rightly not included in the computation
        public virtual float RendererBoundingRadius
        { 
            get
            { 
                Bounds bounds = new Bounds(transform.position, Vector3.zero);
                foreach (Renderer m in renderers)
                {
                    if (m.gameObject.GetComponent<ParticleSystem>() != null)
                        continue;
                    if (m.gameObject.GetComponent<TextMesh>() != null)
                        continue;
                    bounds.Encapsulate(m.bounds);
                }
                return bounds.extents.magnitude; 
            } 
        }
        // bounding radius based on colliders
        public virtual float BoundingRadius
        { 
            get
            { 
                List<Collider> colliders = new List<Collider>(GetComponentsInChildren<Collider>(true));
                Bounds bounds = new Bounds(transform.position, Vector3.zero);
                foreach (Collider m in colliders)
                    bounds.Encapsulate(m.bounds);
                return bounds.extents.magnitude; 
            } 
        }
        // reasonable mass
        public virtual float Mass                   { get { return KAM3RA.User.CalculateMass(collider.bounds.extents.magnitude); } }
        // moving a bit faster than desired speed
        public virtual bool TooFast                 { get { return Speed > ScaledSpeed; } }
        // ... or a bit slower
        public virtual bool TooSlow                 { get { return Speed < ScaledSpeed; } }
        // actual current speed, *not* desired speed
        public virtual float Speed                  { get { return rigidbody.velocity.magnitude; } }
        // current horizontal speed, *not* desired speed
        public virtual float HorizontalSpeed        { get { return KAM3RA.User.SetVectorY(rigidbody.velocity, 0f).magnitude; } }
        // current speed adjusted by scale height of the object
        public virtual float ScaledSpeed            { get { return speed * Height; } }
        // max speed adjusted by scale height of the object
        public virtual float MaxScaledSpeed         { get { return maxSpeed * Height; } }
        // current position + adjustment for where the "eyes" are
        public virtual Vector3 EyePosition          { get { return KAM3RA.User.AddVectorY(transform.position, Height * eyeHeightScale); } }
        // same as above but the middle of the object
        public virtual Vector3 WaistPosition        { get { return KAM3RA.User.AddVectorY(transform.position, Height * 0.5f); } }
        // same as above but the bottom of the object
        public virtual Vector3 FootPosition         { get { return KAM3RA.User.AddVectorY(transform.position, Height * 0.1f); } }
        // simple collider radius
        public virtual float Radius                 { get { return radius; } }
        // we could use renderer.bounds.size.y * eyeHeightScale here, but that changes per frame a bit
        public virtual float Height                 { get { return collider.height * transform.localScale.y; } }
        // jump adjusted for scale and gravity
        public virtual float JumpVelocity           { get { return Mathf.Sqrt(jumpHeight * Height * -Physics.gravity.y * 2f); } }
        
        //////////////////////////////////////////////////////////////
        // Enums
        //////////////////////////////////////////////////////////////
        // major difference between Fly and Hover is gravity (when in the air -- hoverers have gravity when on ground and jumping)
        public enum Type
        {
            Ground,
            Fly,
            Hover
        }
        public enum CollisionState
        {
            None,
            Grounded,
            Blocked
        }
        public enum AltitudeState
        {
            Level,
            Up,
            Down
        }       
        
        //////////////////////////////////////////////////////////////
        // Other Classes
        //////////////////////////////////////////////////////////////
        [Serializable]
        public class StateMap
        {
            public string state = "Idle";
            public List<StateName> map = new List<StateName>();
            static string[] MOVING = new string[]
            {
                "RunForward",
                "RunBack",
                "StrafeRight",
                "StafeLeft",
                "WalkForward",
                "WalkBack"
            };
            public StateMap()
            {
                // these are the defaults germane to the test model, however they 
                // obviously be whatever you want them to be...
                // you can change the defaults here to give yourself a leg up
                // otherwise change, add or delete them via the inspector
                // NOTE that the NpcController class use these first 12 states so change/delete with caution
                Add("Idle", "Idle");        // no input
                Add("WalkForward", "Walk");         // walking forward at half ScaledSpeed
                Add("WalkBack", "Walk");        // walking back at half ScaleSpeed
                Add("RunForward", "Run");       // moving forward at ScaledSpeed
                Add("RunBack", "Run");      // moving back and ScaledSpeed
                Add("TurnLeft", "Shuffle");     // *not* moving and turning left with key/mouse
                Add("TurnRight", "Shuffle");    // *not* moving and turnign right with key/mouse
                Add("StafeLeft", "Walk");       // side-to-side left
                Add("StrafeRight", "Walk");         // side-to-side right
                Add("Jump", "Jump");        // start a jump
                Add("Fall", "Fall");        // after a certain velocity.y threshold, fall
                Add("Fly", "Fly");          // flying in-air
            }
            public string Name
            {
                get
                {
                    foreach (StateName m in map)
                        if (m.state == state)
                            return m.name;
                    return "Idle";
                }
            }
            public bool Moving                              { get { return IsState(MOVING); } }
            public bool MovingForward                       { get { return state == "WalkForward" || state == "RunForward"; } }
            public bool MovingBack                          { get { return state == "WalkBack" || state == "RunBack"; } }
            public bool Walking                             { get { return state == "WalkForward" || state == "WalkBack"; } }
            public bool Jumping                             { get { return state == "Jump"; } }
            public bool MovingOrJumping                     { get { return Jumping || Moving; } }
            public bool IsState(string[] states)
            {
                foreach (string m in states)
                    if (state == m)
                        return true;
                return false;
            }
            public StateName Add(string state, string name)
            {
                StateName m = new StateName(state, name);
                map.Add(m);
                return m;
            }
            public void Remove(StateName m)
            {
                map.Remove(m);
            }
        }
        [Serializable]
        public class StateName
        {
            public string state = "Idle", name = "Idle";
            public StateName(string state, string name)
            {
                this.state = state;
                this.name = name;
            }
        }
    }
}