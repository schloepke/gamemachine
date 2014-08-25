
package GameMachine.Messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import GameMachine.Messages.Entity;
import com.game_machine.core.LocalLinkedBuffer;

import com.game_machine.core.PersistentMessage;
import com.game_machine.core.ActorUtil;
import akka.actor.ActorSelection;
import org.javalite.activejdbc.Errors;

import org.javalite.activejdbc.Model;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class PlayerItem  implements Externalizable, Message<PlayerItem>, Schema<PlayerItem>, PersistentMessage

{

    public static Schema<PlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerItem DEFAULT_INSTANCE = new PlayerItem();

	public Errors ormErrors;
	public String persistPlayerId;
	public String getPersistPlayerId() {
		return persistPlayerId;
	}
	
	public String persistAction;
	public String getPersistAction() {
		return persistAction;
	}

		public String id;

		public String name;

		public Integer quantity;

		public String color;

		public Weapon weapon;

		public Consumable consumable;

		public Cost cost;

    public PlayerItem()
    {
        
    }

	public void ormSaveAsync(String playerId) {
		persistPlayerId = playerId;
		persistAction = "save";
		ActorSelection sel = ActorUtil.getSelectionByName("message_persister");
		sel.tell(this, null);
    }
        
    public Boolean ormSave(String playerId) {
    	return ormSave(playerId,false);
    }
        
    public Boolean ormSave(String playerId, boolean inTransaction) {
    	if (!inTransaction) {
    		com.game_machine.orm.models.PlayerItem.open();
    	}
    	
    	com.game_machine.orm.models.PlayerItem model = com.game_machine.orm.models.PlayerItem.findFirst("player_item_id = ? and player_id = ?", this.id, playerId);
    	if (model == null) {
    		model = new com.game_machine.orm.models.PlayerItem();
    		toModel(model,playerId);
    	} else {
    		toModel(model,null);
    	}
    	
    	if (this.hasWeapon()) {
    		this.weapon.toModel(model,null);
    	} else {
    		Weapon.clearModel(model);
    	}
    	
    	if (this.hasConsumable()) {
    		this.consumable.toModel(model,null);
    	} else {
    		Consumable.clearModel(model);
    	}
    	
    	if (this.hasCost()) {
    		this.cost.toModel(model,null);
    	} else {
    		Cost.clearModel(model);
    	}

    	Boolean res = model.save();
    	if (!res) {
    		ormErrors = model.errors();
    	}
    	if (!inTransaction) {
    		com.game_machine.orm.models.PlayerItem.close();
    	}
    	return res;
    }
    
    public static void ormDeleteAsync(String id, String playerId) {
    	PlayerItem message = new PlayerItem();
    	message.setId(id);
		message.persistPlayerId = playerId;
		message.persistAction = "delete";
		ActorSelection sel = ActorUtil.getSelectionByName("message_persister");
		sel.tell(message, null);
    }
    
    public Boolean ormDelete(String playerId) {
    	Boolean result;
    	com.game_machine.orm.models.PlayerItem.open();
    	com.game_machine.orm.models.PlayerItem model = com.game_machine.orm.models.PlayerItem.findFirst("player_item_id = ? and player_id = ?", this.id, playerId);
    	if (model != null) {
    		result = model.delete();
    	} else {
    		result = false;
    	}
    	com.game_machine.orm.models.PlayerItem.close();
    	return result;
    }
    
    public static PlayerItem ormFind(String id, String playerId) {
    	return ormFind(id, playerId, false);
    }
    
    public static PlayerItem ormFind(String id, String playerId, boolean inTransaction) {
    	if (!inTransaction) {
    		com.game_machine.orm.models.PlayerItem.open();
    	}
    	
    	com.game_machine.orm.models.PlayerItem model = com.game_machine.orm.models.PlayerItem.findFirst("player_item_id = ? and player_id = ?", id, playerId);
    	
    	if (!inTransaction) {
    		com.game_machine.orm.models.PlayerItem.close();
    	}
    	
    	if (model == null) {
    		return null;
    	} else {
    		PlayerItem playerItem = fromModel(model);
    		
    		playerItem.weapon = Weapon.fromModel(model);
    		
    		playerItem.consumable = Consumable.fromModel(model);
    		
    		playerItem.cost = Cost.fromModel(model);
    		
    		return playerItem;
    	}
    }
    
    public static List<PlayerItem> ormFindAll(String playerId) {
    	com.game_machine.orm.models.PlayerItem.open();
    	List<com.game_machine.orm.models.PlayerItem> models = com.game_machine.orm.models.PlayerItem.where("player_id = ?", playerId);
    	List<PlayerItem> messages = new ArrayList<PlayerItem>();
    	for (com.game_machine.orm.models.PlayerItem model : models) {
    		PlayerItem playerItem = fromModel(model);
    		
    		playerItem.weapon = Weapon.fromModel(model);
    		
    		playerItem.consumable = Consumable.fromModel(model);
    		
    		playerItem.cost = Cost.fromModel(model);
    		
    		messages.add(playerItem);
    	}
    	com.game_machine.orm.models.PlayerItem.close();
    	return messages;
    }
    
    public static List<PlayerItem> ormWhere(String query, Object ... params) {
    	com.game_machine.orm.models.PlayerItem.open();
    	List<com.game_machine.orm.models.PlayerItem> models = com.game_machine.orm.models.PlayerItem.where(query, params);
    	List<PlayerItem> messages = new ArrayList<PlayerItem>();
    	for (com.game_machine.orm.models.PlayerItem model : models) {
    		PlayerItem playerItem = fromModel(model);
    		
    		playerItem.weapon = Weapon.fromModel(model);
    		
    		playerItem.consumable = Consumable.fromModel(model);
    		
    		playerItem.cost = Cost.fromModel(model);
    		
    		messages.add(playerItem);
    	}
    	com.game_machine.orm.models.PlayerItem.close();
    	return messages;
    }

	public static void clearModel(Model model) {

    	model.set("player_item_id",null);

    	model.set("player_item_name",null);

    	model.set("player_item_quantity",null);

    	model.set("player_item_color",null);

    }
    
	public void toModel(Model model, String playerId) {

    	if (id != null) {
    		model.setString("player_item_id",id);
    	}

    	if (name != null) {
    		model.setString("player_item_name",name);
    	}

    	if (quantity != null) {
    		model.setInteger("player_item_quantity",quantity);
    	}

    	if (color != null) {
    		model.setString("player_item_color",color);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static PlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	PlayerItem message = new PlayerItem();

    	String idField = model.getString("player_item_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}

    	String nameField = model.getString("player_item_name");
    	if (nameField != null) {
    		message.setName(nameField);
    		hasFields = true;
    	}

    	Integer quantityField = model.getInteger("player_item_quantity");
    	if (quantityField != null) {
    		message.setQuantity(quantityField);
    		hasFields = true;
    	}

    	String colorField = model.getString("player_item_color");
    	if (colorField != null) {
    		message.setColor(colorField);
    		hasFields = true;
    	}

    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public String getId() {
		return id;
	}
	
	public PlayerItem setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
    }

	public String getName() {
		return name;
	}
	
	public PlayerItem setName(String name) {
		this.name = name;
		return this;
	}
	
	public Boolean hasName()  {
        return name == null ? false : true;
    }

	public Integer getQuantity() {
		return quantity;
	}
	
	public PlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}
	
	public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }

	public String getColor() {
		return color;
	}
	
	public PlayerItem setColor(String color) {
		this.color = color;
		return this;
	}
	
	public Boolean hasColor()  {
        return color == null ? false : true;
    }

	public Weapon getWeapon() {
		return weapon;
	}
	
	public PlayerItem setWeapon(Weapon weapon) {
		this.weapon = weapon;
		return this;
	}
	
	public Boolean hasWeapon()  {
        return weapon == null ? false : true;
    }

	public Consumable getConsumable() {
		return consumable;
	}
	
	public PlayerItem setConsumable(Consumable consumable) {
		this.consumable = consumable;
		return this;
	}
	
	public Boolean hasConsumable()  {
        return consumable == null ? false : true;
    }

	public Cost getCost() {
		return cost;
	}
	
	public PlayerItem setCost(Cost cost) {
		this.cost = cost;
		return this;
	}
	
	public Boolean hasCost()  {
        return cost == null ? false : true;
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

    public Schema<PlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerItem newMessage()
    {
        return new PlayerItem();
    }

    public Class<PlayerItem> typeClass()
    {
        return PlayerItem.class;
    }

    public String messageName()
    {
        return PlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerItem.class.getName();
    }

    public boolean isInitialized(PlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.id = input.readString();
                	break;

            	case 2:

                	message.name = input.readString();
                	break;

            	case 3:

                	message.quantity = input.readInt32();
                	break;

            	case 4:

                	message.color = input.readString();
                	break;

            	case 5:

                	message.weapon = input.mergeObject(message.weapon, Weapon.getSchema());
                    break;

            	case 6:

                	message.consumable = input.mergeObject(message.consumable, Consumable.getSchema());
                    break;

            	case 8:

                	message.cost = input.mergeObject(message.cost, Cost.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, PlayerItem message) throws IOException
    {

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(1, message.id, false);

    	if(message.name == null)
            throw new UninitializedMessageException(message);

    	if(message.name != null)
            output.writeString(2, message.name, false);

    	if(message.quantity == null)
            throw new UninitializedMessageException(message);

    	if(message.quantity != null)
            output.writeInt32(3, message.quantity, false);

    	if(message.color != null)
            output.writeString(4, message.color, false);

    	if(message.weapon != null)
    		output.writeObject(5, message.weapon, Weapon.getSchema(), false);

    	if(message.consumable != null)
    		output.writeObject(6, message.consumable, Consumable.getSchema(), false);

    	if(message.cost != null)
    		output.writeObject(8, message.cost, Cost.getSchema(), false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "id";
        	
        	case 2: return "name";
        	
        	case 3: return "quantity";
        	
        	case 4: return "color";
        	
        	case 5: return "weapon";
        	
        	case 6: return "consumable";
        	
        	case 8: return "cost";
        	
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
    	
    	__fieldMap.put("id", 1);
    	
    	__fieldMap.put("name", 2);
    	
    	__fieldMap.put("quantity", 3);
    	
    	__fieldMap.put("color", 4);
    	
    	__fieldMap.put("weapon", 5);
    	
    	__fieldMap.put("consumable", 6);
    	
    	__fieldMap.put("cost", 8);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerItem parseFrom(byte[] bytes) {
	PlayerItem message = new PlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(PlayerItem.class));
	return message;
}

public PlayerItem clone() {
	byte[] bytes = this.toByteArray();
	PlayerItem playerItem = PlayerItem.parseFrom(bytes);
	return playerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerItem> schema = RuntimeSchema.getSchema(PlayerItem.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(PlayerItem.class), buffer);
		buffer.clear();
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(PlayerItem.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
