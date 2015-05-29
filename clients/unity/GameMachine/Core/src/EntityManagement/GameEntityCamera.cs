using UnityEngine;

namespace GameMachine {
    namespace Common {
        public class GameEntityCamera : MonoBehaviour {

            public enum MouseButton {
                Left,
                Right,
                Middle,
                Mouse4,
                Mouse5
            }

            float targetYaw;
            float targetPitch;
            float targetDistance;

            float currentYaw;
            float currentPitch;
            float currentDistance;
            float currentMinDistance;
            float currentMaxDistance;

            float realDistance = 0f;

            public bool DisplayDebugGizmos = true;

            public Camera camera = null;
            public Transform target = null;

            public float MinDistance = 1f;
            public float MaxDistance = 32f;
            public float MinPitch = -80f;
            public float MaxPitch = 80f;
            public float ZoomSpeed = 16f;
            public float RotationMouseSpeed = 4f;

            public bool SmoothZoom = true;
            public float SmoothZoomSpeed = 8f;

            public bool SmoothRotation = true;
            public float SmoothRotationSpeed = 8f;

            public bool SmoothAutoRotation = true;
            public float SmoothAutoRotationSpeed = 4f;

            private LayerMask cameraObstacles = 0;
            public Vector3 TargetOffset = Vector3.zero;

            public string ZoomAxis = "Mouse ScrollWheel";
            public string YawAxis = "Mouse X";
            public string PitchAxis = "Mouse Y";
            public string MouseRotateButton = "Fire1";
            public string MouseLookButton = "Fire2";

            public bool LockCameraBehindTarget { get; set; }
            public bool RotateCameraBehindTarget { get; set; }

            public bool HasCamera { get { return camera != null; } }
            public bool HasTarget { get { return target != null; } }
            public Vector3 TargetPosition { get { return HasTarget ? target.position + TargetOffset : TargetOffset; } }

            [SerializeField]
            MouseButton mouseLookButton = MouseButton.Right;

            [SerializeField]
            float mouseTurnSpeed = 4f;

            void Start() {
                if (!HasCamera) {
                    camera = GetComponentInChildren<Camera>();
                }
                cameraObstacles = Settings.Instance().cameraObstacles;

                target = GameEntityManager.GetPlayerEntity().GetTransform();

                if (HasCamera) {
                    camera.transform.localPosition = Vector3.zero;
                    camera.transform.localRotation = Quaternion.identity;
                }

                MinPitch = Mathf.Clamp(MinPitch, -85f, 0f);
                MaxPitch = Mathf.Clamp(MaxPitch, 0f, 85f);
                MinDistance = Mathf.Max(0f, MinDistance);

                currentMinDistance = MinDistance;
                currentMaxDistance = MaxDistance;

                currentYaw = targetYaw = 0f;
                currentPitch = targetPitch = Mathf.Lerp(MinPitch, MaxPitch, 0.6f);
                currentDistance = targetDistance = realDistance = Mathf.Lerp(MinDistance, MaxDistance, 0.5f);
            }

