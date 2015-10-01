
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
public final class UserDefinedData implements Externalizable, Message<UserDefinedData>, Schema<UserDefinedData>{



    public static Schema<UserDefinedData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UserDefinedData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UserDefinedData DEFAULT_INSTANCE = new UserDefinedData();

    			public Integer command;
	    
        			public Integer value;
	    
        			public Integer userdefInt2;
	    
        			public Integer userdefInt3;
	    
        			public Integer userdefInt4;
	    
        			public Integer userdefInt5;
	    
        			public Integer userdefInt6;
	    
        			public Integer userdefInt7;
	    
        			public Integer userdefInt8;
	    
        			public Integer userdefInt9;
	    
        			public Integer userdefInt1;
	    
        			public Integer rx;
	    
        			public Integer ry;
	    
        			public Integer rz;
	    
        			public Integer rw;
	    
        			public Integer x;
	    
        			public Integer y;
	    
        			public Integer z;
	    
        			public Integer userdefInt10;
	    
        			public Integer userdefInt11;
	    
        			public Integer userdefInt12;
	    
        			public Integer userdefInt13;
	    
        			public Integer userdefInt14;
	    
        			public Integer userdefInt15;
	    
      
    public UserDefinedData()
    {
        
    }


	

	    
    public Boolean hasCommand()  {
        return command == null ? false : true;
    }
        
		public Integer getCommand() {
		return command;
	}
	
	public UserDefinedData setCommand(Integer command) {
		this.command = command;
		return this;	}
	
		    
    public Boolean hasValue()  {
        return value == null ? false : true;
    }
        
		public Integer getValue() {
		return value;
	}
	
	public UserDefinedData setValue(Integer value) {
		this.value = value;
		return this;	}
	
		    
    public Boolean hasUserdefInt2()  {
        return userdefInt2 == null ? false : true;
    }
        
		public Integer getUserdefInt2() {
		return userdefInt2;
	}
	
	public UserDefinedData setUserdefInt2(Integer userdefInt2) {
		this.userdefInt2 = userdefInt2;
		return this;	}
	
		    
    public Boolean hasUserdefInt3()  {
        return userdefInt3 == null ? false : true;
    }
        
		public Integer getUserdefInt3() {
		return userdefInt3;
	}
	
	public UserDefinedData setUserdefInt3(Integer userdefInt3) {
		this.userdefInt3 = userdefInt3;
		return this;	}
	
		    
    public Boolean hasUserdefInt4()  {
        return userdefInt4 == null ? false : true;
    }
        
		public Integer getUserdefInt4() {
		return userdefInt4;
	}
	
	public UserDefinedData setUserdefInt4(Integer userdefInt4) {
		this.userdefInt4 = userdefInt4;
		return this;	}
	
		    
    public Boolean hasUserdefInt5()  {
        return userdefInt5 == null ? false : true;
    }
        
		public Integer getUserdefInt5() {
		return userdefInt5;
	}
	
	public UserDefinedData setUserdefInt5(Integer userdefInt5) {
		this.userdefInt5 = userdefInt5;
		return this;	}
	
		    
    public Boolean hasUserdefInt6()  {
        return userdefInt6 == null ? false : true;
    }
        
		public Integer getUserdefInt6() {
		return userdefInt6;
	}
	
	public UserDefinedData setUserdefInt6(Integer userdefInt6) {
		this.userdefInt6 = userdefInt6;
		return this;	}
	
		    
    public Boolean hasUserdefInt7()  {
        return userdefInt7 == null ? false : true;
    }
        
		public Integer getUserdefInt7() {
		return userdefInt7;
	}
	
	public UserDefinedData setUserdefInt7(Integer userdefInt7) {
		this.userdefInt7 = userdefInt7;
		return this;	}
	
		    
    public Boolean hasUserdefInt8()  {
        return userdefInt8 == null ? false : true;
    }
        
		public Integer getUserdefInt8() {
		return userdefInt8;
	}
	
	public UserDefinedData setUserdefInt8(Integer userdefInt8) {
		this.userdefInt8 = userdefInt8;
		return this;	}
	
		    
    public Boolean hasUserdefInt9()  {
        return userdefInt9 == null ? false : true;
    }
        
		public Integer getUserdefInt9() {
		return userdefInt9;
	}
	
	public UserDefinedData setUserdefInt9(Integer userdefInt9) {
		this.userdefInt9 = userdefInt9;
		return this;	}
	
		    
    public Boolean hasUserdefInt1()  {
        return userdefInt1 == null ? false : true;
    }
        
		public Integer getUserdefInt1() {
		return userdefInt1;
	}
	
	public UserDefinedData setUserdefInt1(Integer userdefInt1) {
		this.userdefInt1 = userdefInt1;
		return this;	}
	
		    
    public Boolean hasRx()  {
        return rx == null ? false : true;
    }
        
