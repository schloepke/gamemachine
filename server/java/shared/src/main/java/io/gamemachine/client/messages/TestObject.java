
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
public final class TestObject implements Externalizable, Message<TestObject>, Schema<TestObject>{



    public static Schema<TestObject> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TestObject getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TestObject DEFAULT_INSTANCE = new TestObject();

    			public String optionalString;
	    
        			public String requiredString;
	    
            public List<Integer> numbers;
	    			public ByteString bstring;
	    
        			public Boolean bvalue;
	    
        			public Double dvalue;
	    
        			public Float fvalue;
	    
        			public Long numbers64;
	    
            public List<Player> player;
	    			public Integer recordId;
	    
        			public String id;
	    
      
    public TestObject()
    {
        
    }


	

	    
    public Boolean hasOptionalString()  {
        return optionalString == null ? false : true;
    }
        
		public String getOptionalString() {
		return optionalString;
	}
	
	public TestObject setOptionalString(String optionalString) {
		this.optionalString = optionalString;
		return this;	}
	
		    
    public Boolean hasRequiredString()  {
        return requiredString == null ? false : true;
    }
        
		public String getRequiredString() {
		return requiredString;
	}
	
	public TestObject setRequiredString(String requiredString) {
		this.requiredString = requiredString;
		return this;	}
	
		    
    public Boolean hasNumbers()  {
        return numbers == null ? false : true;
    }
        
		public List<Integer> getNumbersList() {
		if(this.numbers == null)
            this.numbers = new ArrayList<Integer>();
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
        	
    
    
    
		    
    public Boolean hasBstring()  {
        return bstring == null ? false : true;
    }
        
		public ByteString getBstring() {
		return bstring;
	}
	
	public TestObject setBstring(ByteString bstring) {
		this.bstring = bstring;
		return this;	}
	
		    
    public Boolean hasBvalue()  {
        return bvalue == null ? false : true;
    }
        
		public Boolean getBvalue() {
		return bvalue;
	}
	
	public TestObject setBvalue(Boolean bvalue) {
		this.bvalue = bvalue;
		return this;	}
	
		    
    public Boolean hasDvalue()  {
        return dvalue == null ? false : true;
    }
        
		public Double getDvalue() {
		return dvalue;
	}
	
	public TestObject setDvalue(Double dvalue) {
		this.dvalue = dvalue;
		return this;	}
	
		    
    public Boolean hasFvalue()  {
        return fvalue == null ? false : true;
    }
        
		public Float getFvalue() {
		return fvalue;
	}
	
	public TestObject setFvalue(Float fvalue) {
		this.fvalue = fvalue;
		return this;	}
	
		    
    public Boolean hasNumbers64()  {
        return numbers64 == null ? false : true;
    }
        
		public Long getNumbers64() {
		return numbers64;
	}
	
	public TestObject setNumbers64(Long numbers64) {
		this.numbers64 = numbers64;
		return this;	}
	
		    
    public Boolean hasPlayer()  {
        return player == null ? false : true;
    }
        
		public List<Player> getPlayerList() {
		if(this.player == null)
            this.player = new ArrayList<Player>();
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
    
        	    	    	    	
    public TestObject removePlayerByPasswordHash(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.passwordHash.equals(obj.passwordHash)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByGameId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.gameId.equals(obj.gameId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByRecordId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByRole(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.role.equals(obj.role)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByLocked(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.locked.equals(obj.locked)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByIp(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.ip.equals(obj.ip)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByIpChangedAt(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.ipChangedAt.equals(obj.ipChangedAt)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public TestObject removePlayerByCharacterId(Player player)  {
    	if(this.player == null)
           return this;
            
       	Iterator<Player> itr = this.player.iterator();
       	while (itr.hasNext()) {
    	Player obj = itr.next();
    	
    	    		if (player.characterId.equals(obj.characterId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	        	
    
    
    
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public TestObject setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public TestObject setId(String id) {
		this.id = id;
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
            	                	                	message.recordId = input.readInt32();
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
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(10, message.recordId, false);
    	    	
    	            	
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
        	        	case 10: return "recordId";
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
    	    	__fieldMap.put("recordId", 10);
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
	ProtobufIOUtil.mergeFrom(bytes, message, TestObject.getSchema());
	return message;
}

public static TestObject parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TestObject message = new TestObject();
	JsonIOUtil.mergeFrom(bytes, message, TestObject.getSchema(), false);
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

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TestObject.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TestObject> schema = TestObject.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TestObject.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