            void Update() {
                if (InputManager.CameraDisabled()) {
                    return;
                }

                bool mouseLookDown = Input.GetMouseButton((int)mouseLookButton);
                bool mouseLookPressed = Input.GetMouseButtonDown((int)mouseLookButton);

                
                if (mouseLookDown) {
                    RotateCameraBehindTarget = true;
                    LockCameraBehindTarget = true;
                    float angles = Input.GetAxisRaw("Mouse X") * Time.smoothDeltaTime * mouseTurnSpeed;
                    target.RotateAround(Vector3.up, angles);
                }

                // If we pressed mouse look, set rotation
                if (mouseLookPressed) {
                   
                }
            }
            void LateUpdate() {
                if (InputManager.CameraDisabled()) {
                    return;
                }

                if (!HasCamera) {
                    Debug.LogError("No camera found");
                    return;
                }

                if (!HasTarget) {
                    Debug.LogError("No target found");
                    return;
                }

                bool rotate = InputUtils.GetButtonSafe(MouseRotateButton, false);
                bool mouseLook = InputUtils.GetButtonSafe(MouseLookButton, false);

                bool smoothRotation = SmoothRotation || SmoothAutoRotation;
                float smoothRotationSpeed = SmoothRotationSpeed;

                // This defines our "real" distance to the player
                realDistance -= InputUtils.GetAxisRawSafe(ZoomAxis, 0f) * ZoomSpeed;
                realDistance = Mathf.Clamp(realDistance, MinDistance, MaxDistance);

                // This is the distance we want to (clamped to what is viewable)
                targetDistance = realDistance;
                targetDistance = Mathf.Clamp(targetDistance, currentMinDistance, currentMaxDistance);

                // This is our current distance
                if (SmoothZoom) {
                    currentDistance = Mathf.Lerp(currentDistance, targetDistance, Time.deltaTime * SmoothZoomSpeed);
                } else {
                    currentDistance = targetDistance;
                }

                // Calculate offset vector
                Vector3 offset = new Vector3(0, 0, -currentDistance);

                // LMB is not down, but we should rotate camera behind target
                if (!rotate && RotateCameraBehindTarget) {
                    targetYaw = InputUtils.SignedAngle(offset.normalized, -target.forward, Vector3.up);
                    smoothRotation = SmoothAutoRotation;
                    smoothRotationSpeed = SmoothAutoRotationSpeed;
                }

                // Only LMB down and no lock
                if (rotate && !mouseLook && !LockCameraBehindTarget) {
                    targetYaw += (InputUtils.GetAxisRawSafe(YawAxis, 0f) * RotationMouseSpeed);
                    targetPitch -= (InputUtils.GetAxisRawSafe(PitchAxis, 0f) * RotationMouseSpeed);
                    targetPitch = Mathf.Clamp(targetPitch, MinPitch, MaxPitch);
                    smoothRotation = SmoothRotation;
                    smoothRotationSpeed = SmoothRotationSpeed;
                }

                // RMB 
                if (mouseLook && LockCameraBehindTarget) {
                    targetPitch -= (InputUtils.GetAxisRawSafe(PitchAxis, 0f) * RotationMouseSpeed);
                    targetPitch = Mathf.Clamp(targetPitch, MinPitch, MaxPitch);
                }

                // Lock camera behind target, this overrides everything
                if (LockCameraBehindTarget) {
                    targetYaw = InputUtils.SignedAngle(offset.normalized, -target.transform.forward, Vector3.up);
                    smoothRotation = false;
                }

                // Clamp targetYaw to -180, 180
                targetYaw = Mathf.Repeat(targetYaw + 180f, 360f) - 180f;

                if (!smoothRotation) {
                    currentYaw = targetYaw;
                    currentPitch = targetPitch;
                } else {
                    // Clamp smooth currentYaw to targetYaw and clamp it to -180, 180
                    currentYaw = Mathf.LerpAngle(currentYaw, targetYaw, Time.deltaTime * smoothRotationSpeed);
                    currentYaw = Mathf.Repeat(currentYaw + 180f, 360f) - 180f;

                    // Smooth pitch
                    currentPitch = Mathf.LerpAngle(currentPitch, targetPitch, Time.deltaTime * smoothRotationSpeed);
                }

                // Rotate offset vector
                offset = Quaternion.Euler(currentPitch, currentYaw, 0f) * offset;

                // Position camera holder correctly
                transform.position = TargetPosition + offset;

                // And then have the camera look at our target
                camera.transform.LookAt(TargetPosition);

                // Make sure we don't collide with anything
                float closest = float.MaxValue;
                bool mid = AvoidCollision(transform.position, ref closest);
                bool bottomLeft = AvoidCollision(camera.ScreenToWorldPoint(new Vector3(0, 0, camera.nearClipPlane)), ref closest);
                bool bottomRight = AvoidCollision(camera.ScreenToWorldPoint(new Vector3(0, Screen.height, camera.nearClipPlane)), ref closest);
                bool topLeft = AvoidCollision(camera.ScreenToWorldPoint(new Vector3(Screen.width, 0, camera.nearClipPlane)), ref closest);
                bool topRight = AvoidCollision(camera.ScreenToWorldPoint(new Vector3(Screen.width, Screen.height, camera.nearClipPlane)), ref closest);

                if (mid && bottomLeft && bottomRight && topLeft && topRight) {
                    currentMinDistance = MinDistance;
                    currentMaxDistance = MaxDistance;
                } else {
                    currentMinDistance = Mathf.Min(currentMinDistance, 1f);
                    currentMaxDistance = Mathf.Max(currentMinDistance + 0.05f, closest * 0.9f);
                }

                // Clear this flag
                LockCameraBehindTarget = false;
                RotateCameraBehindTarget = false;
            }

            bool AvoidCollision(Vector3 point, ref float closest) {
                RaycastHit hit;
                Vector3 direction = (point - TargetPosition).normalized;

                if (Physics.Raycast(TargetPosition, direction, out hit, MaxDistance, cameraObstacles)) {
                    float calculatedDistance = (hit.point - target.position).magnitude;

                    if (calculatedDistance < closest) {
                        closest = calculatedDistance;
                    }

                    return false;
                }

                return true;
            }

            void OnDrawGizmos() {
                if (DisplayDebugGizmos && HasTarget) {
                    Gizmos.color = Color.green;
                    Gizmos.DrawWireSphere(TargetPosition, currentMinDistance);

                    Gizmos.color = Color.blue;
                    Gizmos.DrawWireSphere(TargetPosition, currentMaxDistance);

                    Gizmos.color = Color.magenta;
                    Gizmos.DrawLine(TargetPosition, camera.ScreenToWorldPoint(new Vector3(0, 0, camera.nearClipPlane)));
                    Gizmos.DrawLine(TargetPosition, camera.ScreenToWorldPoint(new Vector3(Screen.width, Screen.height, camera.nearClipPlane)));
                    Gizmos.DrawLine(TargetPosition, camera.ScreenToWorldPoint(new Vector3(Screen.width, 0, camera.nearClipPlane)));
                    Gizmos.DrawLine(TargetPosition, camera.ScreenToWorldPoint(new Vector3(0, Screen.height, camera.nearClipPlane)));
                    Gizmos.DrawLine(TargetPosition, transform.position);
                }
            }
        }
    }
}
