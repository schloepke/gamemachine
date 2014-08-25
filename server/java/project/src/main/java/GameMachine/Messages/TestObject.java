
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

public final class TestObject  implements Externalizable, Message<TestObject>, Schema<TestObject>, PersistentMessage

{

	public enum Corpus implements com.dyuproject.protostuff.EnumLite<Corpus>
    {
    	
    	UNIVERSAL(0),
    	
    	WEB(1),
    	
    	IMAGES(2),
    	
    	LOCAL(3),
    	
    	NEWS(4),
    	
    	PRODUCTS(5),
    	
    	VIDEO(6);

        public final int number;
        
        private Corpus (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Corpus valueOf(int number)
        {
            switch(number) 
            {
            	
    			case 0: return (UNIVERSAL);
    			
    			case 1: return (WEB);
    			
    			case 2: return (IMAGES);
    			
    			case 3: return (LOCAL);
    			
    			case 4: return (NEWS);
    			
    			case 5: return (PRODUCTS);
    			
    			case 6: return (VIDEO);
    			
                default: return null;
            }
        }
    }

    public static Schema<TestObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TestObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TestObject DEFAULT_INSTANCE = new TestObject();

	public Errors ormErrors;
	public String persistPlayerId;
	public String getPersistPlayerId() {
		return persistPlayerId;
	}
	
	public String persistAction;
	public String getPersistAction() {
		return persistAction;
	}

		public String optionalString;

		public String requiredString;

    public List<Integer> numbers;

		public ByteString bstring;

		public Boolean bvalue;

		public Double dvalue;

		public Float fvalue;

		public Long numbers64;

    public List<Player> player;

		public Corpus corpus; // = UNIVERSAL:0;

    public List<Corpus> corpus2;

		public String id;

    public TestObject()
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
    		com.game_machine.orm.models.TestObject.open();
    	}
    	
    	com.game_machine.orm.models.TestObject model = com.game_machine.orm.models.TestObject.findFirst("test_object_id = ? and player_id = ?", this.id, playerId);
    	if (model == null) {
    		model = new com.game_machine.orm.models.TestObject();
    		toModel(model,playerId);
    	} else {
    		toModel(model,null);
    	}

