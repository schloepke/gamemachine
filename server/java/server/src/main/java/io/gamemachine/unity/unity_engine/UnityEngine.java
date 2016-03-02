package io.gamemachine.unity.unity_engine;

import java.util.concurrent.atomic.AtomicInteger;

import io.gamemachine.messages.*;
import io.gamemachine.net.Connection;
import io.gamemachine.unity.UnityMessageHandler;
import io.gamemachine.unity.unity_engine.unity_types.GameObject;
import io.gamemachine.unity.unity_engine.unity_types.Quaternion;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

public class UnityEngine {

    private static AtomicInteger requestId = new AtomicInteger();
    private String region;
    private UnityEngineHandler handler;

    public UnityEngine(String region, UnityEngineHandler handler) {
        this.region = region;
        this.handler = handler;
    }

    private void SendRequest(Object request, int messageId) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.unityMessage = new UnityMessage();
        clientMessage.unityMessage.playerId = region;
        clientMessage.unityMessage.unityEngineRequest = new UnityEngineRequest();

        if (request instanceof OverlapSphereRequest) {
            clientMessage.unityMessage.unityEngineRequest.overlapSphereRequest = (OverlapSphereRequest) request;
        } else if (request instanceof RaycastRequest) {
            clientMessage.unityMessage.unityEngineRequest.raycastRequest = (RaycastRequest) request;
        } else if (request instanceof SphereCastRequest) {
            clientMessage.unityMessage.unityEngineRequest.sphereCastRequest = (SphereCastRequest) request;
        } else if (request instanceof InstantiateRequest) {
            clientMessage.unityMessage.unityEngineRequest.instantiateRequest = (InstantiateRequest) request;
        } else if (request instanceof DestroyRequest) {
            clientMessage.unityMessage.unityEngineRequest.destroyRequest = (DestroyRequest) request;
        } else if (request instanceof PathRequest) {
            clientMessage.unityMessage.unityEngineRequest.pathRequest = (PathRequest) request;
        } else if (request instanceof UnityConfigRequest) {
            clientMessage.unityMessage.unityEngineRequest.unityConfigRequest = (UnityConfigRequest) request;
        }

        Connection connection = UnityMessageHandler.getConnection(region);
        if (connection != null) {
            UnityMessageHandler.addEngineRequest(messageId,this);
            connection.sendToClient(clientMessage);
        }

    }

    public boolean isAlive() {
       return UnityMessageHandler.isAlive(region);
    }

    // Handle unity responses
    public void sphereCastResponse(SphereCastResponse response) {
        SphereCastResult result = new SphereCastResult();
        for (GmGameObject gmGo : response.gameObjects) {
            result.gameObjects.add(GameObject.fromGmGameObject(gmGo));
        }
        handler.onEngineResult(result);
    }

    public void overlapSphereResponse(OverlapSphereResponse response) {
        OverlapSphereResult result = new OverlapSphereResult();
        for (GmGameObject gmGo : response.gameObjects) {
            result.gameObjects.add(GameObject.fromGmGameObject(gmGo));
        }
        handler.onEngineResult(result);
    }

    public void raycastResponse(RaycastResponse response) {
        RaycastResult result = new RaycastResult();
        for (GmGameObject gmGo : response.gameObjects) {
            result.gameObjects.add(GameObject.fromGmGameObject(gmGo));
        }
        result.hit = response.hit;
        handler.onEngineResult(result);
    }

    public void instantiateResponse(InstantiateResponse response) {
        InstantiateResult result = new InstantiateResult();
        result.status = response.status;
        handler.onEngineResult(result);
    }

    public void destroyResponse(DestroyResponse response) {
        DestroyResult result = new DestroyResult();
        result.status = response.status;
        handler.onEngineResult(result);
    }

    public void pathResponse(PathResponse response) {
        PathResult result = new PathResult();

        if (result.status == PathResponse.Status.Success && result.path != null) {
            for (GmVector3 vec : response.path) {
                result.path.add(Vector3.fromGmVector3(vec));
            }
        }

        result.status = response.status;
        handler.onEngineResult(result);
    }

    public void unityConfigResponse(UnityConfigResponse response) {
        UnityConfigResult result = new UnityConfigResult();
        result.npcGroupDefs = response.npcGroupDef;

        handler.onEngineResult(result);
    }


    // Send unity requests

    public void unityConfigRequest() {
        UnityConfigRequest request = new UnityConfigRequest();
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }

    public void findPath(Vector3 start, Vector3 end) {
        PathRequest request = new PathRequest();
        request.start = Vector3.toGmVector3(start);
        request.end = Vector3.toGmVector3(end);
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }

    public void destroy(String name) {
        DestroyRequest request = new DestroyRequest();
        request.name = name;
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }

    public void instantiate(String prefab, String name, Vector3 position, Quaternion rotation) {
        InstantiateRequest request = new InstantiateRequest();
        request.prefab = prefab;
        request.name = name;
        request.position = position.toGmVector3();
        request.rotation = rotation.toGmQuaternion();
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }

    public void instantiate(String prefab, String name, Vector3 position) {
        instantiate(prefab,name,position,Quaternion.identity);
    }

    public void sphereCast(Vector3 origin, Vector3 direction, float radius, float distance, String layerMask) {
        SphereCastRequest request = new SphereCastRequest();
        request.origin = Vector3.toGmVector3(origin);
        request.direction = Vector3.toGmVector3(direction);
        request.radius = radius;
        request.layerMask = layerMask;
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }

    public void overlapSphere(Vector3 origin, float radius, String layerMask) {
        OverlapSphereRequest request = new OverlapSphereRequest();
        request.origin = Vector3.toGmVector3(origin);
        request.radius = radius;
        request.layerMask = layerMask;
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }

    public void raycast(Vector3 origin, Vector3 direction, float distance, String layerMask) {
        RaycastRequest request = new RaycastRequest();
        request.origin = Vector3.toGmVector3(origin);
        request.direction = Vector3.toGmVector3(direction);
        request.maxDistance = distance;
        request.layerMask = layerMask;
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }





}
