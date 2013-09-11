
package com.game_machine.entity_system.generated;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.game_machine.entity_system.generated.Entity;

import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class Entity  implements Externalizable, Message<Entity>, Schema<Entity>
{




    public static Schema<Entity> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Entity getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Entity DEFAULT_INSTANCE = new Entity();



    public Player player;



    public Neighbors neighbors;



    public Health health;



    public ChatMessage chatMessage;



    public ClientConnection clientConnection;



    public EchoTest echoTest;



    public String id;



    public PlayerLogin playerLogin;



    public Subscribe subscribe;



    public Publish publish;



    public ChatChannel chatChannel;



    public JoinChat joinChat;



    public LeaveChat leaveChat;



    public Unsubscribe unsubscribe;



    public ChatRegister chatRegister;



    public ChatChannels chatChannels;



    public ErrorMessage errorMessage;



    public RegisterPlayerObserver registerPlayerObserver;



    public GetNeighbors getNeighbors;



    public TrackEntity trackEntity;



    public Transform transform;



    public IsNpc isNpc;



    public Vector3 vector3;



    public CreateSingleton createSingleton;



    public EntityList entityList;



    public Boolean published;



    public String entityType;



    public NotifySingleton notifySingleton;



    public DestroySingleton destroySingleton;



    public Effect effect;



    public EffectList effectList;



    public Attack attack;



    public CombatAbility combatAbility;



    public PlayerAuthenticated playerAuthenticated;



    public PlayerLogout playerLogout;



    public Boolean sendToPlayer;


    


    public Entity()
    {
        
    }



	public ArrayList<String> componentNames() {
		ArrayList<String> names = new ArrayList<String>();






		if (this.hasHealth()) {
			names.add(this.health.getClass().getSimpleName());
		}



		if (this.hasEffect()) {
			names.add(this.effect.getClass().getSimpleName());
		}



		if (this.hasEffectList()) {
			names.add(this.effectList.getClass().getSimpleName());
		}



		if (this.hasCombatAbility()) {
			names.add(this.combatAbility.getClass().getSimpleName());
		}



		if (this.hasAttack()) {
			names.add(this.attack.getClass().getSimpleName());
		}



		if (this.hasTrackEntity()) {
			names.add(this.trackEntity.getClass().getSimpleName());
		}



		if (this.hasRegisterPlayerObserver()) {
			names.add(this.registerPlayerObserver.getClass().getSimpleName());
		}







		if (this.hasPlayerLogout()) {
			names.add(this.playerLogout.getClass().getSimpleName());
		}



		if (this.hasPlayerAuthenticated()) {
			names.add(this.playerAuthenticated.getClass().getSimpleName());
		}





		if (this.hasErrorMessage()) {
			names.add(this.errorMessage.getClass().getSimpleName());
		}



		if (this.hasChatChannels()) {
			names.add(this.chatChannels.getClass().getSimpleName());
		}



		if (this.hasChatChannel()) {
			names.add(this.chatChannel.getClass().getSimpleName());
		}



		if (this.hasJoinChat()) {
			names.add(this.joinChat.getClass().getSimpleName());
		}



		if (this.hasLeaveChat()) {
			names.add(this.leaveChat.getClass().getSimpleName());
		}



		if (this.hasChatMessage()) {
			names.add(this.chatMessage.getClass().getSimpleName());
		}



		if (this.hasChatRegister()) {
			names.add(this.chatRegister.getClass().getSimpleName());
		}



		if (this.hasSubscribe()) {
			names.add(this.subscribe.getClass().getSimpleName());
		}



		if (this.hasUnsubscribe()) {
			names.add(this.unsubscribe.getClass().getSimpleName());
		}



		if (this.hasPublish()) {
			names.add(this.publish.getClass().getSimpleName());
		}









		if (this.hasClientConnection()) {
			names.add(this.clientConnection.getClass().getSimpleName());
		}



		if (this.hasPlayerLogin()) {
			names.add(this.playerLogin.getClass().getSimpleName());
		}





		if (this.hasIsNpc()) {
			names.add(this.isNpc.getClass().getSimpleName());
		}



		if (this.hasDestroySingleton()) {
			names.add(this.destroySingleton.getClass().getSimpleName());
		}



		if (this.hasNotifySingleton()) {
			names.add(this.notifySingleton.getClass().getSimpleName());
		}



		if (this.hasCreateSingleton()) {
			names.add(this.createSingleton.getClass().getSimpleName());
		}



		if (this.hasPlayer()) {
			names.add(this.player.getClass().getSimpleName());
		}



		if (this.hasVector3()) {
			names.add(this.vector3.getClass().getSimpleName());
		}





		if (this.hasTransform()) {
			names.add(this.transform.getClass().getSimpleName());
		}



		if (this.hasEchoTest()) {
			names.add(this.echoTest.getClass().getSimpleName());
		}





		if (this.hasNeighbors()) {
			names.add(this.neighbors.getClass().getSimpleName());
		}



		if (this.hasGetNeighbors()) {
			names.add(this.getNeighbors.getClass().getSimpleName());
		}





		if (this.hasEntityList()) {
			names.add(this.entityList.getClass().getSimpleName());
		}




		return names;
	}




    

	public Player getPlayer() {
		return player;
	}
	
	public Entity setPlayer(Player player) {
		this.player = player;
		return this;
	}
	
	public Boolean hasPlayer()  {
        return player == null ? false : true;
    }



    

	public Neighbors getNeighbors() {
		return neighbors;
	}
	
	public Entity setNeighbors(Neighbors neighbors) {
		this.neighbors = neighbors;
		return this;
	}
	
	public Boolean hasNeighbors()  {
        return neighbors == null ? false : true;
    }



    

	public Health getHealth() {
		return health;
	}
	
	public Entity setHealth(Health health) {
		this.health = health;
		return this;
	}
	
	public Boolean hasHealth()  {
        return health == null ? false : true;
    }



    

	public ChatMessage getChatMessage() {
		return chatMessage;
	}
	
	public Entity setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
		return this;
	}
	
	public Boolean hasChatMessage()  {
        return chatMessage == null ? false : true;
    }



    

	public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	public Entity setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		return this;
	}
	
	public Boolean hasClientConnection()  {
        return clientConnection == null ? false : true;
    }



    

	public EchoTest getEchoTest() {
		return echoTest;
	}
	
	public Entity setEchoTest(EchoTest echoTest) {
		this.echoTest = echoTest;
		return this;
	}
	
	public Boolean hasEchoTest()  {
        return echoTest == null ? false : true;
    }



    

	public String getId() {
		return id;
	}
	
	public Entity setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }



    

	public PlayerLogin getPlayerLogin() {
		return playerLogin;
	}
	
	public Entity setPlayerLogin(PlayerLogin playerLogin) {
		this.playerLogin = playerLogin;
		return this;
	}
	
	public Boolean hasPlayerLogin()  {
        return playerLogin == null ? false : true;
    }



    

	public Subscribe getSubscribe() {
		return subscribe;
	}
	
	public Entity setSubscribe(Subscribe subscribe) {
		this.subscribe = subscribe;
		return this;
	}
	
	public Boolean hasSubscribe()  {
        return subscribe == null ? false : true;
    }



    

	public Publish getPublish() {
		return publish;
	}
	
	public Entity setPublish(Publish publish) {
		this.publish = publish;
		return this;
	}
	
	public Boolean hasPublish()  {
        return publish == null ? false : true;
    }



    

	public ChatChannel getChatChannel() {
		return chatChannel;
	}
	
	public Entity setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
		return this;
	}
	
	public Boolean hasChatChannel()  {
        return chatChannel == null ? false : true;
    }



    

	public JoinChat getJoinChat() {
		return joinChat;
	}
	
	public Entity setJoinChat(JoinChat joinChat) {
		this.joinChat = joinChat;
		return this;
	}
	
	public Boolean hasJoinChat()  {
        return joinChat == null ? false : true;
    }



    

	public LeaveChat getLeaveChat() {
		return leaveChat;
	}
	
	public Entity setLeaveChat(LeaveChat leaveChat) {
		this.leaveChat = leaveChat;
		return this;
	}
	
	public Boolean hasLeaveChat()  {
        return leaveChat == null ? false : true;
    }



    

	public Unsubscribe getUnsubscribe() {
		return unsubscribe;
	}
	
	public Entity setUnsubscribe(Unsubscribe unsubscribe) {
		this.unsubscribe = unsubscribe;
		return this;
	}
	
	public Boolean hasUnsubscribe()  {
        return unsubscribe == null ? false : true;
    }



    

	public ChatRegister getChatRegister() {
		return chatRegister;
	}
	
	public Entity setChatRegister(ChatRegister chatRegister) {
		this.chatRegister = chatRegister;
		return this;
	}
	
	public Boolean hasChatRegister()  {
        return chatRegister == null ? false : true;
    }



    

	public ChatChannels getChatChannels() {
		return chatChannels;
	}
	
	public Entity setChatChannels(ChatChannels chatChannels) {
		this.chatChannels = chatChannels;
		return this;
	}
	
	public Boolean hasChatChannels()  {
        return chatChannels == null ? false : true;
    }



    

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
	
	public Entity setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}
	
	public Boolean hasErrorMessage()  {
        return errorMessage == null ? false : true;
    }



    

	public RegisterPlayerObserver getRegisterPlayerObserver() {
		return registerPlayerObserver;
	}
	
	public Entity setRegisterPlayerObserver(RegisterPlayerObserver registerPlayerObserver) {
		this.registerPlayerObserver = registerPlayerObserver;
		return this;
	}
	
	public Boolean hasRegisterPlayerObserver()  {
        return registerPlayerObserver == null ? false : true;
    }



    

	public GetNeighbors getGetNeighbors() {
		return getNeighbors;
	}
	
	public Entity setGetNeighbors(GetNeighbors getNeighbors) {
		this.getNeighbors = getNeighbors;
		return this;
	}
	
	public Boolean hasGetNeighbors()  {
        return getNeighbors == null ? false : true;
    }



    

	public TrackEntity getTrackEntity() {
		return trackEntity;
	}
	
	public Entity setTrackEntity(TrackEntity trackEntity) {
		this.trackEntity = trackEntity;
		return this;
	}
	
	public Boolean hasTrackEntity()  {
        return trackEntity == null ? false : true;
    }



    

	public Transform getTransform() {
		return transform;
	}
	
	public Entity setTransform(Transform transform) {
		this.transform = transform;
		return this;
	}
	
	public Boolean hasTransform()  {
        return transform == null ? false : true;
    }



    

	public IsNpc getIsNpc() {
		return isNpc;
	}
	
	public Entity setIsNpc(IsNpc isNpc) {
		this.isNpc = isNpc;
		return this;
	}
	
	public Boolean hasIsNpc()  {
        return isNpc == null ? false : true;
    }



    

	public Vector3 getVector3() {
		return vector3;
	}
	
	public Entity setVector3(Vector3 vector3) {
		this.vector3 = vector3;
		return this;
	}
	
	public Boolean hasVector3()  {
        return vector3 == null ? false : true;
    }



    

	public CreateSingleton getCreateSingleton() {
		return createSingleton;
	}
	
	public Entity setCreateSingleton(CreateSingleton createSingleton) {
		this.createSingleton = createSingleton;
		return this;
	}
	
	public Boolean hasCreateSingleton()  {
        return createSingleton == null ? false : true;
    }



    

	public EntityList getEntityList() {
		return entityList;
	}
	
	public Entity setEntityList(EntityList entityList) {
		this.entityList = entityList;
		return this;
	}
	
	public Boolean hasEntityList()  {
        return entityList == null ? false : true;
    }



    

	public Boolean getPublished() {
		return published;
	}
	
	public Entity setPublished(Boolean published) {
		this.published = published;
		return this;
	}
	
	public Boolean hasPublished()  {
        return published == null ? false : true;
    }



    

	public String getEntityType() {
		return entityType;
	}
	
	public Entity setEntityType(String entityType) {
		this.entityType = entityType;
		return this;
	}
	
	public Boolean hasEntityType()  {
        return entityType == null ? false : true;
    }



    

	public NotifySingleton getNotifySingleton() {
		return notifySingleton;
	}
	
	public Entity setNotifySingleton(NotifySingleton notifySingleton) {
		this.notifySingleton = notifySingleton;
		return this;
	}
	
	public Boolean hasNotifySingleton()  {
        return notifySingleton == null ? false : true;
    }



    

	public DestroySingleton getDestroySingleton() {
		return destroySingleton;
	}
	
	public Entity setDestroySingleton(DestroySingleton destroySingleton) {
		this.destroySingleton = destroySingleton;
		return this;
	}
	
	public Boolean hasDestroySingleton()  {
        return destroySingleton == null ? false : true;
    }



    

	public Effect getEffect() {
		return effect;
	}
	
	public Entity setEffect(Effect effect) {
		this.effect = effect;
		return this;
	}
	
	public Boolean hasEffect()  {
        return effect == null ? false : true;
    }



    

	public EffectList getEffectList() {
		return effectList;
	}
	
	public Entity setEffectList(EffectList effectList) {
		this.effectList = effectList;
		return this;
	}
	
	public Boolean hasEffectList()  {
        return effectList == null ? false : true;
    }



    

	public Attack getAttack() {
		return attack;
	}
	
	public Entity setAttack(Attack attack) {
		this.attack = attack;
		return this;
	}
	
	public Boolean hasAttack()  {
        return attack == null ? false : true;
    }



    

	public CombatAbility getCombatAbility() {
		return combatAbility;
	}
	
	public Entity setCombatAbility(CombatAbility combatAbility) {
		this.combatAbility = combatAbility;
		return this;
	}
	
	public Boolean hasCombatAbility()  {
        return combatAbility == null ? false : true;
    }



    

	public PlayerAuthenticated getPlayerAuthenticated() {
		return playerAuthenticated;
	}
	
	public Entity setPlayerAuthenticated(PlayerAuthenticated playerAuthenticated) {
		this.playerAuthenticated = playerAuthenticated;
		return this;
	}
	
	public Boolean hasPlayerAuthenticated()  {
        return playerAuthenticated == null ? false : true;
    }



    

	public PlayerLogout getPlayerLogout() {
		return playerLogout;
	}
	
	public Entity setPlayerLogout(PlayerLogout playerLogout) {
		this.playerLogout = playerLogout;
		return this;
	}
	
	public Boolean hasPlayerLogout()  {
        return playerLogout == null ? false : true;
    }



    

	public Boolean getSendToPlayer() {
		return sendToPlayer;
	}
	
	public Entity setSendToPlayer(Boolean sendToPlayer) {
		this.sendToPlayer = sendToPlayer;
		return this;
	}
	
	public Boolean hasSendToPlayer()  {
        return sendToPlayer == null ? false : true;
    }



  
    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<Entity> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Entity newMessage()
    {
        return new Entity();
    }

    public Class<Entity> typeClass()
    {
        return Entity.class;
    }

    public String messageName()
    {
        return Entity.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Entity.class.getName();
    }

    public boolean isInitialized(Entity message)
    {
        return true;
    }

    public void mergeFrom(Input input, Entity message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;

            	case 1:


                	message.player = input.mergeObject(message.player, Player.getSchema());
                    break;

                	


            	case 2:


                	message.neighbors = input.mergeObject(message.neighbors, Neighbors.getSchema());
                    break;

                	


            	case 3:


                	message.health = input.mergeObject(message.health, Health.getSchema());
                    break;

                	


            	case 4:


                	message.chatMessage = input.mergeObject(message.chatMessage, ChatMessage.getSchema());
                    break;

                	


            	case 5:


                	message.clientConnection = input.mergeObject(message.clientConnection, ClientConnection.getSchema());
                    break;

                	


            	case 6:


                	message.echoTest = input.mergeObject(message.echoTest, EchoTest.getSchema());
                    break;

                	


            	case 7:


                	message.id = input.readString();
                	break;

                	


            	case 8:


                	message.playerLogin = input.mergeObject(message.playerLogin, PlayerLogin.getSchema());
                    break;

                	


            	case 9:


                	message.subscribe = input.mergeObject(message.subscribe, Subscribe.getSchema());
                    break;

                	


            	case 10:


                	message.publish = input.mergeObject(message.publish, Publish.getSchema());
                    break;

                	


            	case 11:


                	message.chatChannel = input.mergeObject(message.chatChannel, ChatChannel.getSchema());
                    break;

                	


            	case 12:


                	message.joinChat = input.mergeObject(message.joinChat, JoinChat.getSchema());
                    break;

                	


            	case 13:


                	message.leaveChat = input.mergeObject(message.leaveChat, LeaveChat.getSchema());
                    break;

                	


            	case 14:


                	message.unsubscribe = input.mergeObject(message.unsubscribe, Unsubscribe.getSchema());
                    break;

                	


            	case 15:


                	message.chatRegister = input.mergeObject(message.chatRegister, ChatRegister.getSchema());
                    break;

                	


            	case 16:


                	message.chatChannels = input.mergeObject(message.chatChannels, ChatChannels.getSchema());
                    break;

                	


            	case 17:


                	message.errorMessage = input.mergeObject(message.errorMessage, ErrorMessage.getSchema());
                    break;

                	


            	case 18:


                	message.registerPlayerObserver = input.mergeObject(message.registerPlayerObserver, RegisterPlayerObserver.getSchema());
                    break;

                	


            	case 21:


                	message.getNeighbors = input.mergeObject(message.getNeighbors, GetNeighbors.getSchema());
                    break;

                	


            	case 22:


                	message.trackEntity = input.mergeObject(message.trackEntity, TrackEntity.getSchema());
                    break;

                	


            	case 23:


                	message.transform = input.mergeObject(message.transform, Transform.getSchema());
                    break;

                	


            	case 24:


                	message.isNpc = input.mergeObject(message.isNpc, IsNpc.getSchema());
                    break;

                	


            	case 25:


                	message.vector3 = input.mergeObject(message.vector3, Vector3.getSchema());
                    break;

                	


            	case 26:


                	message.createSingleton = input.mergeObject(message.createSingleton, CreateSingleton.getSchema());
                    break;

                	


            	case 27:


                	message.entityList = input.mergeObject(message.entityList, EntityList.getSchema());
                    break;

                	


            	case 29:


                	message.published = input.readBool();
                	break;

                	


            	case 30:


                	message.entityType = input.readString();
                	break;

                	


            	case 31:


                	message.notifySingleton = input.mergeObject(message.notifySingleton, NotifySingleton.getSchema());
                    break;

                	


            	case 32:


                	message.destroySingleton = input.mergeObject(message.destroySingleton, DestroySingleton.getSchema());
                    break;

                	


            	case 33:


                	message.effect = input.mergeObject(message.effect, Effect.getSchema());
                    break;

                	


            	case 34:


                	message.effectList = input.mergeObject(message.effectList, EffectList.getSchema());
                    break;

                	


            	case 35:


                	message.attack = input.mergeObject(message.attack, Attack.getSchema());
                    break;

                	


            	case 36:


                	message.combatAbility = input.mergeObject(message.combatAbility, CombatAbility.getSchema());
                    break;

                	


            	case 37:


                	message.playerAuthenticated = input.mergeObject(message.playerAuthenticated, PlayerAuthenticated.getSchema());
                    break;

                	


            	case 38:


                	message.playerLogout = input.mergeObject(message.playerLogout, PlayerLogout.getSchema());
                    break;

                	


            	case 39:


                	message.sendToPlayer = input.readBool();
                	break;

                	


            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Entity message) throws IOException
    {

    	

    	


    	if(message.player != null)
    		output.writeObject(1, message.player, Player.getSchema(), false);

    	


    	

    	


    	if(message.neighbors != null)
    		output.writeObject(2, message.neighbors, Neighbors.getSchema(), false);

    	


    	

    	


    	if(message.health != null)
    		output.writeObject(3, message.health, Health.getSchema(), false);

    	


    	

    	


    	if(message.chatMessage != null)
    		output.writeObject(4, message.chatMessage, ChatMessage.getSchema(), false);

    	


    	

    	


    	if(message.clientConnection != null)
    		output.writeObject(5, message.clientConnection, ClientConnection.getSchema(), false);

    	


    	

    	


    	if(message.echoTest != null)
    		output.writeObject(6, message.echoTest, EchoTest.getSchema(), false);

    	


    	

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	


    	if(message.id != null)
            output.writeString(7, message.id, false);

    	


    	

    	


    	if(message.playerLogin != null)
    		output.writeObject(8, message.playerLogin, PlayerLogin.getSchema(), false);

    	


    	

    	


    	if(message.subscribe != null)
    		output.writeObject(9, message.subscribe, Subscribe.getSchema(), false);

    	


    	

    	


    	if(message.publish != null)
    		output.writeObject(10, message.publish, Publish.getSchema(), false);

    	


    	

    	


    	if(message.chatChannel != null)
    		output.writeObject(11, message.chatChannel, ChatChannel.getSchema(), false);

    	


    	

    	


    	if(message.joinChat != null)
    		output.writeObject(12, message.joinChat, JoinChat.getSchema(), false);

    	


    	

    	


    	if(message.leaveChat != null)
    		output.writeObject(13, message.leaveChat, LeaveChat.getSchema(), false);

    	


    	

    	


    	if(message.unsubscribe != null)
    		output.writeObject(14, message.unsubscribe, Unsubscribe.getSchema(), false);

    	


    	

    	


    	if(message.chatRegister != null)
    		output.writeObject(15, message.chatRegister, ChatRegister.getSchema(), false);

    	


    	

    	


    	if(message.chatChannels != null)
    		output.writeObject(16, message.chatChannels, ChatChannels.getSchema(), false);

    	


    	

    	


    	if(message.errorMessage != null)
    		output.writeObject(17, message.errorMessage, ErrorMessage.getSchema(), false);

    	


    	

    	


    	if(message.registerPlayerObserver != null)
    		output.writeObject(18, message.registerPlayerObserver, RegisterPlayerObserver.getSchema(), false);

    	


    	

    	


    	if(message.getNeighbors != null)
    		output.writeObject(21, message.getNeighbors, GetNeighbors.getSchema(), false);

    	


    	

    	


    	if(message.trackEntity != null)
    		output.writeObject(22, message.trackEntity, TrackEntity.getSchema(), false);

    	


    	

    	


    	if(message.transform != null)
    		output.writeObject(23, message.transform, Transform.getSchema(), false);

    	


    	

    	


    	if(message.isNpc != null)
    		output.writeObject(24, message.isNpc, IsNpc.getSchema(), false);

    	


    	

    	


    	if(message.vector3 != null)
    		output.writeObject(25, message.vector3, Vector3.getSchema(), false);

    	


    	

    	


    	if(message.createSingleton != null)
    		output.writeObject(26, message.createSingleton, CreateSingleton.getSchema(), false);

    	


    	

    	


    	if(message.entityList != null)
    		output.writeObject(27, message.entityList, EntityList.getSchema(), false);

    	


    	

    	


    	if(message.published != null)
            output.writeBool(29, message.published, false);

    	


    	

    	


    	if(message.entityType != null)
            output.writeString(30, message.entityType, false);

    	


    	

    	


    	if(message.notifySingleton != null)
    		output.writeObject(31, message.notifySingleton, NotifySingleton.getSchema(), false);

    	


    	

    	


    	if(message.destroySingleton != null)
    		output.writeObject(32, message.destroySingleton, DestroySingleton.getSchema(), false);

    	


    	

    	


    	if(message.effect != null)
    		output.writeObject(33, message.effect, Effect.getSchema(), false);

    	


    	

    	


    	if(message.effectList != null)
    		output.writeObject(34, message.effectList, EffectList.getSchema(), false);

    	


    	

    	


    	if(message.attack != null)
    		output.writeObject(35, message.attack, Attack.getSchema(), false);

    	


    	

    	


    	if(message.combatAbility != null)
    		output.writeObject(36, message.combatAbility, CombatAbility.getSchema(), false);

    	


    	

    	


    	if(message.playerAuthenticated != null)
    		output.writeObject(37, message.playerAuthenticated, PlayerAuthenticated.getSchema(), false);

    	


    	

    	


    	if(message.playerLogout != null)
    		output.writeObject(38, message.playerLogout, PlayerLogout.getSchema(), false);

    	


    	

    	


    	if(message.sendToPlayer != null)
            output.writeBool(39, message.sendToPlayer, false);

    	


    	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {

        	case 1: return "player";

        	case 2: return "neighbors";

        	case 3: return "health";

        	case 4: return "chatMessage";

        	case 5: return "clientConnection";

        	case 6: return "echoTest";

        	case 7: return "id";

        	case 8: return "playerLogin";

        	case 9: return "subscribe";

        	case 10: return "publish";

        	case 11: return "chatChannel";

        	case 12: return "joinChat";

        	case 13: return "leaveChat";

        	case 14: return "unsubscribe";

        	case 15: return "chatRegister";

        	case 16: return "chatChannels";

        	case 17: return "errorMessage";

        	case 18: return "registerPlayerObserver";

        	case 21: return "getNeighbors";

        	case 22: return "trackEntity";

        	case 23: return "transform";

        	case 24: return "isNpc";

        	case 25: return "vector3";

        	case 26: return "createSingleton";

        	case 27: return "entityList";

        	case 29: return "published";

        	case 30: return "entityType";

        	case 31: return "notifySingleton";

        	case 32: return "destroySingleton";

        	case 33: return "effect";

        	case 34: return "effectList";

        	case 35: return "attack";

        	case 36: return "combatAbility";

        	case 37: return "playerAuthenticated";

        	case 38: return "playerLogout";

        	case 39: return "sendToPlayer";

            default: return null;
        }
    }

    public int getFieldNumber(String name)
    {
        final Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final java.util.HashMap<String,Integer> __fieldMap = new java.util.HashMap<String,Integer>();
    static
    {

    	__fieldMap.put("player", 1);

    	__fieldMap.put("neighbors", 2);

    	__fieldMap.put("health", 3);

    	__fieldMap.put("chatMessage", 4);

    	__fieldMap.put("clientConnection", 5);

    	__fieldMap.put("echoTest", 6);

    	__fieldMap.put("id", 7);

    	__fieldMap.put("playerLogin", 8);

    	__fieldMap.put("subscribe", 9);

    	__fieldMap.put("publish", 10);

    	__fieldMap.put("chatChannel", 11);

    	__fieldMap.put("joinChat", 12);

    	__fieldMap.put("leaveChat", 13);

    	__fieldMap.put("unsubscribe", 14);

    	__fieldMap.put("chatRegister", 15);

    	__fieldMap.put("chatChannels", 16);

    	__fieldMap.put("errorMessage", 17);

    	__fieldMap.put("registerPlayerObserver", 18);

    	__fieldMap.put("getNeighbors", 21);

    	__fieldMap.put("trackEntity", 22);

    	__fieldMap.put("transform", 23);

    	__fieldMap.put("isNpc", 24);

    	__fieldMap.put("vector3", 25);

    	__fieldMap.put("createSingleton", 26);

    	__fieldMap.put("entityList", 27);

    	__fieldMap.put("published", 29);

    	__fieldMap.put("entityType", 30);

    	__fieldMap.put("notifySingleton", 31);

    	__fieldMap.put("destroySingleton", 32);

    	__fieldMap.put("effect", 33);

    	__fieldMap.put("effectList", 34);

    	__fieldMap.put("attack", 35);

    	__fieldMap.put("combatAbility", 36);

    	__fieldMap.put("playerAuthenticated", 37);

    	__fieldMap.put("playerLogout", 38);

    	__fieldMap.put("sendToPlayer", 39);

    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Entity.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Entity parseFrom(byte[] bytes) {
	Entity message = new Entity();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(Entity.class));
	return message;
}

public Entity clone() {
	byte[] bytes = this.toByteArray();
	Entity entity = Entity.parseFrom(bytes);
	return entity;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Entity.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}
		
public byte[] toProtobuf() {
	LinkedBuffer buffer = LinkedBuffer.allocate(8024);
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(Entity.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
