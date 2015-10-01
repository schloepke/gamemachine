
package io.gamemachine.client.messages;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
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


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class GameMessage implements Externalizable, Message<GameMessage>, Schema<GameMessage>{



    public static Schema<GameMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GameMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GameMessage DEFAULT_INSTANCE = new GameMessage();

    			public String playerId;
	    
        			public String messageId;
	    
        			public Integer destinationId;
	    
        			public String destination;
	    
        			public String agentId;
	    
        			public DynamicMessage dynamicMessage;
	    
        			public PathData pathData;
	    
        			public PlayerItems playerItems;
	    
        			public AddPlayerItem addPlayerItem;
	    
        			public RemovePlayerItem removePlayerItem;
	    
        			public RequestPlayerItems requestPlayerItems;
	    
        			public PvpGameMessage pvpGameMessage;
	    
        			public Harvest harvest;
	    
        			public Long authtoken;
	    
        			public Long unityMessageId;
	    
        			public CraftItem craftItem;
	    
        			public UseSkill useSkill;
	    
        			public PlayerSkills playerSkills;
	    
        			public Vitals vitals;
	    
        			public Attack attack;
	    
        			public StatusEffect statusEffect;
	    
        			public StatusEffectResult statusEffectResult;
	    
        			public DataRequest dataRequest;
	    
        			public VisualEffect visualEffect;
	    
        			public WorldObjects worldObjects;
	    
        			public SiegeCommand siegeCommand;
	    
        			public GmBounds bounds;
	    
        			public UseItem useItem;
	    
        			public TimeCycle timeCycle;
	    
        			public Guilds guilds;
	    
        			public GuildAction guildAction;
	    
        			public GuildMemberList guildMemberList;
	    
        			public Territory territory;
	    
        			public Territories territories;
	    
        			public EquippedItem equippedItem;
	    
        			public ComboAttack comboAttack;
	    
        			public NpcData npcData;
	    
        			public BuildObjects buildObjects;
	    
        			public BuildableAreas buildableAreas;
	    
        			public GmStats gmStats;
	    
        			public Zone zone;
	    
        			public BuildObjectChunks buildObjectChunks;
	    
        			public TerrainEdits terrainEdits;
	    
        			public UpdatePlayerItem updatePlayerItem;
	    
      
    public GameMessage()
    {
        
    }


	

	    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public GameMessage setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasMessageId()  {
        return messageId == null ? false : true;
    }
        
		public String getMessageId() {
		return messageId;
	}
	
	public GameMessage setMessageId(String messageId) {
		this.messageId = messageId;
		return this;	}
	
		    
    public Boolean hasDestinationId()  {
        return destinationId == null ? false : true;
    }
        
		public Integer getDestinationId() {
		return destinationId;
	}
	
	public GameMessage setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
		return this;	}
	
		    
    public Boolean hasDestination()  {
        return destination == null ? false : true;
    }
        
		public String getDestination() {
		return destination;
	}
	
	public GameMessage setDestination(String destination) {
		this.destination = destination;
		return this;	}
	
		    
    public Boolean hasAgentId()  {
        return agentId == null ? false : true;
    }
        
		public String getAgentId() {
		return agentId;
	}
	
	public GameMessage setAgentId(String agentId) {
		this.agentId = agentId;
		return this;	}
	
		    
    public Boolean hasDynamicMessage()  {
        return dynamicMessage == null ? false : true;
    }
        
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public GameMessage setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;	}
	
		    
    public Boolean hasPathData()  {
        return pathData == null ? false : true;
    }
        
		public PathData getPathData() {
		return pathData;
	}
	
	public GameMessage setPathData(PathData pathData) {
		this.pathData = pathData;
		return this;	}
	
		    
    public Boolean hasPlayerItems()  {
        return playerItems == null ? false : true;
    }
        
		public PlayerItems getPlayerItems() {
		return playerItems;
	}
	
	public GameMessage setPlayerItems(PlayerItems playerItems) {
		this.playerItems = playerItems;
		return this;	}
	
		    
    public Boolean hasAddPlayerItem()  {
        return addPlayerItem == null ? false : true;
    }
        
		public AddPlayerItem getAddPlayerItem() {
		return addPlayerItem;
	}
	
	public GameMessage setAddPlayerItem(AddPlayerItem addPlayerItem) {
		this.addPlayerItem = addPlayerItem;
		return this;	}
	
		    
    public Boolean hasRemovePlayerItem()  {
        return removePlayerItem == null ? false : true;
    }
        
		public RemovePlayerItem getRemovePlayerItem() {
		return removePlayerItem;
	}
	
	public GameMessage setRemovePlayerItem(RemovePlayerItem removePlayerItem) {
		this.removePlayerItem = removePlayerItem;
		return this;	}
	
		    
    public Boolean hasRequestPlayerItems()  {
        return requestPlayerItems == null ? false : true;
    }
        
		public RequestPlayerItems getRequestPlayerItems() {
		return requestPlayerItems;
	}
	
	public GameMessage setRequestPlayerItems(RequestPlayerItems requestPlayerItems) {
		this.requestPlayerItems = requestPlayerItems;
		return this;	}
	
		    
    public Boolean hasPvpGameMessage()  {
        return pvpGameMessage == null ? false : true;
    }
        
		public PvpGameMessage getPvpGameMessage() {
		return pvpGameMessage;
	}
	
	public GameMessage setPvpGameMessage(PvpGameMessage pvpGameMessage) {
		this.pvpGameMessage = pvpGameMessage;
		return this;	}
	
		    
    public Boolean hasHarvest()  {
        return harvest == null ? false : true;
    }
        
		public Harvest getHarvest() {
		return harvest;
	}
	
	public GameMessage setHarvest(Harvest harvest) {
		this.harvest = harvest;
		return this;	}
	
		    
    public Boolean hasAuthtoken()  {
        return authtoken == null ? false : true;
    }
        
		public Long getAuthtoken() {
		return authtoken;
	}
	
	public GameMessage setAuthtoken(Long authtoken) {
		this.authtoken = authtoken;
		return this;	}
	
		    
    public Boolean hasUnityMessageId()  {
        return unityMessageId == null ? false : true;
    }
        
		public Long getUnityMessageId() {
		return unityMessageId;
	}
	
	public GameMessage setUnityMessageId(Long unityMessageId) {
		this.unityMessageId = unityMessageId;
		return this;	}
	
		    
    public Boolean hasCraftItem()  {
        return craftItem == null ? false : true;
    }
        
		public CraftItem getCraftItem() {
		return craftItem;
	}
	
	public GameMessage setCraftItem(CraftItem craftItem) {
		this.craftItem = craftItem;
		return this;	}
	
		    
    public Boolean hasUseSkill()  {
        return useSkill == null ? false : true;
    }
        
		public UseSkill getUseSkill() {
		return useSkill;
	}
	
	public GameMessage setUseSkill(UseSkill useSkill) {
		this.useSkill = useSkill;
		return this;	}
	
		    
    public Boolean hasPlayerSkills()  {
        return playerSkills == null ? false : true;
    }
        
		public PlayerSkills getPlayerSkills() {
		return playerSkills;
	}
	
	public GameMessage setPlayerSkills(PlayerSkills playerSkills) {
		this.playerSkills = playerSkills;
		return this;	}
	
		    
    public Boolean hasVitals()  {
        return vitals == null ? false : true;
    }
        
		public Vitals getVitals() {
		return vitals;
	}
	
	public GameMessage setVitals(Vitals vitals) {
		this.vitals = vitals;
		return this;	}
	
		    
    public Boolean hasAttack()  {
        return attack == null ? false : true;
    }
        
		public Attack getAttack() {
		return attack;
	}
	
	public GameMessage setAttack(Attack attack) {
		this.attack = attack;
		return this;	}
	
		    
    public Boolean hasStatusEffect()  {
        return statusEffect == null ? false : true;
    }
        
		public StatusEffect getStatusEffect() {
		return statusEffect;
	}
	
	public GameMessage setStatusEffect(StatusEffect statusEffect) {
		this.statusEffect = statusEffect;
		return this;	}
	
		    
    public Boolean hasStatusEffectResult()  {
        return statusEffectResult == null ? false : true;
    }
        
		public StatusEffectResult getStatusEffectResult() {
		return statusEffectResult;
	}
	
	public GameMessage setStatusEffectResult(StatusEffectResult statusEffectResult) {
		this.statusEffectResult = statusEffectResult;
		return this;	}
	
		    
    public Boolean hasDataRequest()  {
        return dataRequest == null ? false : true;
    }
        
		public DataRequest getDataRequest() {
		return dataRequest;
	}
	
	public GameMessage setDataRequest(DataRequest dataRequest) {
		this.dataRequest = dataRequest;
		return this;	}
	
		    
    public Boolean hasVisualEffect()  {
        return visualEffect == null ? false : true;
    }
        
		public VisualEffect getVisualEffect() {
		return visualEffect;
	}
	
	public GameMessage setVisualEffect(VisualEffect visualEffect) {
		this.visualEffect = visualEffect;
		return this;	}
	
		    
    public Boolean hasWorldObjects()  {
        return worldObjects == null ? false : true;
    }
        
		public WorldObjects getWorldObjects() {
		return worldObjects;
	}
	
	public GameMessage setWorldObjects(WorldObjects worldObjects) {
		this.worldObjects = worldObjects;
		return this;	}
	
		    
    public Boolean hasSiegeCommand()  {
        return siegeCommand == null ? false : true;
    }
        
		public SiegeCommand getSiegeCommand() {
		return siegeCommand;
	}
	
	public GameMessage setSiegeCommand(SiegeCommand siegeCommand) {
		this.siegeCommand = siegeCommand;
		return this;	}
	
		    
    public Boolean hasBounds()  {
        return bounds == null ? false : true;
    }
        
		public GmBounds getBounds() {
		return bounds;
	}
	
	public GameMessage setBounds(GmBounds bounds) {
		this.bounds = bounds;
		return this;	}
	
		    
    public Boolean hasUseItem()  {
        return useItem == null ? false : true;
    }
        
		public UseItem getUseItem() {
		return useItem;
	}
	
	public GameMessage setUseItem(UseItem useItem) {
		this.useItem = useItem;
		return this;	}
	
		    
    public Boolean hasTimeCycle()  {
        return timeCycle == null ? false : true;
    }
        
		public TimeCycle getTimeCycle() {
		return timeCycle;
	}
	
	public GameMessage setTimeCycle(TimeCycle timeCycle) {
		this.timeCycle = timeCycle;
		return this;	}
	
		    
    public Boolean hasGuilds()  {
        return guilds == null ? false : true;
    }
        
		public Guilds getGuilds() {
		return guilds;
	}
	
	public GameMessage setGuilds(Guilds guilds) {
		this.guilds = guilds;
		return this;	}
	
		    
    public Boolean hasGuildAction()  {
        return guildAction == null ? false : true;
    }
        
		public GuildAction getGuildAction() {
		return guildAction;
	}
	
	public GameMessage setGuildAction(GuildAction guildAction) {
		this.guildAction = guildAction;
		return this;	}
	
		    
    public Boolean hasGuildMemberList()  {
        return guildMemberList == null ? false : true;
    }
        
		public GuildMemberList getGuildMemberList() {
		return guildMemberList;
	}
	
	public GameMessage setGuildMemberList(GuildMemberList guildMemberList) {
		this.guildMemberList = guildMemberList;
		return this;	}
	
		    
    public Boolean hasTerritory()  {
        return territory == null ? false : true;
    }
        
		public Territory getTerritory() {
		return territory;
	}
	
	public GameMessage setTerritory(Territory territory) {
		this.territory = territory;
		return this;	}
	
		    
    public Boolean hasTerritories()  {
        return territories == null ? false : true;
    }
        
		public Territories getTerritories() {
		return territories;
	}
	
	public GameMessage setTerritories(Territories territories) {
		this.territories = territories;
		return this;	}
	
		    
    public Boolean hasEquippedItem()  {
        return equippedItem == null ? false : true;
    }
        
		public EquippedItem getEquippedItem() {
		return equippedItem;
	}
	
	public GameMessage setEquippedItem(EquippedItem equippedItem) {
		this.equippedItem = equippedItem;
		return this;	}
	
		    
    public Boolean hasComboAttack()  {
        return comboAttack == null ? false : true;
    }
        
		public ComboAttack getComboAttack() {
		return comboAttack;
	}
	
	public GameMessage setComboAttack(ComboAttack comboAttack) {
		this.comboAttack = comboAttack;
		return this;	}
	
		    
    public Boolean hasNpcData()  {
        return npcData == null ? false : true;
    }
        
		public NpcData getNpcData() {
		return npcData;
	}
	
	public GameMessage setNpcData(NpcData npcData) {
		this.npcData = npcData;
		return this;	}
	
		    
    public Boolean hasBuildObjects()  {
        return buildObjects == null ? false : true;
    }
        
		public BuildObjects getBuildObjects() {
		return buildObjects;
	}
	
	public GameMessage setBuildObjects(BuildObjects buildObjects) {
		this.buildObjects = buildObjects;
		return this;	}
	
		    
    public Boolean hasBuildableAreas()  {
        return buildableAreas == null ? false : true;
    }
        
		public BuildableAreas getBuildableAreas() {
		return buildableAreas;
	}
	
	public GameMessage setBuildableAreas(BuildableAreas buildableAreas) {
		this.buildableAreas = buildableAreas;
		return this;	}
	
		    
    public Boolean hasGmStats()  {
        return gmStats == null ? false : true;
    }
        
		public GmStats getGmStats() {
		return gmStats;
	}
	
	public GameMessage setGmStats(GmStats gmStats) {
		this.gmStats = gmStats;
		return this;	}
	
		    
    public Boolean hasZone()  {
        return zone == null ? false : true;
    }
        
		public Zone getZone() {
		return zone;
	}
	
	public GameMessage setZone(Zone zone) {
		this.zone = zone;
		return this;	}
	
		    
    public Boolean hasBuildObjectChunks()  {
        return buildObjectChunks == null ? false : true;
    }
        
		public BuildObjectChunks getBuildObjectChunks() {
		return buildObjectChunks;
	}
	
	public GameMessage setBuildObjectChunks(BuildObjectChunks buildObjectChunks) {
		this.buildObjectChunks = buildObjectChunks;
		return this;	}
	
		    
    public Boolean hasTerrainEdits()  {
        return terrainEdits == null ? false : true;
    }
        
		public TerrainEdits getTerrainEdits() {
		return terrainEdits;
	}
	
	public GameMessage setTerrainEdits(TerrainEdits terrainEdits) {
		this.terrainEdits = terrainEdits;
		return this;	}
	
		    
    public Boolean hasUpdatePlayerItem()  {
        return updatePlayerItem == null ? false : true;
    }
        
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
    	    	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(1, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.messageId != null)
            output.writeString(2, message.messageId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.destinationId != null)
            output.writeInt32(3, message.destinationId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.destination != null)
            output.writeString(4, message.destination, false);
    	    	
    	            	
    	    	
    	    	    	if(message.agentId != null)
            output.writeString(5, message.agentId, false);
    	    	
    	            	
    	    	
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
    	    	
    	            	
    	    	
    	    	    	if(message.authtoken != null)
            output.writeInt64(16, message.authtoken, false);
    	    	
    	            	
    	    	
    	    	    	if(message.unityMessageId != null)
            output.writeInt64(17, message.unityMessageId, false);
    	    	
    	            	
    	    	
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
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
