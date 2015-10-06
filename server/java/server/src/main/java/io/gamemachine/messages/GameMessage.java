
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;





import org.javalite.common.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class GameMessage implements Externalizable, Message<GameMessage>, Schema<GameMessage>{

private static final Logger logger = LoggerFactory.getLogger(GameMessage.class);



    public static Schema<GameMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameMessage DEFAULT_INSTANCE = new GameMessage();
    static final String defaultScope = GameMessage.class.getSimpleName();

    	
							    public String playerId= null;
		    			    
		
    
        	
							    public String messageId= null;
		    			    
		
    
        	
							    public int destinationId= 0;
		    			    
		
    
        	
							    public String destination= null;
		    			    
		
    
        	
							    public String agentId= null;
		    			    
		
    
        	
					public DynamicMessage dynamicMessage = null;
			    
		
    
        	
					public PathData pathData = null;
			    
		
    
        	
					public PlayerItems playerItems = null;
			    
		
    
        	
					public AddPlayerItem addPlayerItem = null;
			    
		
    
        	
					public RemovePlayerItem removePlayerItem = null;
			    
		
    
        	
					public RequestPlayerItems requestPlayerItems = null;
			    
		
    
        	
					public PvpGameMessage pvpGameMessage = null;
			    
		
    
        	
					public Harvest harvest = null;
			    
		
    
        	
							    public long authtoken= 0L;
		    			    
		
    
        	
							    public long unityMessageId= 0L;
		    			    
		
    
        	
					public CraftItem craftItem = null;
			    
		
    
        	
					public UseSkill useSkill = null;
			    
		
    
        	
					public PlayerSkills playerSkills = null;
			    
		
    
        	
					public Vitals vitals = null;
			    
		
    
        	
					public Attack attack = null;
			    
		
    
        	
					public StatusEffect statusEffect = null;
			    
		
    
        	
					public StatusEffectResult statusEffectResult = null;
			    
		
    
        	
					public DataRequest dataRequest = null;
			    
		
    
        	
					public VisualEffect visualEffect = null;
			    
		
    
        	
					public WorldObjects worldObjects = null;
			    
		
    
        	
					public SiegeCommand siegeCommand = null;
			    
		
    
        	
					public GmBounds bounds = null;
			    
		
    
        	
					public UseItem useItem = null;
			    
		
    
        	
					public TimeCycle timeCycle = null;
			    
		
    
        	
					public Guilds guilds = null;
			    
		
    
        	
					public GuildAction guildAction = null;
			    
		
    
        	
					public GuildMemberList guildMemberList = null;
			    
		
    
        	
					public Territory territory = null;
			    
		
    
        	
					public Territories territories = null;
			    
		
    
        	
					public EquippedItem equippedItem = null;
			    
		
    
        	
					public ComboAttack comboAttack = null;
			    
		
    
        	
					public NpcData npcData = null;
			    
		
    
        	
					public BuildObjects buildObjects = null;
			    
		
    
        	
					public BuildableAreas buildableAreas = null;
			    
		
    
        	
					public GmStats gmStats = null;
			    
		
    
        	
					public Zone zone = null;
			    
		
    
        	
					public BuildObjectChunks buildObjectChunks = null;
			    
		
    
        	
					public TerrainEdits terrainEdits = null;
			    
		
    
        	
					public UpdatePlayerItem updatePlayerItem = null;
			    
		
    
        


    public GameMessage()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("game_message_player_id",null);
    	    	    	    	    	    	model.set("game_message_message_id",null);
    	    	    	    	    	    	model.set("game_message_destination_id",null);
    	    	    	    	    	    	model.set("game_message_destination",null);
    	    	    	    	    	    	model.set("game_message_agent_id",null);
    	    	    	    	    	    	    	    	    	    	    	    	    	    	model.set("game_message_authtoken",null);
    	    	    	    	    	    	model.set("game_message_unity_message_id",null);
    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (playerId != null) {
    	       	    	model.setString("game_message_player_id",playerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (messageId != null) {
    	       	    	model.setString("game_message_message_id",messageId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (destinationId != null) {
    	       	    	model.setInteger("game_message_destination_id",destinationId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (destination != null) {
    	       	    	model.setString("game_message_destination",destination);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (agentId != null) {
    	       	    	model.setString("game_message_agent_id",agentId);
    	        		
    	//}
    	    	    	    	    	    	    	    	    	    	    	    	    	
    	    	    	//if (authtoken != null) {
    	       	    	model.setLong("game_message_authtoken",authtoken);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (unityMessageId != null) {
    	       	    	model.setLong("game_message_unity_message_id",unityMessageId);
    	        		
    	//}
    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    }
    
	public static GameMessage fromModel(Model model) {
		boolean hasFields = false;
    	GameMessage message = new GameMessage();
    	    	    	    	    	
    	    			String playerIdTestField = model.getString("game_message_player_id");
		if (playerIdTestField != null) {
			String playerIdField = playerIdTestField;
			message.setPlayerId(playerIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String messageIdTestField = model.getString("game_message_message_id");
		if (messageIdTestField != null) {
			String messageIdField = messageIdTestField;
			message.setMessageId(messageIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Integer destinationIdTestField = model.getInteger("game_message_destination_id");
		if (destinationIdTestField != null) {
			int destinationIdField = destinationIdTestField;
			message.setDestinationId(destinationIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String destinationTestField = model.getString("game_message_destination");
		if (destinationTestField != null) {
			String destinationField = destinationTestField;
			message.setDestination(destinationField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			String agentIdTestField = model.getString("game_message_agent_id");
		if (agentIdTestField != null) {
			String agentIdField = agentIdTestField;
			message.setAgentId(agentIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	    	    	    	    	    	    	    	
    	    			Long authtokenTestField = model.getLong("game_message_authtoken");
		if (authtokenTestField != null) {
			long authtokenField = authtokenTestField;
			message.setAuthtoken(authtokenField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	
    	    			Long unityMessageIdTestField = model.getLong("game_message_unity_message_id");
		if (unityMessageIdTestField != null) {
			long unityMessageIdField = unityMessageIdTestField;
			message.setUnityMessageId(unityMessageIdField);
			hasFields = true;
		}
    	
    	    	
    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public String getPlayerId() {
		return playerId;
	}
	
	public GameMessage setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		            
		public String getMessageId() {
		return messageId;
	}
	
	public GameMessage setMessageId(String messageId) {
		this.messageId = messageId;
		return this;	}
	
		            
		public int getDestinationId() {
		return destinationId;
	}
	
	public GameMessage setDestinationId(int destinationId) {
		this.destinationId = destinationId;
		return this;	}
	
		            
		public String getDestination() {
		return destination;
	}
	
	public GameMessage setDestination(String destination) {
		this.destination = destination;
		return this;	}
	
		            
		public String getAgentId() {
		return agentId;
	}
	
	public GameMessage setAgentId(String agentId) {
		this.agentId = agentId;
		return this;	}
	
		            
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public GameMessage setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;	}
	
		            
		public PathData getPathData() {
		return pathData;
	}
	
	public GameMessage setPathData(PathData pathData) {
		this.pathData = pathData;
		return this;	}
	
		            
		public PlayerItems getPlayerItems() {
		return playerItems;
	}
	
	public GameMessage setPlayerItems(PlayerItems playerItems) {
		this.playerItems = playerItems;
		return this;	}
	
		            
		public AddPlayerItem getAddPlayerItem() {
		return addPlayerItem;
	}
	
	public GameMessage setAddPlayerItem(AddPlayerItem addPlayerItem) {
		this.addPlayerItem = addPlayerItem;
		return this;	}
	
		            
		public RemovePlayerItem getRemovePlayerItem() {
		return removePlayerItem;
	}
	
	public GameMessage setRemovePlayerItem(RemovePlayerItem removePlayerItem) {
		this.removePlayerItem = removePlayerItem;
		return this;	}
	
		            
		public RequestPlayerItems getRequestPlayerItems() {
		return requestPlayerItems;
	}
	
	public GameMessage setRequestPlayerItems(RequestPlayerItems requestPlayerItems) {
		this.requestPlayerItems = requestPlayerItems;
		return this;	}
	
		            
		public PvpGameMessage getPvpGameMessage() {
		return pvpGameMessage;
	}
	
	public GameMessage setPvpGameMessage(PvpGameMessage pvpGameMessage) {
		this.pvpGameMessage = pvpGameMessage;
		return this;	}
	
		            
		public Harvest getHarvest() {
		return harvest;
	}
	
	public GameMessage setHarvest(Harvest harvest) {
		this.harvest = harvest;
		return this;	}
	
		            
		public long getAuthtoken() {
		return authtoken;
	}
	
	public GameMessage setAuthtoken(long authtoken) {
		this.authtoken = authtoken;
		return this;	}
	
		            
		public long getUnityMessageId() {
		return unityMessageId;
	}
	
	public GameMessage setUnityMessageId(long unityMessageId) {
		this.unityMessageId = unityMessageId;
		return this;	}
	
		            
		public CraftItem getCraftItem() {
		return craftItem;
	}
	
	public GameMessage setCraftItem(CraftItem craftItem) {
		this.craftItem = craftItem;
		return this;	}
	
		            
		public UseSkill getUseSkill() {
		return useSkill;
	}
	
	public GameMessage setUseSkill(UseSkill useSkill) {
		this.useSkill = useSkill;
		return this;	}
	
		            
		public PlayerSkills getPlayerSkills() {
		return playerSkills;
	}
	
	public GameMessage setPlayerSkills(PlayerSkills playerSkills) {
		this.playerSkills = playerSkills;
		return this;	}
	
		            
		public Vitals getVitals() {
		return vitals;
	}
	
	public GameMessage setVitals(Vitals vitals) {
		this.vitals = vitals;
		return this;	}
	
		            
		public Attack getAttack() {
		return attack;
	}
	
	public GameMessage setAttack(Attack attack) {
		this.attack = attack;
		return this;	}
	
		            
		public StatusEffect getStatusEffect() {
		return statusEffect;
	}
	
	public GameMessage setStatusEffect(StatusEffect statusEffect) {
		this.statusEffect = statusEffect;
		return this;	}
	
		            
		public StatusEffectResult getStatusEffectResult() {
		return statusEffectResult;
	}
	
	public GameMessage setStatusEffectResult(StatusEffectResult statusEffectResult) {
		this.statusEffectResult = statusEffectResult;
		return this;	}
	
		            
		public DataRequest getDataRequest() {
		return dataRequest;
	}
	
	public GameMessage setDataRequest(DataRequest dataRequest) {
		this.dataRequest = dataRequest;
		return this;	}
	
		            
		public VisualEffect getVisualEffect() {
		return visualEffect;
	}
	
	public GameMessage setVisualEffect(VisualEffect visualEffect) {
		this.visualEffect = visualEffect;
		return this;	}
	
		            
		public WorldObjects getWorldObjects() {
		return worldObjects;
	}
	
	public GameMessage setWorldObjects(WorldObjects worldObjects) {
		this.worldObjects = worldObjects;
		return this;	}
	
		            
		public SiegeCommand getSiegeCommand() {
		return siegeCommand;
	}
	
	public GameMessage setSiegeCommand(SiegeCommand siegeCommand) {
		this.siegeCommand = siegeCommand;
		return this;	}
	
		            
		public GmBounds getBounds() {
		return bounds;
	}
	
	public GameMessage setBounds(GmBounds bounds) {
		this.bounds = bounds;
		return this;	}
	
		            
		public UseItem getUseItem() {
		return useItem;
	}
	
	public GameMessage setUseItem(UseItem useItem) {
		this.useItem = useItem;
		return this;	}
	
		            
		public TimeCycle getTimeCycle() {
		return timeCycle;
	}
	
	public GameMessage setTimeCycle(TimeCycle timeCycle) {
		this.timeCycle = timeCycle;
		return this;	}
	
		            
		public Guilds getGuilds() {
		return guilds;
	}
	
	public GameMessage setGuilds(Guilds guilds) {
		this.guilds = guilds;
		return this;	}
	
		            
		public GuildAction getGuildAction() {
		return guildAction;
	}
	
	public GameMessage setGuildAction(GuildAction guildAction) {
		this.guildAction = guildAction;
		return this;	}
	
		            
		public GuildMemberList getGuildMemberList() {
		return guildMemberList;
	}
	
	public GameMessage setGuildMemberList(GuildMemberList guildMemberList) {
		this.guildMemberList = guildMemberList;
		return this;	}
	
		            
		public Territory getTerritory() {
		return territory;
	}
	
	public GameMessage setTerritory(Territory territory) {
		this.territory = territory;
		return this;	}
	
		            
		public Territories getTerritories() {
		return territories;
	}
	
	public GameMessage setTerritories(Territories territories) {
		this.territories = territories;
		return this;	}
	
		            
		public EquippedItem getEquippedItem() {
		return equippedItem;
	}
	
	public GameMessage setEquippedItem(EquippedItem equippedItem) {
		this.equippedItem = equippedItem;
		return this;	}
	
		            
		public ComboAttack getComboAttack() {
		return comboAttack;
	}
	
	public GameMessage setComboAttack(ComboAttack comboAttack) {
		this.comboAttack = comboAttack;
		return this;	}
	
		            
		public NpcData getNpcData() {
		return npcData;
	}
	
	public GameMessage setNpcData(NpcData npcData) {
		this.npcData = npcData;
		return this;	}
	
		            
		public BuildObjects getBuildObjects() {
		return buildObjects;
	}
	
	public GameMessage setBuildObjects(BuildObjects buildObjects) {
		this.buildObjects = buildObjects;
		return this;	}
	
		            
		public BuildableAreas getBuildableAreas() {
		return buildableAreas;
	}
	
	public GameMessage setBuildableAreas(BuildableAreas buildableAreas) {
		this.buildableAreas = buildableAreas;
		return this;	}
	
		            
		public GmStats getGmStats() {
		return gmStats;
	}
	
	public GameMessage setGmStats(GmStats gmStats) {
		this.gmStats = gmStats;
		return this;	}
	
		            
		public Zone getZone() {
		return zone;
	}
	
	public GameMessage setZone(Zone zone) {
		this.zone = zone;
		return this;	}
	
		            
		public BuildObjectChunks getBuildObjectChunks() {
		return buildObjectChunks;
	}
	
	public GameMessage setBuildObjectChunks(BuildObjectChunks buildObjectChunks) {
		this.buildObjectChunks = buildObjectChunks;
		return this;	}
	
		            
		public TerrainEdits getTerrainEdits() {
		return terrainEdits;
	}
	
	public GameMessage setTerrainEdits(TerrainEdits terrainEdits) {
		this.terrainEdits = terrainEdits;
		return this;	}
	
		            
		public UpdatePlayerItem getUpdatePlayerItem() {
		return updatePlayerItem;
	}
	
	public GameMessage setUpdatePlayerItem(UpdatePlayerItem updatePlayerItem) {
		this.updatePlayerItem = updatePlayerItem;
		return this;	}
	
	
  
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

    public Schema<GameMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GameMessage newMessage()
    {
        return new GameMessage();
    }

    public Class<GameMessage> typeClass()
    {
        return GameMessage.class;
    }

    public String messageName()
    {
        return GameMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GameMessage.class.getName();
    }

    public boolean isInitialized(GameMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, GameMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.messageId = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.destinationId = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.destination = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.agentId = input.readString();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            	case 7:
            	                	                	message.pathData = input.mergeObject(message.pathData, PathData.getSchema());
                    break;
                                    	
                            	            	case 10:
            	                	                	message.playerItems = input.mergeObject(message.playerItems, PlayerItems.getSchema());
                    break;
                                    	
                            	            	case 11:
            	                	                	message.addPlayerItem = input.mergeObject(message.addPlayerItem, AddPlayerItem.getSchema());
                    break;
                                    	
                            	            	case 12:
            	                	                	message.removePlayerItem = input.mergeObject(message.removePlayerItem, RemovePlayerItem.getSchema());
                    break;
                                    	
                            	            	case 13:
            	                	                	message.requestPlayerItems = input.mergeObject(message.requestPlayerItems, RequestPlayerItems.getSchema());
                    break;
                                    	
                            	            	case 14:
            	                	                	message.pvpGameMessage = input.mergeObject(message.pvpGameMessage, PvpGameMessage.getSchema());
                    break;
                                    	
                            	            	case 15:
            	                	                	message.harvest = input.mergeObject(message.harvest, Harvest.getSchema());
                    break;
                                    	
                            	            	case 16:
            	                	                	message.authtoken = input.readInt64();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.unityMessageId = input.readInt64();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.craftItem = input.mergeObject(message.craftItem, CraftItem.getSchema());
                    break;
                                    	
                            	            	case 19:
            	                	                	message.useSkill = input.mergeObject(message.useSkill, UseSkill.getSchema());
                    break;
                                    	
                            	            	case 20:
            	                	                	message.playerSkills = input.mergeObject(message.playerSkills, PlayerSkills.getSchema());
                    break;
                                    	
                            	            	case 21:
            	                	                	message.vitals = input.mergeObject(message.vitals, Vitals.getSchema());
                    break;
                                    	
                            	            	case 22:
            	                	                	message.attack = input.mergeObject(message.attack, Attack.getSchema());
                    break;
                                    	
                            	            	case 23:
            	                	                	message.statusEffect = input.mergeObject(message.statusEffect, StatusEffect.getSchema());
                    break;
                                    	
                            	            	case 24:
            	                	                	message.statusEffectResult = input.mergeObject(message.statusEffectResult, StatusEffectResult.getSchema());
                    break;
                                    	
                            	            	case 25:
            	                	                	message.dataRequest = input.mergeObject(message.dataRequest, DataRequest.getSchema());
                    break;
                                    	
                            	            	case 26:
            	                	                	message.visualEffect = input.mergeObject(message.visualEffect, VisualEffect.getSchema());
                    break;
                                    	
                            	            	case 27:
            	                	                	message.worldObjects = input.mergeObject(message.worldObjects, WorldObjects.getSchema());
                    break;
                                    	
                            	            	case 28:
            	                	                	message.siegeCommand = input.mergeObject(message.siegeCommand, SiegeCommand.getSchema());
                    break;
                                    	
                            	            	case 29:
            	                	                	message.bounds = input.mergeObject(message.bounds, GmBounds.getSchema());
                    break;
                                    	
                            	            	case 30:
            	                	                	message.useItem = input.mergeObject(message.useItem, UseItem.getSchema());
                    break;
                                    	
                            	            	case 31:
            	                	                	message.timeCycle = input.mergeObject(message.timeCycle, TimeCycle.getSchema());
                    break;
                                    	
                            	            	case 32:
            	                	                	message.guilds = input.mergeObject(message.guilds, Guilds.getSchema());
                    break;
                                    	
                            	            	case 33:
            	                	                	message.guildAction = input.mergeObject(message.guildAction, GuildAction.getSchema());
                    break;
                                    	
                            	            	case 34:
            	                	                	message.guildMemberList = input.mergeObject(message.guildMemberList, GuildMemberList.getSchema());
                    break;
                                    	
                            	            	case 35:
            	                	                	message.territory = input.mergeObject(message.territory, Territory.getSchema());
                    break;
                                    	
                            	            	case 36:
            	                	                	message.territories = input.mergeObject(message.territories, Territories.getSchema());
                    break;
                                    	
                            	            	case 37:
            	                	                	message.equippedItem = input.mergeObject(message.equippedItem, EquippedItem.getSchema());
                    break;
                                    	
                            	            	case 38:
            	                	                	message.comboAttack = input.mergeObject(message.comboAttack, ComboAttack.getSchema());
                    break;
                                    	
                            	            	case 39:
            	                	                	message.npcData = input.mergeObject(message.npcData, NpcData.getSchema());
                    break;
                                    	
                            	            	case 40:
            	                	                	message.buildObjects = input.mergeObject(message.buildObjects, BuildObjects.getSchema());
                    break;
                                    	
                            	            	case 41:
            	                	                	message.buildableAreas = input.mergeObject(message.buildableAreas, BuildableAreas.getSchema());
                    break;
                                    	
                            	            	case 42:
            	                	                	message.gmStats = input.mergeObject(message.gmStats, GmStats.getSchema());
                    break;
                                    	
                            	            	case 43:
            	                	                	message.zone = input.mergeObject(message.zone, Zone.getSchema());
                    break;
                                    	
                            	            	case 44:
            	                	                	message.buildObjectChunks = input.mergeObject(message.buildObjectChunks, BuildObjectChunks.getSchema());
                    break;
                                    	
                            	            	case 45:
            	                	                	message.terrainEdits = input.mergeObject(message.terrainEdits, TerrainEdits.getSchema());
                    break;
                                    	
                            	            	case 46:
            	                	                	message.updatePlayerItem = input.mergeObject(message.updatePlayerItem, UpdatePlayerItem.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GameMessage message) throws IOException
    {
    	    	
    	    	
    	    	    	if( (String)message.playerId != null) {
            output.writeString(1, message.playerId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.messageId != null) {
            output.writeString(2, message.messageId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.destinationId != null) {
            output.writeInt32(3, message.destinationId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.destination != null) {
            output.writeString(4, message.destination, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (String)message.agentId != null) {
            output.writeString(5, message.agentId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(6, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.pathData != null)
    		output.writeObject(7, message.pathData, PathData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerItems != null)
    		output.writeObject(10, message.playerItems, PlayerItems.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.addPlayerItem != null)
    		output.writeObject(11, message.addPlayerItem, AddPlayerItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.removePlayerItem != null)
    		output.writeObject(12, message.removePlayerItem, RemovePlayerItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.requestPlayerItems != null)
    		output.writeObject(13, message.requestPlayerItems, RequestPlayerItems.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.pvpGameMessage != null)
    		output.writeObject(14, message.pvpGameMessage, PvpGameMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.harvest != null)
    		output.writeObject(15, message.harvest, Harvest.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.authtoken != null) {
            output.writeInt64(16, message.authtoken, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Long)message.unityMessageId != null) {
            output.writeInt64(17, message.unityMessageId, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.craftItem != null)
    		output.writeObject(18, message.craftItem, CraftItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.useSkill != null)
    		output.writeObject(19, message.useSkill, UseSkill.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerSkills != null)
    		output.writeObject(20, message.playerSkills, PlayerSkills.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.vitals != null)
    		output.writeObject(21, message.vitals, Vitals.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.attack != null)
    		output.writeObject(22, message.attack, Attack.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.statusEffect != null)
    		output.writeObject(23, message.statusEffect, StatusEffect.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.statusEffectResult != null)
    		output.writeObject(24, message.statusEffectResult, StatusEffectResult.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.dataRequest != null)
    		output.writeObject(25, message.dataRequest, DataRequest.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.visualEffect != null)
    		output.writeObject(26, message.visualEffect, VisualEffect.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.worldObjects != null)
    		output.writeObject(27, message.worldObjects, WorldObjects.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.siegeCommand != null)
    		output.writeObject(28, message.siegeCommand, SiegeCommand.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.bounds != null)
    		output.writeObject(29, message.bounds, GmBounds.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.useItem != null)
    		output.writeObject(30, message.useItem, UseItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.timeCycle != null)
    		output.writeObject(31, message.timeCycle, TimeCycle.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.guilds != null)
    		output.writeObject(32, message.guilds, Guilds.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.guildAction != null)
    		output.writeObject(33, message.guildAction, GuildAction.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.guildMemberList != null)
    		output.writeObject(34, message.guildMemberList, GuildMemberList.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.territory != null)
    		output.writeObject(35, message.territory, Territory.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.territories != null)
    		output.writeObject(36, message.territories, Territories.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.equippedItem != null)
    		output.writeObject(37, message.equippedItem, EquippedItem.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.comboAttack != null)
    		output.writeObject(38, message.comboAttack, ComboAttack.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.npcData != null)
    		output.writeObject(39, message.npcData, NpcData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.buildObjects != null)
    		output.writeObject(40, message.buildObjects, BuildObjects.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.buildableAreas != null)
    		output.writeObject(41, message.buildableAreas, BuildableAreas.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.gmStats != null)
    		output.writeObject(42, message.gmStats, GmStats.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.zone != null)
    		output.writeObject(43, message.zone, Zone.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.buildObjectChunks != null)
    		output.writeObject(44, message.buildObjectChunks, BuildObjectChunks.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.terrainEdits != null)
    		output.writeObject(45, message.terrainEdits, TerrainEdits.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.updatePlayerItem != null)
    		output.writeObject(46, message.updatePlayerItem, UpdatePlayerItem.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START GameMessage");
    	    	//if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	//}
    	    	//if(this.messageId != null) {
    		System.out.println("messageId="+this.messageId);
    	//}
    	    	//if(this.destinationId != null) {
    		System.out.println("destinationId="+this.destinationId);
    	//}
    	    	//if(this.destination != null) {
    		System.out.println("destination="+this.destination);
    	//}
    	    	//if(this.agentId != null) {
    		System.out.println("agentId="+this.agentId);
    	//}
    	    	//if(this.dynamicMessage != null) {
    		System.out.println("dynamicMessage="+this.dynamicMessage);
    	//}
    	    	//if(this.pathData != null) {
    		System.out.println("pathData="+this.pathData);
    	//}
    	    	//if(this.playerItems != null) {
    		System.out.println("playerItems="+this.playerItems);
    	//}
    	    	//if(this.addPlayerItem != null) {
    		System.out.println("addPlayerItem="+this.addPlayerItem);
    	//}
    	    	//if(this.removePlayerItem != null) {
    		System.out.println("removePlayerItem="+this.removePlayerItem);
    	//}
    	    	//if(this.requestPlayerItems != null) {
    		System.out.println("requestPlayerItems="+this.requestPlayerItems);
    	//}
    	    	//if(this.pvpGameMessage != null) {
    		System.out.println("pvpGameMessage="+this.pvpGameMessage);
    	//}
    	    	//if(this.harvest != null) {
    		System.out.println("harvest="+this.harvest);
    	//}
    	    	//if(this.authtoken != null) {
    		System.out.println("authtoken="+this.authtoken);
    	//}
    	    	//if(this.unityMessageId != null) {
    		System.out.println("unityMessageId="+this.unityMessageId);
    	//}
    	    	//if(this.craftItem != null) {
    		System.out.println("craftItem="+this.craftItem);
    	//}
    	    	//if(this.useSkill != null) {
    		System.out.println("useSkill="+this.useSkill);
    	//}
    	    	//if(this.playerSkills != null) {
    		System.out.println("playerSkills="+this.playerSkills);
    	//}
    	    	//if(this.vitals != null) {
    		System.out.println("vitals="+this.vitals);
    	//}
    	    	//if(this.attack != null) {
    		System.out.println("attack="+this.attack);
    	//}
    	    	//if(this.statusEffect != null) {
    		System.out.println("statusEffect="+this.statusEffect);
    	//}
    	    	//if(this.statusEffectResult != null) {
    		System.out.println("statusEffectResult="+this.statusEffectResult);
    	//}
    	    	//if(this.dataRequest != null) {
    		System.out.println("dataRequest="+this.dataRequest);
    	//}
    	    	//if(this.visualEffect != null) {
    		System.out.println("visualEffect="+this.visualEffect);
    	//}
    	    	//if(this.worldObjects != null) {
    		System.out.println("worldObjects="+this.worldObjects);
    	//}
    	    	//if(this.siegeCommand != null) {
    		System.out.println("siegeCommand="+this.siegeCommand);
    	//}
    	    	//if(this.bounds != null) {
    		System.out.println("bounds="+this.bounds);
    	//}
    	    	//if(this.useItem != null) {
    		System.out.println("useItem="+this.useItem);
    	//}
    	    	//if(this.timeCycle != null) {
    		System.out.println("timeCycle="+this.timeCycle);
    	//}
    	    	//if(this.guilds != null) {
    		System.out.println("guilds="+this.guilds);
    	//}
    	    	//if(this.guildAction != null) {
    		System.out.println("guildAction="+this.guildAction);
    	//}
    	    	//if(this.guildMemberList != null) {
    		System.out.println("guildMemberList="+this.guildMemberList);
    	//}
    	    	//if(this.territory != null) {
    		System.out.println("territory="+this.territory);
    	//}
    	    	//if(this.territories != null) {
    		System.out.println("territories="+this.territories);
    	//}
    	    	//if(this.equippedItem != null) {
    		System.out.println("equippedItem="+this.equippedItem);
    	//}
    	    	//if(this.comboAttack != null) {
    		System.out.println("comboAttack="+this.comboAttack);
    	//}
    	    	//if(this.npcData != null) {
    		System.out.println("npcData="+this.npcData);
    	//}
    	    	//if(this.buildObjects != null) {
    		System.out.println("buildObjects="+this.buildObjects);
    	//}
    	    	//if(this.buildableAreas != null) {
    		System.out.println("buildableAreas="+this.buildableAreas);
    	//}
    	    	//if(this.gmStats != null) {
    		System.out.println("gmStats="+this.gmStats);
    	//}
    	    	//if(this.zone != null) {
    		System.out.println("zone="+this.zone);
    	//}
    	    	//if(this.buildObjectChunks != null) {
    		System.out.println("buildObjectChunks="+this.buildObjectChunks);
    	//}
    	    	//if(this.terrainEdits != null) {
    		System.out.println("terrainEdits="+this.terrainEdits);
    	//}
    	    	//if(this.updatePlayerItem != null) {
    		System.out.println("updatePlayerItem="+this.updatePlayerItem);
    	//}
    	    	System.out.println("END GameMessage");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "playerId";
        	        	case 2: return "messageId";
        	        	case 3: return "destinationId";
        	        	case 4: return "destination";
        	        	case 5: return "agentId";
        	        	case 6: return "dynamicMessage";
        	        	case 7: return "pathData";
        	        	case 10: return "playerItems";
        	        	case 11: return "addPlayerItem";
        	        	case 12: return "removePlayerItem";
        	        	case 13: return "requestPlayerItems";
        	        	case 14: return "pvpGameMessage";
        	        	case 15: return "harvest";
        	        	case 16: return "authtoken";
        	        	case 17: return "unityMessageId";
        	        	case 18: return "craftItem";
        	        	case 19: return "useSkill";
        	        	case 20: return "playerSkills";
        	        	case 21: return "vitals";
        	        	case 22: return "attack";
        	        	case 23: return "statusEffect";
        	        	case 24: return "statusEffectResult";
        	        	case 25: return "dataRequest";
        	        	case 26: return "visualEffect";
        	        	case 27: return "worldObjects";
        	        	case 28: return "siegeCommand";
        	        	case 29: return "bounds";
        	        	case 30: return "useItem";
        	        	case 31: return "timeCycle";
        	        	case 32: return "guilds";
        	        	case 33: return "guildAction";
        	        	case 34: return "guildMemberList";
        	        	case 35: return "territory";
        	        	case 36: return "territories";
        	        	case 37: return "equippedItem";
        	        	case 38: return "comboAttack";
        	        	case 39: return "npcData";
        	        	case 40: return "buildObjects";
        	        	case 41: return "buildableAreas";
        	        	case 42: return "gmStats";
        	        	case 43: return "zone";
        	        	case 44: return "buildObjectChunks";
        	        	case 45: return "terrainEdits";
        	        	case 46: return "updatePlayerItem";
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
    	    	__fieldMap.put("playerId", 1);
    	    	__fieldMap.put("messageId", 2);
    	    	__fieldMap.put("destinationId", 3);
    	    	__fieldMap.put("destination", 4);
    	    	__fieldMap.put("agentId", 5);
    	    	__fieldMap.put("dynamicMessage", 6);
    	    	__fieldMap.put("pathData", 7);
    	    	__fieldMap.put("playerItems", 10);
    	    	__fieldMap.put("addPlayerItem", 11);
    	    	__fieldMap.put("removePlayerItem", 12);
    	    	__fieldMap.put("requestPlayerItems", 13);
    	    	__fieldMap.put("pvpGameMessage", 14);
    	    	__fieldMap.put("harvest", 15);
    	    	__fieldMap.put("authtoken", 16);
    	    	__fieldMap.put("unityMessageId", 17);
    	    	__fieldMap.put("craftItem", 18);
    	    	__fieldMap.put("useSkill", 19);
    	    	__fieldMap.put("playerSkills", 20);
    	    	__fieldMap.put("vitals", 21);
    	    	__fieldMap.put("attack", 22);
    	    	__fieldMap.put("statusEffect", 23);
    	    	__fieldMap.put("statusEffectResult", 24);
    	    	__fieldMap.put("dataRequest", 25);
    	    	__fieldMap.put("visualEffect", 26);
    	    	__fieldMap.put("worldObjects", 27);
    	    	__fieldMap.put("siegeCommand", 28);
    	    	__fieldMap.put("bounds", 29);
    	    	__fieldMap.put("useItem", 30);
    	    	__fieldMap.put("timeCycle", 31);
    	    	__fieldMap.put("guilds", 32);
    	    	__fieldMap.put("guildAction", 33);
    	    	__fieldMap.put("guildMemberList", 34);
    	    	__fieldMap.put("territory", 35);
    	    	__fieldMap.put("territories", 36);
    	    	__fieldMap.put("equippedItem", 37);
    	    	__fieldMap.put("comboAttack", 38);
    	    	__fieldMap.put("npcData", 39);
    	    	__fieldMap.put("buildObjects", 40);
    	    	__fieldMap.put("buildableAreas", 41);
    	    	__fieldMap.put("gmStats", 42);
    	    	__fieldMap.put("zone", 43);
    	    	__fieldMap.put("buildObjectChunks", 44);
    	    	__fieldMap.put("terrainEdits", 45);
    	    	__fieldMap.put("updatePlayerItem", 46);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GameMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GameMessage parseFrom(byte[] bytes) {
	GameMessage message = new GameMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, GameMessage.getSchema());
	return message;
}

public static GameMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GameMessage message = new GameMessage();
	JsonIOUtil.mergeFrom(bytes, message, GameMessage.getSchema(), false);
	return message;
}

public GameMessage clone() {
	byte[] bytes = this.toByteArray();
	GameMessage gameMessage = GameMessage.parseFrom(bytes);
	return gameMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GameMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GameMessage> schema = GameMessage.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, GameMessage.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, GameMessage.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