		public Integer getRx() {
		return rx;
	}
	
	public UserDefinedData setRx(Integer rx) {
		this.rx = rx;
		return this;	}
	
		    
    public Boolean hasRy()  {
        return ry == null ? false : true;
    }
        
		public Integer getRy() {
		return ry;
	}
	
	public UserDefinedData setRy(Integer ry) {
		this.ry = ry;
		return this;	}
	
		    
    public Boolean hasRz()  {
        return rz == null ? false : true;
    }
        
		public Integer getRz() {
		return rz;
	}
	
	public UserDefinedData setRz(Integer rz) {
		this.rz = rz;
		return this;	}
	
		    
    public Boolean hasRw()  {
        return rw == null ? false : true;
    }
        
		public Integer getRw() {
		return rw;
	}
	
	public UserDefinedData setRw(Integer rw) {
		this.rw = rw;
		return this;	}
	
		    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Integer getX() {
		return x;
	}
	
	public UserDefinedData setX(Integer x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Integer getY() {
		return y;
	}
	
	public UserDefinedData setY(Integer y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasZ()  {
        return z == null ? false : true;
    }
        
		public Integer getZ() {
		return z;
	}
	
	public UserDefinedData setZ(Integer z) {
		this.z = z;
		return this;	}
	
		    
    public Boolean hasUserdefInt10()  {
        return userdefInt10 == null ? false : true;
    }
        
		public Integer getUserdefInt10() {
		return userdefInt10;
	}
	
	public UserDefinedData setUserdefInt10(Integer userdefInt10) {
		this.userdefInt10 = userdefInt10;
		return this;	}
	
		    
    public Boolean hasUserdefInt11()  {
        return userdefInt11 == null ? false : true;
    }
        
		public Integer getUserdefInt11() {
		return userdefInt11;
	}
	
	public UserDefinedData setUserdefInt11(Integer userdefInt11) {
		this.userdefInt11 = userdefInt11;
		return this;	}
	
		    
    public Boolean hasUserdefInt12()  {
        return userdefInt12 == null ? false : true;
    }
        
		public Integer getUserdefInt12() {
		return userdefInt12;
	}
	
	public UserDefinedData setUserdefInt12(Integer userdefInt12) {
		this.userdefInt12 = userdefInt12;
		return this;	}
	
		    
    public Boolean hasUserdefInt13()  {
        return userdefInt13 == null ? false : true;
    }
        
		public Integer getUserdefInt13() {
		return userdefInt13;
	}
	
	public UserDefinedData setUserdefInt13(Integer userdefInt13) {
		this.userdefInt13 = userdefInt13;
		return this;	}
	
		    
    public Boolean hasUserdefInt14()  {
        return userdefInt14 == null ? false : true;
    }
        
		public Integer getUserdefInt14() {
		return userdefInt14;
	}
	
	public UserDefinedData setUserdefInt14(Integer userdefInt14) {
		this.userdefInt14 = userdefInt14;
		return this;	}
	
		    
    public Boolean hasUserdefInt15()  {
        return userdefInt15 == null ? false : true;
    }
        
		public Integer getUserdefInt15() {
		return userdefInt15;
	}
	
	public UserDefinedData setUserdefInt15(Integer userdefInt15) {
		this.userdefInt15 = userdefInt15;
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

    public Schema<UserDefinedData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UserDefinedData newMessage()
    {
        return new UserDefinedData();
    }

    public Class<UserDefinedData> typeClass()
    {
        return UserDefinedData.class;
    }

    public String messageName()
    {
        return UserDefinedData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UserDefinedData.class.getName();
    }

    public boolean isInitialized(UserDefinedData message)
    {
        return true;
    }

    public void mergeFrom(Input input, UserDefinedData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.command = input.readInt32();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.value = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.userdefInt2 = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.userdefInt3 = input.readInt32();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.userdefInt4 = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.userdefInt5 = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.userdefInt6 = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.userdefInt7 = input.readInt32();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.userdefInt8 = input.readInt32();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.userdefInt9 = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.userdefInt1 = input.readInt32();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.rx = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.ry = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.rz = input.readInt32();
                	break;
                	                	
                            	            	case 15:
            	                	                	message.rw = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.x = input.readSInt32();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.y = input.readSInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.z = input.readSInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.userdefInt10 = input.readInt32();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.userdefInt11 = input.readInt32();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.userdefInt12 = input.readInt32();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.userdefInt13 = input.readInt32();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.userdefInt14 = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.userdefInt15 = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UserDefinedData message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.command != null)
            output.writeInt32(1, message.command, false);
    	    	
    	            	
    	    	
    	    	    	if(message.value != null)
            output.writeInt32(2, message.value, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt2 != null)
            output.writeInt32(3, message.userdefInt2, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt3 != null)
            output.writeInt32(4, message.userdefInt3, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt4 != null)
            output.writeInt32(5, message.userdefInt4, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt5 != null)
            output.writeInt32(6, message.userdefInt5, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt6 != null)
            output.writeInt32(7, message.userdefInt6, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt7 != null)
            output.writeInt32(8, message.userdefInt7, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt8 != null)
            output.writeInt32(9, message.userdefInt8, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt9 != null)
            output.writeInt32(10, message.userdefInt9, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt1 != null)
            output.writeInt32(11, message.userdefInt1, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rx != null)
            output.writeInt32(12, message.rx, false);
    	    	
    	            	
    	    	
    	    	    	if(message.ry != null)
            output.writeInt32(13, message.ry, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rz != null)
            output.writeInt32(14, message.rz, false);
    	    	
    	            	
    	    	
    	    	    	if(message.rw != null)
            output.writeInt32(15, message.rw, false);
    	    	
    	            	
    	    	
    	    	    	if(message.x != null)
            output.writeSInt32(16, message.x, false);
    	    	
    	            	
    	    	
    	    	    	if(message.y != null)
            output.writeSInt32(17, message.y, false);
    	    	
    	            	
    	    	
    	    	    	if(message.z != null)
            output.writeSInt32(18, message.z, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt10 != null)
            output.writeInt32(19, message.userdefInt10, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt11 != null)
            output.writeInt32(20, message.userdefInt11, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt12 != null)
            output.writeInt32(21, message.userdefInt12, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt13 != null)
            output.writeInt32(22, message.userdefInt13, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt14 != null)
            output.writeInt32(23, message.userdefInt14, false);
    	    	
    	            	
    	    	
    	    	    	if(message.userdefInt15 != null)
            output.writeInt32(24, message.userdefInt15, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "command";
        	        	case 2: return "value";
        	        	case 3: return "userdefInt2";
        	        	case 4: return "userdefInt3";
        	        	case 5: return "userdefInt4";
        	        	case 6: return "userdefInt5";
        	        	case 7: return "userdefInt6";
        	        	case 8: return "userdefInt7";
        	        	case 9: return "userdefInt8";
        	        	case 10: return "userdefInt9";
        	        	case 11: return "userdefInt1";
        	        	case 12: return "rx";
        	        	case 13: return "ry";
        	        	case 14: return "rz";
        	        	case 15: return "rw";
        	        	case 16: return "x";
        	        	case 17: return "y";
        	        	case 18: return "z";
        	        	case 19: return "userdefInt10";
        	        	case 20: return "userdefInt11";
        	        	case 21: return "userdefInt12";
        	        	case 22: return "userdefInt13";
        	        	case 23: return "userdefInt14";
        	        	case 24: return "userdefInt15";
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
    	    	__fieldMap.put("command", 1);
    	    	__fieldMap.put("value", 2);
    	    	__fieldMap.put("userdefInt2", 3);
    	    	__fieldMap.put("userdefInt3", 4);
    	    	__fieldMap.put("userdefInt4", 5);
    	    	__fieldMap.put("userdefInt5", 6);
    	    	__fieldMap.put("userdefInt6", 7);
    	    	__fieldMap.put("userdefInt7", 8);
    	    	__fieldMap.put("userdefInt8", 9);
    	    	__fieldMap.put("userdefInt9", 10);
    	    	__fieldMap.put("userdefInt1", 11);
    	    	__fieldMap.put("rx", 12);
    	    	__fieldMap.put("ry", 13);
    	    	__fieldMap.put("rz", 14);
    	    	__fieldMap.put("rw", 15);
    	    	__fieldMap.put("x", 16);
    	    	__fieldMap.put("y", 17);
    	    	__fieldMap.put("z", 18);
    	    	__fieldMap.put("userdefInt10", 19);
    	    	__fieldMap.put("userdefInt11", 20);
    	    	__fieldMap.put("userdefInt12", 21);
    	    	__fieldMap.put("userdefInt13", 22);
    	    	__fieldMap.put("userdefInt14", 23);
    	    	__fieldMap.put("userdefInt15", 24);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = UserDefinedData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static UserDefinedData parseFrom(byte[] bytes) {
	UserDefinedData message = new UserDefinedData();
	ProtobufIOUtil.mergeFrom(bytes, message, UserDefinedData.getSchema());
	return message;
}

public static UserDefinedData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	UserDefinedData message = new UserDefinedData();
	JsonIOUtil.mergeFrom(bytes, message, UserDefinedData.getSchema(), false);
	return message;
}

public UserDefinedData clone() {
	byte[] bytes = this.toByteArray();
	UserDefinedData userDefinedData = UserDefinedData.parseFrom(bytes);
	return userDefinedData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, UserDefinedData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<UserDefinedData> schema = UserDefinedData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, UserDefinedData.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
