package io.gamemachine.unity.unity_engine;

import java.util.concurrent.atomic.AtomicInteger;

import akka.actor.ActorRef;
import io.gamemachine.messages.*;
import io.gamemachine.net.Connection;
import io.gamemachine.unity.UnityMessageHandler;
import io.gamemachine.unity.unity_engine.engine_results.*;
import io.gamemachine.unity.unity_engine.unity_types.GameObject;
import io.gamemachine.unity.unity_engine.unity_types.Quaternion;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

public class UnityEngine {

    private static AtomicInteger requestId = new AtomicInteger();
    private String region;
    private ActorRef handler;

    public UnityEngine(String region, ActorRef handler) {
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
        } else if (request instanceof SyncComponentMessage) {
            clientMessage.unityMessage.syncComponentMessage = (SyncComponentMessage)request;
        }

        Connection connection = UnityMessageHandler.getConnection(region);
        if (connection != null) {
            UnityMessageHandler.addEngineRequest(messageId,this);
            connection.sendToClient(clientMessage);
        }

    }

    public boolean isAlive() {
       return RegionData.isAlive(region);
    }

    private void tellHandler(Object message) {
        HandlerMessage handlerMessage = new HandlerMessage(HandlerMessage.Type.EngineResult);
        handlerMessage.message = message;
        handler.tell(handlerMessage,null);
    }
    // Handle unity responses
    public void sphereCastResponse(SphereCastResponse response) {
        SphereCastResult result = new SphereCastResult();
        for (GmGameObject gmGo : response.gameObjects) {
            result.gameObjects.add(GameObject.fromGmGameObject(gmGo));
        }
        tellHandler(result);
    }

    public void overlapSphereResponse(OverlapSphereResponse response) {
        OverlapSphereResult result = new OverlapSphereResult();
        if (response.gameObjects != null) {
            for (GmGameObject gmGo : response.gameObjects) {
                result.gameObjects.add(GameObject.fromGmGameObject(gmGo));
            }
        }

        tellHandler(result);
    }

    public void raycastResponse(RaycastResponse response) {
        RaycastResult result = new RaycastResult();
        if (response.gameObjects != null) {
            for (GmGameObject gmGo : response.gameObjects) {
                result.gameObjects.add(GameObject.fromGmGameObject(gmGo));
            }
        }

        result.hit = response.hit;
        tellHandler(result);
    }

    public void instantiateResponse(InstantiateResponse response) {
        InstantiateResult result = new InstantiateResult();
        result.status = response.status;
        tellHandler(result);
    }

    public void destroyResponse(DestroyResponse response) {
        DestroyResult result = new DestroyResult();
        result.status = response.status;
        tellHandler(result);
    }

    public void pathResponse(PathResponse response) {
        PathResult result = new PathResult();

        if (response.status == PathResponse.Status.Success && response.path != null) {
            for (GmVector3 vec : response.path) {
                result.path.add(Vector3.fromGmVector3(vec));
            }
        }

        result.status = response.status;
        tellHandler(result);
    }



    // Send unity requests


    public void syncComponentMessage(SyncComponentMessage message) {
        SendRequest(message,requestId.getAndIncrement());
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

    public void raycast(String originId, String targetId, float distance, String layerMask) {
        RaycastRequest request = new RaycastRequest();
        request.originId = originId;
        request.targetId = targetId;
        request.maxDistance = distance;
        request.layerMask = layerMask;
        request.messageId = requestId.getAndIncrement();

        SendRequest(request,request.messageId);
    }





}
