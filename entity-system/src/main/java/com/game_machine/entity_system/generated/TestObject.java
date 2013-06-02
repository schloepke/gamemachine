
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
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.Component;

import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;

public final class TestObject extends com.game_machine.entity_system.Component implements Externalizable, Message<TestObject>, Schema<TestObject>
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



    private String optionalString;



    private String requiredString;



    private List<Integer> numbers;



    private ByteString bstring;



    private Boolean bvalue;



    private Double dvalue;



    private Float fvalue;



    private Long numbers64;



    private List<Player> player;



    private Corpus corpus;



    private List<Corpus> corpus2;


    

    public TestObject()
    {
        
    }




	public String getOptionalString() {
		return optionalString;
	}
	
	public TestObject setOptionalString(String optionalString) {
		this.optionalString = optionalString;
		return this;
	}




	public String getRequiredString() {
		return requiredString;
	}
	
	public TestObject setRequiredString(String requiredString) {
		this.requiredString = requiredString;
		return this;
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




	public Boolean getBvalue() {
		return bvalue;
	}
	
	public TestObject setBvalue(Boolean bvalue) {
		this.bvalue = bvalue;
		return this;
	}




	public Double getDvalue() {
		return dvalue;
	}
	
	public TestObject setDvalue(Double dvalue) {
		this.dvalue = dvalue;
		return this;
	}




	public Float getFvalue() {
		return fvalue;
	}
	
	public TestObject setFvalue(Float fvalue) {
		this.fvalue = fvalue;
		return this;
	}




	public Long getNumbers64() {
		return numbers64;
	}
	
	public TestObject setNumbers64(Long numbers64) {
		this.numbers64 = numbers64;
		return this;
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
    




	public Corpus getCorpus() {
		return corpus;
	}
	
	public TestObject setCorpus(Corpus corpus) {
		this.corpus = corpus;
		return this;
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
	testObject.setEntityId(null);
	return testObject;
}
	
public byte[] toByteArray() {
	if (Entities.encoding.equals("protobuf")) {
		return toProtobuf();
	} else if (Entities.encoding.equals("json")) {
		return toJson();
	} else {
		throw new RuntimeException("No encoding specified");
	}
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
		
public byte[] toProtobuf() {
	LinkedBuffer buffer = LinkedBuffer.allocate(1024);
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

 
    

}
