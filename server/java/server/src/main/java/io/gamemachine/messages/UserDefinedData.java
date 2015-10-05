
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
import org.javalite.activejdbc.Model;
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
    static final String defaultScope = UserDefinedData.class.getSimpleName();

    	
	    	    public int command= 0;
	    		
    
        	
	    	    public int value= 0;
	    		
    
        	
	    	    public int userdefInt2= 0;
	    		
    
        	
	    	    public int userdefInt3= 0;
	    		
    
        	
	    	    public int userdefInt4= 0;
	    		
    
        	
	    	    public int userdefInt5= 0;
	    		
    
        	
	    	    public int userdefInt6= 0;
	    		
    
        	
	    	    public int userdefInt7= 0;
	    		
    
        	
	    	    public int userdefInt8= 0;
	    		
    
        	
	    	    public int userdefInt9= 0;
	    		
    
        	
	    	    public int userdefInt1= 0;
	    		
    
        	
	    	    public int rx= 0;
	    		
    
        	
	    	    public int ry= 0;
	    		
    
        	
	    	    public int rz= 0;
	    		
    
        	
	    	    public int rw= 0;
	    		
    
        	
	    	    public int x= 0;
	    		
    
        	
	    	    public int y= 0;
	    		
    
        	
	    	    public int z= 0;
	    		
    
        	
	    	    public int userdefInt10= 0;
	    		
    
        	
	    	    public int userdefInt11= 0;
	    		
    
        	
	    	    public int userdefInt12= 0;
	    		
    
        	
	    	    public int userdefInt13= 0;
	    		
    
        	
	    	    public int userdefInt14= 0;
	    		
    
        	
	    	    public int userdefInt15= 0;
	    		
    
        


    public UserDefinedData()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("user_defined_data_command",null);
    	    	    	    	    	    	model.set("user_defined_data_value",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int2",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int3",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int4",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int5",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int6",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int7",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int8",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int9",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int1",null);
    	    	    	    	    	    	model.set("user_defined_data_rx",null);
    	    	    	    	    	    	model.set("user_defined_data_ry",null);
    	    	    	    	    	    	model.set("user_defined_data_rz",null);
    	    	    	    	    	    	model.set("user_defined_data_rw",null);
    	    	    	    	    	    	model.set("user_defined_data_x",null);
    	    	    	    	    	    	model.set("user_defined_data_y",null);
    	    	    	    	    	    	model.set("user_defined_data_z",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int10",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int11",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int12",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int13",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int14",null);
    	    	    	    	    	    	model.set("user_defined_data_userdef_int15",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (command != null) {
    	       	    	model.setInteger("user_defined_data_command",command);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (value != null) {
    	       	    	model.setInteger("user_defined_data_value",value);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt2 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int2",userdefInt2);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt3 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int3",userdefInt3);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt4 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int4",userdefInt4);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt5 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int5",userdefInt5);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt6 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int6",userdefInt6);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt7 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int7",userdefInt7);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt8 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int8",userdefInt8);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt9 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int9",userdefInt9);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt1 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int1",userdefInt1);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rx != null) {
    	       	    	model.setInteger("user_defined_data_rx",rx);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (ry != null) {
    	       	    	model.setInteger("user_defined_data_ry",ry);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rz != null) {
    	       	    	model.setInteger("user_defined_data_rz",rz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (rw != null) {
    	       	    	model.setInteger("user_defined_data_rw",rw);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (x != null) {
    	       	    	model.setInteger("user_defined_data_x",x);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (y != null) {
    	       	    	model.setInteger("user_defined_data_y",y);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (z != null) {
    	       	    	model.setInteger("user_defined_data_z",z);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt10 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int10",userdefInt10);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt11 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int11",userdefInt11);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt12 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int12",userdefInt12);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt13 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int13",userdefInt13);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt14 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int14",userdefInt14);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (userdefInt15 != null) {
    	       	    	model.setInteger("user_defined_data_userdef_int15",userdefInt15);
    	        		
    	//}
    	    	    }
    
	public static UserDefinedData fromModel(Model model) {
		boolean hasFields = false;
    	UserDefinedData message = new UserDefinedData();
    	    	    	    	    	
    	    	    	Integer commandTestField = model.getInteger("user_defined_data_command");
    	if (commandTestField != null) {
    		int commandField = commandTestField;
    		message.setCommand(commandField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer valueTestField = model.getInteger("user_defined_data_value");
    	if (valueTestField != null) {
    		int valueField = valueTestField;
    		message.setValue(valueField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt2TestField = model.getInteger("user_defined_data_userdef_int2");
    	if (userdefInt2TestField != null) {
    		int userdefInt2Field = userdefInt2TestField;
    		message.setUserdefInt2(userdefInt2Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt3TestField = model.getInteger("user_defined_data_userdef_int3");
    	if (userdefInt3TestField != null) {
    		int userdefInt3Field = userdefInt3TestField;
    		message.setUserdefInt3(userdefInt3Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt4TestField = model.getInteger("user_defined_data_userdef_int4");
    	if (userdefInt4TestField != null) {
    		int userdefInt4Field = userdefInt4TestField;
    		message.setUserdefInt4(userdefInt4Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt5TestField = model.getInteger("user_defined_data_userdef_int5");
    	if (userdefInt5TestField != null) {
    		int userdefInt5Field = userdefInt5TestField;
    		message.setUserdefInt5(userdefInt5Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt6TestField = model.getInteger("user_defined_data_userdef_int6");
    	if (userdefInt6TestField != null) {
    		int userdefInt6Field = userdefInt6TestField;
    		message.setUserdefInt6(userdefInt6Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt7TestField = model.getInteger("user_defined_data_userdef_int7");
    	if (userdefInt7TestField != null) {
    		int userdefInt7Field = userdefInt7TestField;
    		message.setUserdefInt7(userdefInt7Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt8TestField = model.getInteger("user_defined_data_userdef_int8");
    	if (userdefInt8TestField != null) {
    		int userdefInt8Field = userdefInt8TestField;
    		message.setUserdefInt8(userdefInt8Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt9TestField = model.getInteger("user_defined_data_userdef_int9");
    	if (userdefInt9TestField != null) {
    		int userdefInt9Field = userdefInt9TestField;
    		message.setUserdefInt9(userdefInt9Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt1TestField = model.getInteger("user_defined_data_userdef_int1");
    	if (userdefInt1TestField != null) {
    		int userdefInt1Field = userdefInt1TestField;
    		message.setUserdefInt1(userdefInt1Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer rxTestField = model.getInteger("user_defined_data_rx");
    	if (rxTestField != null) {
    		int rxField = rxTestField;
    		message.setRx(rxField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer ryTestField = model.getInteger("user_defined_data_ry");
    	if (ryTestField != null) {
    		int ryField = ryTestField;
    		message.setRy(ryField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer rzTestField = model.getInteger("user_defined_data_rz");
    	if (rzTestField != null) {
    		int rzField = rzTestField;
    		message.setRz(rzField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer rwTestField = model.getInteger("user_defined_data_rw");
    	if (rwTestField != null) {
    		int rwField = rwTestField;
    		message.setRw(rwField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer xTestField = model.getInteger("user_defined_data_x");
    	if (xTestField != null) {
    		int xField = xTestField;
    		message.setX(xField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer yTestField = model.getInteger("user_defined_data_y");
    	if (yTestField != null) {
    		int yField = yTestField;
    		message.setY(yField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer zTestField = model.getInteger("user_defined_data_z");
    	if (zTestField != null) {
    		int zField = zTestField;
    		message.setZ(zField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt10TestField = model.getInteger("user_defined_data_userdef_int10");
    	if (userdefInt10TestField != null) {
    		int userdefInt10Field = userdefInt10TestField;
    		message.setUserdefInt10(userdefInt10Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt11TestField = model.getInteger("user_defined_data_userdef_int11");
    	if (userdefInt11TestField != null) {
    		int userdefInt11Field = userdefInt11TestField;
    		message.setUserdefInt11(userdefInt11Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt12TestField = model.getInteger("user_defined_data_userdef_int12");
    	if (userdefInt12TestField != null) {
    		int userdefInt12Field = userdefInt12TestField;
    		message.setUserdefInt12(userdefInt12Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt13TestField = model.getInteger("user_defined_data_userdef_int13");
    	if (userdefInt13TestField != null) {
    		int userdefInt13Field = userdefInt13TestField;
    		message.setUserdefInt13(userdefInt13Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt14TestField = model.getInteger("user_defined_data_userdef_int14");
    	if (userdefInt14TestField != null) {
    		int userdefInt14Field = userdefInt14TestField;
    		message.setUserdefInt14(userdefInt14Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer userdefInt15TestField = model.getInteger("user_defined_data_userdef_int15");
    	if (userdefInt15TestField != null) {
    		int userdefInt15Field = userdefInt15TestField;
    		message.setUserdefInt15(userdefInt15Field);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public int getCommand() {
		return command;
	}
	
	public UserDefinedData setCommand(int command) {
		this.command = command;
		return this;	}
	
		            
		public int getValue() {
		return value;
	}
	
	public UserDefinedData setValue(int value) {
		this.value = value;
		return this;	}
	
		            
		public int getUserdefInt2() {
		return userdefInt2;
	}
	
	public UserDefinedData setUserdefInt2(int userdefInt2) {
		this.userdefInt2 = userdefInt2;
		return this;	}
	
		            
		public int getUserdefInt3() {
		return userdefInt3;
	}
	
	public UserDefinedData setUserdefInt3(int userdefInt3) {
		this.userdefInt3 = userdefInt3;
		return this;	}
	
		            
		public int getUserdefInt4() {
		return userdefInt4;
	}
	
	public UserDefinedData setUserdefInt4(int userdefInt4) {
		this.userdefInt4 = userdefInt4;
		return this;	}
	
		            
		public int getUserdefInt5() {
		return userdefInt5;
	}
	
	public UserDefinedData setUserdefInt5(int userdefInt5) {
		this.userdefInt5 = userdefInt5;
		return this;	}
	
		            
		public int getUserdefInt6() {
		return userdefInt6;
	}
	
	public UserDefinedData setUserdefInt6(int userdefInt6) {
		this.userdefInt6 = userdefInt6;
		return this;	}
	
		            
		public int getUserdefInt7() {
		return userdefInt7;
	}
	
	public UserDefinedData setUserdefInt7(int userdefInt7) {
		this.userdefInt7 = userdefInt7;
		return this;	}
	
		            
		public int getUserdefInt8() {
		return userdefInt8;
	}
	
	public UserDefinedData setUserdefInt8(int userdefInt8) {
		this.userdefInt8 = userdefInt8;
		return this;	}
	
		            
		public int getUserdefInt9() {
		return userdefInt9;
	}
	
	public UserDefinedData setUserdefInt9(int userdefInt9) {
		this.userdefInt9 = userdefInt9;
		return this;	}
	
		            
		public int getUserdefInt1() {
		return userdefInt1;
	}
	
	public UserDefinedData setUserdefInt1(int userdefInt1) {
		this.userdefInt1 = userdefInt1;
		return this;	}
	
		            
		public int getRx() {
		return rx;
	}
	
	public UserDefinedData setRx(int rx) {
		this.rx = rx;
		return this;	}
	
		            
		public int getRy() {
		return ry;
	}
	
	public UserDefinedData setRy(int ry) {
		this.ry = ry;
		return this;	}
	
		            
		public int getRz() {
		return rz;
	}
	
	public UserDefinedData setRz(int rz) {
		this.rz = rz;
		return this;	}
	
		            
		public int getRw() {
		return rw;
	}
	
	public UserDefinedData setRw(int rw) {
		this.rw = rw;
		return this;	}
	
		            
		public int getX() {
		return x;
	}
	
	public UserDefinedData setX(int x) {
		this.x = x;
		return this;	}
	
		            
		public int getY() {
		return y;
	}
	
	public UserDefinedData setY(int y) {
		this.y = y;
		return this;	}
	
		            
		public int getZ() {
		return z;
	}
	
	public UserDefinedData setZ(int z) {
		this.z = z;
		return this;	}
	
		            
		public int getUserdefInt10() {
		return userdefInt10;
	}
	
	public UserDefinedData setUserdefInt10(int userdefInt10) {
		this.userdefInt10 = userdefInt10;
		return this;	}
	
		            
		public int getUserdefInt11() {
		return userdefInt11;
	}
	
	public UserDefinedData setUserdefInt11(int userdefInt11) {
		this.userdefInt11 = userdefInt11;
		return this;	}
	
		            
		public int getUserdefInt12() {
		return userdefInt12;
	}
	
	public UserDefinedData setUserdefInt12(int userdefInt12) {
		this.userdefInt12 = userdefInt12;
		return this;	}
	
		            
		public int getUserdefInt13() {
		return userdefInt13;
	}
	
	public UserDefinedData setUserdefInt13(int userdefInt13) {
		this.userdefInt13 = userdefInt13;
		return this;	}
	
		            
		public int getUserdefInt14() {
		return userdefInt14;
	}
	
	public UserDefinedData setUserdefInt14(int userdefInt14) {
		this.userdefInt14 = userdefInt14;
		return this;	}
	
		            
		public int getUserdefInt15() {
		return userdefInt15;
	}
	
	public UserDefinedData setUserdefInt15(int userdefInt15) {
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
    	    	
    	    	
    	    	    	if( (Integer)message.command != null) {
            output.writeInt32(1, message.command, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.value != null) {
            output.writeInt32(2, message.value, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt2 != null) {
            output.writeInt32(3, message.userdefInt2, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt3 != null) {
            output.writeInt32(4, message.userdefInt3, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt4 != null) {
            output.writeInt32(5, message.userdefInt4, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt5 != null) {
            output.writeInt32(6, message.userdefInt5, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt6 != null) {
            output.writeInt32(7, message.userdefInt6, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt7 != null) {
            output.writeInt32(8, message.userdefInt7, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt8 != null) {
            output.writeInt32(9, message.userdefInt8, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt9 != null) {
            output.writeInt32(10, message.userdefInt9, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt1 != null) {
            output.writeInt32(11, message.userdefInt1, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rx != null) {
            output.writeInt32(12, message.rx, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.ry != null) {
            output.writeInt32(13, message.ry, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rz != null) {
            output.writeInt32(14, message.rz, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.rw != null) {
            output.writeInt32(15, message.rw, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.x != null) {
            output.writeSInt32(16, message.x, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.y != null) {
            output.writeSInt32(17, message.y, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.z != null) {
            output.writeSInt32(18, message.z, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt10 != null) {
            output.writeInt32(19, message.userdefInt10, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt11 != null) {
            output.writeInt32(20, message.userdefInt11, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt12 != null) {
            output.writeInt32(21, message.userdefInt12, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt13 != null) {
            output.writeInt32(22, message.userdefInt13, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt14 != null) {
            output.writeInt32(23, message.userdefInt14, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.userdefInt15 != null) {
            output.writeInt32(24, message.userdefInt15, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START UserDefinedData");
    	    	//if(this.command != null) {
    		System.out.println("command="+this.command);
    	//}
    	    	//if(this.value != null) {
    		System.out.println("value="+this.value);
    	//}
    	    	//if(this.userdefInt2 != null) {
    		System.out.println("userdefInt2="+this.userdefInt2);
    	//}
    	    	//if(this.userdefInt3 != null) {
    		System.out.println("userdefInt3="+this.userdefInt3);
    	//}
    	    	//if(this.userdefInt4 != null) {
    		System.out.println("userdefInt4="+this.userdefInt4);
    	//}
    	    	//if(this.userdefInt5 != null) {
    		System.out.println("userdefInt5="+this.userdefInt5);
    	//}
    	    	//if(this.userdefInt6 != null) {
    		System.out.println("userdefInt6="+this.userdefInt6);
    	//}
    	    	//if(this.userdefInt7 != null) {
    		System.out.println("userdefInt7="+this.userdefInt7);
    	//}
    	    	//if(this.userdefInt8 != null) {
    		System.out.println("userdefInt8="+this.userdefInt8);
    	//}
    	    	//if(this.userdefInt9 != null) {
    		System.out.println("userdefInt9="+this.userdefInt9);
    	//}
    	    	//if(this.userdefInt1 != null) {
    		System.out.println("userdefInt1="+this.userdefInt1);
    	//}
    	    	//if(this.rx != null) {
    		System.out.println("rx="+this.rx);
    	//}
    	    	//if(this.ry != null) {
    		System.out.println("ry="+this.ry);
    	//}
    	    	//if(this.rz != null) {
    		System.out.println("rz="+this.rz);
    	//}
    	    	//if(this.rw != null) {
    		System.out.println("rw="+this.rw);
    	//}
    	    	//if(this.x != null) {
    		System.out.println("x="+this.x);
    	//}
    	    	//if(this.y != null) {
    		System.out.println("y="+this.y);
    	//}
    	    	//if(this.z != null) {
    		System.out.println("z="+this.z);
    	//}
    	    	//if(this.userdefInt10 != null) {
    		System.out.println("userdefInt10="+this.userdefInt10);
    	//}
    	    	//if(this.userdefInt11 != null) {
    		System.out.println("userdefInt11="+this.userdefInt11);
    	//}
    	    	//if(this.userdefInt12 != null) {
    		System.out.println("userdefInt12="+this.userdefInt12);
    	//}
    	    	//if(this.userdefInt13 != null) {
    		System.out.println("userdefInt13="+this.userdefInt13);
    	//}
    	    	//if(this.userdefInt14 != null) {
    		System.out.println("userdefInt14="+this.userdefInt14);
    	//}
    	    	//if(this.userdefInt15 != null) {
    		System.out.println("userdefInt15="+this.userdefInt15);
    	//}
    	    	System.out.println("END UserDefinedData");
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
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, UserDefinedData.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