    	Boolean res = model.save();
    	if (!res) {
    		ormErrors = model.errors();
    	}
    	if (!inTransaction) {
    		com.game_machine.orm.models.TestObject.close();
    	}
    	return res;
    }
    
    public static void ormDeleteAsync(String id, String playerId) {
    	TestObject message = new TestObject();
    	message.setId(id);
		message.persistPlayerId = playerId;
		message.persistAction = "delete";
		ActorSelection sel = ActorUtil.getSelectionByName("message_persister");
		sel.tell(message, null);
    }
    
    public Boolean ormDelete(String playerId) {
    	Boolean result;
    	com.game_machine.orm.models.TestObject.open();
    	com.game_machine.orm.models.TestObject model = com.game_machine.orm.models.TestObject.findFirst("test_object_id = ? and player_id = ?", this.id, playerId);
    	if (model != null) {
    		result = model.delete();
    	} else {
    		result = false;
    	}
    	com.game_machine.orm.models.TestObject.close();
    	return result;
    }
    
    public static TestObject ormFind(String id, String playerId) {
    	return ormFind(id, playerId, false);
    }
    
    public static TestObject ormFind(String id, String playerId, boolean inTransaction) {
    	if (!inTransaction) {
    		com.game_machine.orm.models.TestObject.open();
    	}
    	
    	com.game_machine.orm.models.TestObject model = com.game_machine.orm.models.TestObject.findFirst("test_object_id = ? and player_id = ?", id, playerId);
    	
    	if (!inTransaction) {
    		com.game_machine.orm.models.TestObject.close();
    	}
    	
    	if (model == null) {
    		return null;
    	} else {
    		TestObject testObject = fromModel(model);
    		
    		return testObject;
    	}
    }
    
    public static List<TestObject> ormFindAll(String playerId) {
    	com.game_machine.orm.models.TestObject.open();
    	List<com.game_machine.orm.models.TestObject> models = com.game_machine.orm.models.TestObject.where("player_id = ?", playerId);
    	List<TestObject> messages = new ArrayList<TestObject>();
    	for (com.game_machine.orm.models.TestObject model : models) {
    		TestObject testObject = fromModel(model);
    		
    		messages.add(testObject);
    	}
    	com.game_machine.orm.models.TestObject.close();
    	return messages;
    }
    
    public static List<TestObject> ormWhere(String query, Object ... params) {
    	com.game_machine.orm.models.TestObject.open();
    	List<com.game_machine.orm.models.TestObject> models = com.game_machine.orm.models.TestObject.where(query, params);
    	List<TestObject> messages = new ArrayList<TestObject>();
    	for (com.game_machine.orm.models.TestObject model : models) {
    		TestObject testObject = fromModel(model);
    		
    		messages.add(testObject);
    	}
    	com.game_machine.orm.models.TestObject.close();
    	return messages;
    }

	public static void clearModel(Model model) {

    	model.set("test_object_optional_string",null);

    	model.set("test_object_required_string",null);

    	model.set("test_object_bvalue",null);

    	model.set("test_object_dvalue",null);

    	model.set("test_object_fvalue",null);

    	model.set("test_object_numbers64",null);

    	model.set("test_object_id",null);
    	
    }
    
	public void toModel(Model model, String playerId) {

    	if (optionalString != null) {
    		model.setString("test_object_optional_string",optionalString);
    	}

    	if (requiredString != null) {
    		model.setString("test_object_required_string",requiredString);
    	}

    	if (bvalue != null) {
    		model.setBoolean("test_object_bvalue",bvalue);
    	}

    	if (dvalue != null) {
    		model.setDouble("test_object_dvalue",dvalue);
    	}

    	if (fvalue != null) {
    		model.setFloat("test_object_fvalue",fvalue);
    	}

    	if (numbers64 != null) {
    		model.setLong("test_object_numbers64",numbers64);
    	}

    	if (id != null) {
    		model.setString("test_object_id",id);
    	}

    	if (playerId != null) {
    		model.set("player_id",playerId);
    	}
    }
    
	public static TestObject fromModel(Model model) {
		boolean hasFields = false;
    	TestObject message = new TestObject();

    	String optionalStringField = model.getString("test_object_optional_string");
    	if (optionalStringField != null) {
    		message.setOptionalString(optionalStringField);
    		hasFields = true;
    	}

    	String requiredStringField = model.getString("test_object_required_string");
    	if (requiredStringField != null) {
    		message.setRequiredString(requiredStringField);
    		hasFields = true;
    	}

    	Boolean bvalueField = model.getBoolean("test_object_bvalue");
    	if (bvalueField != null) {
    		message.setBvalue(bvalueField);
    		hasFields = true;
    	}

    	Double dvalueField = model.getDouble("test_object_dvalue");
    	if (dvalueField != null) {
    		message.setDvalue(dvalueField);
    		hasFields = true;
    	}

    	Float fvalueField = model.getFloat("test_object_fvalue");
    	if (fvalueField != null) {
    		message.setFvalue(fvalueField);
    		hasFields = true;
    	}

    	Long numbers64Field = model.getLong("test_object_numbers64");
    	if (numbers64Field != null) {
    		message.setNumbers64(numbers64Field);
    		hasFields = true;
    	}

    	String idField = model.getString("test_object_id");
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	
    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }

	public String getOptionalString() {
		return optionalString;
	}
	
	public TestObject setOptionalString(String optionalString) {
		this.optionalString = optionalString;
		return this;
	}
	
	public Boolean hasOptionalString()  {
        return optionalString == null ? false : true;
    }

	public String getRequiredString() {
		return requiredString;
	}
	
	public TestObject setRequiredString(String requiredString) {
		this.requiredString = requiredString;
		return this;
	}
	
	public Boolean hasRequiredString()  {
        return requiredString == null ? false : true;
    }

	public List<Integer> getNumbersList() {
		return numbers;
	}

	public TestObject setNumbersList(List<Integer> numbers) {
		this.numbers = numbers;
		return this;
	}

	public Integer getNumbers(int index)  {
        return numbers == null ? null : numbers.get(index);
    }

    public int getNumbersCount()  {
        return numbers == null ? 0 : numbers.size();
    }

    public TestObject addNumbers(Integer numbers)  {
        if(this.numbers == null)
            this.numbers = new ArrayList<Integer>();
        this.numbers.add(numbers);
        return this;
    }

	public ByteString getBstring() {
		return bstring;
	}
	
	public TestObject setBstring(ByteString bstring) {
		this.bstring = bstring;
		return this;
	}
	
	public Boolean hasBstring()  {
        return bstring == null ? false : true;
    }

	public Boolean getBvalue() {
		return bvalue;
	}
	
	public TestObject setBvalue(Boolean bvalue) {
		this.bvalue = bvalue;
		return this;
	}
	
	public Boolean hasBvalue()  {
        return bvalue == null ? false : true;
    }

	public Double getDvalue() {
		return dvalue;
	}
	
	public TestObject setDvalue(Double dvalue) {
		this.dvalue = dvalue;
		return this;
	}
	
	public Boolean hasDvalue()  {
        return dvalue == null ? false : true;
    }

	public Float getFvalue() {
		return fvalue;
	}
	
	public TestObject setFvalue(Float fvalue) {
		this.fvalue = fvalue;
		return this;
	}
	
	public Boolean hasFvalue()  {
        return fvalue == null ? false : true;
    }

	public Long getNumbers64() {
		return numbers64;
	}
	
	public TestObject setNumbers64(Long numbers64) {
		this.numbers64 = numbers64;
		return this;
	}
	
	public Boolean hasNumbers64()  {
        return numbers64 == null ? false : true;
    }

	public List<Player> getPlayerList() {
		return player;
	}

	public TestObject setPlayerList(List<Player> player) {
		this.player = player;
		return this;
	}

	public Player getPlayer(int index)  {
        return player == null ? null : player.get(index);
    }

    public int getPlayerCount()  {
        return player == null ? 0 : player.size();
    }

    public TestObject addPlayer(Player player)  {
        if(this.player == null)
            this.player = new ArrayList<Player>();
        this.player.add(player);
        return this;
    }

    public TestObject removePlayerById(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();

    		if (player.id.equals(obj.id)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public TestObject removePlayerByName(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();

    		if (player.name.equals(obj.name)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public TestObject removePlayerByAuthenticated(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();

    		if (player.authenticated.equals(obj.authenticated)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

    public TestObject removePlayerByAuthtoken(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();

    		if (player.authtoken.equals(obj.authtoken)) {
    	
      			itr.remove();
    		}
		}
        return this;
    }

	public Corpus getCorpus() {
		return corpus;
	}
	
	public TestObject setCorpus(Corpus corpus) {
		this.corpus = corpus;
		return this;
	}
	
	public Boolean hasCorpus()  {
        return corpus == null ? false : true;
    }

	public List<Corpus> getCorpus2List() {
		return corpus2;
	}

	public TestObject setCorpus2List(List<Corpus> corpus2) {
		this.corpus2 = corpus2;
		return this;
	}

	public Corpus getCorpus2(int index)  {
        return corpus2 == null ? null : corpus2.get(index);
    }

    public int getCorpus2Count()  {
        return corpus2 == null ? 0 : corpus2.size();
    }

    public TestObject addCorpus2(Corpus corpus2)  {
        if(this.corpus2 == null)
            this.corpus2 = new ArrayList<Corpus>();
        this.corpus2.add(corpus2);
        return this;
    }

	public String getId() {
		return id;
	}
	
	public TestObject setId(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean hasId()  {
        return id == null ? false : true;
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

    public Schema<TestObject> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TestObject newMessage()
    {
        return new TestObject();
    }

    public Class<TestObject> typeClass()
    {
        return TestObject.class;
    }

    public String messageName()
    {
        return TestObject.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TestObject.class.getName();
    }

    public boolean isInitialized(TestObject message)
    {
        return true;
    }

    public void mergeFrom(Input input, TestObject message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                
            	case 1:

                	message.optionalString = input.readString();
                	break;

            	case 2:

                	message.requiredString = input.readString();
                	break;

            	case 3:
            	
            		if(message.numbers == null)
                        message.numbers = new ArrayList<Integer>();
                    
                	message.numbers.add(input.readInt32());
                	
                    break;

            	case 4:

                	message.bstring = input.readBytes();
                	break;

            	case 5:

                	message.bvalue = input.readBool();
                	break;

            	case 6:

                	message.dvalue = input.readDouble();
                	break;

            	case 7:

                	message.fvalue = input.readFloat();
                	break;

            	case 8:

                	message.numbers64 = input.readInt64();
                	break;

            	case 9:
            	
            		if(message.player == null)
                        message.player = new ArrayList<Player>();
                    
                    message.player.add(input.mergeObject(null, Player.getSchema()));
                    
                    break;

            	case 10:

                    message.corpus = Corpus.valueOf(input.readEnum());
                    break;

            	case 11:
            	
            		if(message.corpus2 == null)
                        message.corpus2 = new ArrayList<Corpus>();
                    
                    message.corpus2.add(Corpus.valueOf(input.readEnum()));
                    
                    break;

            	case 12:

                	message.id = input.readString();
                	break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void writeTo(Output output, TestObject message) throws IOException
    {

    	if(message.optionalString != null)
            output.writeString(1, message.optionalString, false);

    	if(message.requiredString == null)
            throw new UninitializedMessageException(message);

    	if(message.requiredString != null)
            output.writeString(2, message.requiredString, false);

    	if(message.numbers != null)
        {
            for(Integer numbers : message.numbers)
            {
                if(numbers != null) {
                   	
            		output.writeInt32(3, numbers, true);
    				
    			}
            }
        }

    	if(message.bstring != null)
            output.writeBytes(4, message.bstring, false);

    	if(message.bvalue != null)
            output.writeBool(5, message.bvalue, false);

    	if(message.dvalue != null)
            output.writeDouble(6, message.dvalue, false);

    	if(message.fvalue != null)
            output.writeFloat(7, message.fvalue, false);

    	if(message.numbers64 != null)
            output.writeInt64(8, message.numbers64, false);

    	if(message.player != null)
        {
            for(Player player : message.player)
            {
                if(player != null) {
                   	
    				output.writeObject(9, player, Player.getSchema(), true);
    				
    			}
            }
        }

    	 	output.writeEnum(10, message.corpus.number, false);

    	if(message.corpus2 != null)
        {
            for(Corpus corpus2 : message.corpus2)
            {
                if(corpus2 != null) {
                   	
    				output.writeEnum(11, corpus2.number, true);
    				
    			}
            }
        }

    	if(message.id == null)
            throw new UninitializedMessageException(message);

    	if(message.id != null)
            output.writeString(12, message.id, false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	
        	case 1: return "optionalString";
        	
        	case 2: return "requiredString";
        	
        	case 3: return "numbers";
        	
        	case 4: return "bstring";
        	
        	case 5: return "bvalue";
        	
        	case 6: return "dvalue";
        	
        	case 7: return "fvalue";
        	
        	case 8: return "numbers64";
        	
        	case 9: return "player";
        	
        	case 10: return "corpus";
        	
        	case 11: return "corpus2";
        	
        	case 12: return "id";
        	
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
    	
    	__fieldMap.put("optionalString", 1);
    	
    	__fieldMap.put("requiredString", 2);
    	
    	__fieldMap.put("numbers", 3);
    	
    	__fieldMap.put("bstring", 4);
    	
    	__fieldMap.put("bvalue", 5);
    	
    	__fieldMap.put("dvalue", 6);
    	
    	__fieldMap.put("fvalue", 7);
    	
    	__fieldMap.put("numbers64", 8);
    	
    	__fieldMap.put("player", 9);
    	
    	__fieldMap.put("corpus", 10);
    	
    	__fieldMap.put("corpus2", 11);
    	
    	__fieldMap.put("id", 12);
    	
    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TestObject.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TestObject parseFrom(byte[] bytes) {
	TestObject message = new TestObject();
	ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(TestObject.class));
	return message;
}

public TestObject clone() {
	byte[] bytes = this.toByteArray();
	TestObject testObject = TestObject.parseFrom(bytes);
	return testObject;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public byte[] toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TestObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	return out.toByteArray();
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TestObject> schema = RuntimeSchema.getSchema(TestObject.class);
    
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
		bytes = ProtobufIOUtil.toByteArray(this, RuntimeSchema.getSchema(TestObject.class), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, RuntimeSchema.getSchema(TestObject.class));
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bb;
}

}
