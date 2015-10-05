
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



import io.gamemachine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class BuildableArea implements Externalizable, Message<BuildableArea>, Schema<BuildableArea>{



    public static Schema<BuildableArea> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildableArea getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildableArea DEFAULT_INSTANCE = new BuildableArea();
    static final String defaultScope = BuildableArea.class.getSimpleName();

    	
	    	    public String ownerId= null;
	    		
    
        	
	    	    public float px= 0F;
	    		
    
        	
	    	    public float py= 0F;
	    		
    
        	
	    	    public float pz= 0F;
	    		
    
        	
	    	    public int sx= 0;
	    		
    
        	
	    	    public int sy= 0;
	    		
    
        	
	    	    public int sz= 0;
	    		
    
        	
	    	    public int recordId= 0;
	    		
    
        


    public BuildableArea()
    {
        
    }


	

	public static BuildableAreaDb db() {
		return BuildableAreaDb.getInstance();
	}
	
	public interface BuildableAreaAsyncDb {
		void save(BuildableArea message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class BuildableAreaAsyncDbImpl implements BuildableAreaAsyncDb {
	
		public void save(BuildableArea message) {
			BuildableArea.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	BuildableArea.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	BuildableArea.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class BuildableAreaDb {
	
		public Errors dbErrors;
		private BuildableAreaAsyncDb asyncDb = null;
		
		private BuildableAreaDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<BuildableAreaAsyncDbImpl>(BuildableAreaAsyncDb.class, BuildableAreaAsyncDbImpl.class));
			}
		}
		
		public void stop() {
			if (asyncDb != null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				TypedActor.get(system).stop(asyncDb);
				asyncDb = null;
			}
		}
		
		private static class LazyHolder {
			private static final BuildableAreaDb INSTANCE = new BuildableAreaDb();
		}
	
		public static BuildableAreaDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(BuildableArea message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(BuildableArea message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(BuildableArea message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildableArea.open();
	    	}
	    	
	    	io.gamemachine.orm.models.BuildableArea model = null;
	    	//if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.BuildableArea.findFirst("id = ?", message.recordId);
	    	//}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.BuildableArea();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		dbErrors = model.errors();
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildableArea.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.BuildableArea.open();
	    	int deleted = io.gamemachine.orm.models.BuildableArea.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.BuildableArea.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.BuildableArea.open();
	    	int deleted = io.gamemachine.orm.models.BuildableArea.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.BuildableArea.close();
	    	return result;
	    }
	    
	    public BuildableArea find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public BuildableArea find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildableArea.open();
	    	}
	    	
	    	io.gamemachine.orm.models.BuildableArea model = io.gamemachine.orm.models.BuildableArea.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildableArea.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		BuildableArea buildableArea = fromModel(model);
	    			    		return buildableArea;
	    	}
	    }
	    
	    public BuildableArea findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.BuildableArea.open();
	    	io.gamemachine.orm.models.BuildableArea model = io.gamemachine.orm.models.BuildableArea.findFirst(query, params);
	    	io.gamemachine.orm.models.BuildableArea.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		BuildableArea buildableArea = fromModel(model);
	    			    		return buildableArea;
	    	}
	    }
	    
	    public List<BuildableArea> findAll() {
	    	io.gamemachine.orm.models.BuildableArea.open();
	    	List<io.gamemachine.orm.models.BuildableArea> models = io.gamemachine.orm.models.BuildableArea.findAll();
	    	List<BuildableArea> messages = new ArrayList<BuildableArea>();
	    	for (io.gamemachine.orm.models.BuildableArea model : models) {
	    		BuildableArea buildableArea = fromModel(model);
	    			    		messages.add(buildableArea);
	    	}
	    	io.gamemachine.orm.models.BuildableArea.close();
	    	return messages;
	    }
	    
	    public List<BuildableArea> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<BuildableArea> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildableArea.open();
	    	}
	    	List<io.gamemachine.orm.models.BuildableArea> models = io.gamemachine.orm.models.BuildableArea.where(query, params);
	    	List<BuildableArea> messages = new ArrayList<BuildableArea>();
	    	for (io.gamemachine.orm.models.BuildableArea model : models) {
	    		BuildableArea buildableArea = fromModel(model);
	    			    		messages.add(buildableArea);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.BuildableArea.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("buildable_area_owner_id",null);
    	    	    	    	    	    	model.set("buildable_area_px",null);
    	    	    	    	    	    	model.set("buildable_area_py",null);
    	    	    	    	    	    	model.set("buildable_area_pz",null);
    	    	    	    	    	    	model.set("buildable_area_sx",null);
    	    	    	    	    	    	model.set("buildable_area_sy",null);
    	    	    	    	    	    	model.set("buildable_area_sz",null);
    	    	    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (ownerId != null) {
    	       	    	model.setString("buildable_area_owner_id",ownerId);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (px != null) {
    	       	    	model.setFloat("buildable_area_px",px);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (py != null) {
    	       	    	model.setFloat("buildable_area_py",py);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (pz != null) {
    	       	    	model.setFloat("buildable_area_pz",pz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (sx != null) {
    	       	    	model.setInteger("buildable_area_sx",sx);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (sy != null) {
    	       	    	model.setInteger("buildable_area_sy",sy);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (sz != null) {
    	       	    	model.setInteger("buildable_area_sz",sz);
    	        		
    	//}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    }
    
	public static BuildableArea fromModel(Model model) {
		boolean hasFields = false;
    	BuildableArea message = new BuildableArea();
    	    	    	    	    	
    	    	    	String ownerIdTestField = model.getString("buildable_area_owner_id");
    	if (ownerIdTestField != null) {
    		String ownerIdField = ownerIdTestField;
    		message.setOwnerId(ownerIdField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Float pxTestField = model.getFloat("buildable_area_px");
    	if (pxTestField != null) {
    		float pxField = pxTestField;
    		message.setPx(pxField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Float pyTestField = model.getFloat("buildable_area_py");
    	if (pyTestField != null) {
    		float pyField = pyTestField;
    		message.setPy(pyField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Float pzTestField = model.getFloat("buildable_area_pz");
    	if (pzTestField != null) {
    		float pzField = pzTestField;
    		message.setPz(pzField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer sxTestField = model.getInteger("buildable_area_sx");
    	if (sxTestField != null) {
    		int sxField = sxTestField;
    		message.setSx(sxField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer syTestField = model.getInteger("buildable_area_sy");
    	if (syTestField != null) {
    		int syField = syTestField;
    		message.setSy(syField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Integer szTestField = model.getInteger("buildable_area_sz");
    	if (szTestField != null) {
    		int szField = szTestField;
    		message.setSz(szField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	//if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	//}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getOwnerId() {
		return ownerId;
	}
	
	public BuildableArea setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;	}
	
		            
		public float getPx() {
		return px;
	}
	
	public BuildableArea setPx(float px) {
		this.px = px;
		return this;	}
	
		            
		public float getPy() {
		return py;
	}
	
	public BuildableArea setPy(float py) {
		this.py = py;
		return this;	}
	
		            
		public float getPz() {
		return pz;
	}
	
	public BuildableArea setPz(float pz) {
		this.pz = pz;
		return this;	}
	
		            
		public int getSx() {
		return sx;
	}
	
	public BuildableArea setSx(int sx) {
		this.sx = sx;
		return this;	}
	
		            
		public int getSy() {
		return sy;
	}
	
	public BuildableArea setSy(int sy) {
		this.sy = sy;
		return this;	}
	
		            
		public int getSz() {
		return sz;
	}
	
	public BuildableArea setSz(int sz) {
		this.sz = sz;
		return this;	}
	
		            
		public int getRecordId() {
		return recordId;
	}
	
	public BuildableArea setRecordId(int recordId) {
		this.recordId = recordId;
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

    public Schema<BuildableArea> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildableArea newMessage()
    {
        return new BuildableArea();
    }

    public Class<BuildableArea> typeClass()
    {
        return BuildableArea.class;
    }

    public String messageName()
    {
        return BuildableArea.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildableArea.class.getName();
    }

    public boolean isInitialized(BuildableArea message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildableArea message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.ownerId = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.px = input.readFloat();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.py = input.readFloat();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.pz = input.readFloat();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.sx = input.readInt32();
                	break;
                	                	
                            	            	case 6:
            	                	                	message.sy = input.readInt32();
                	break;
                	                	
                            	            	case 7:
            	                	                	message.sz = input.readInt32();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildableArea message) throws IOException
    {
    	    	
    	    	//if(message.ownerId == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.ownerId != null) {
            output.writeString(1, message.ownerId, false);
        }
    	    	
    	            	
    	    	//if(message.px == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Float)message.px != null) {
            output.writeFloat(2, message.px, false);
        }
    	    	
    	            	
    	    	//if(message.py == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Float)message.py != null) {
            output.writeFloat(3, message.py, false);
        }
    	    	
    	            	
    	    	//if(message.pz == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Float)message.pz != null) {
            output.writeFloat(4, message.pz, false);
        }
    	    	
    	            	
    	    	//if(message.sx == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.sx != null) {
            output.writeInt32(5, message.sx, false);
        }
    	    	
    	            	
    	    	//if(message.sy == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.sy != null) {
            output.writeInt32(6, message.sy, false);
        }
    	    	
    	            	
    	    	//if(message.sz == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Integer)message.sz != null) {
            output.writeInt32(7, message.sz, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Integer)message.recordId != null) {
            output.writeInt32(8, message.recordId, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START BuildableArea");
    	    	//if(this.ownerId != null) {
    		System.out.println("ownerId="+this.ownerId);
    	//}
    	    	//if(this.px != null) {
    		System.out.println("px="+this.px);
    	//}
    	    	//if(this.py != null) {
    		System.out.println("py="+this.py);
    	//}
    	    	//if(this.pz != null) {
    		System.out.println("pz="+this.pz);
    	//}
    	    	//if(this.sx != null) {
    		System.out.println("sx="+this.sx);
    	//}
    	    	//if(this.sy != null) {
    		System.out.println("sy="+this.sy);
    	//}
    	    	//if(this.sz != null) {
    		System.out.println("sz="+this.sz);
    	//}
    	    	//if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	//}
    	    	System.out.println("END BuildableArea");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "ownerId";
        	        	case 2: return "px";
        	        	case 3: return "py";
        	        	case 4: return "pz";
        	        	case 5: return "sx";
        	        	case 6: return "sy";
        	        	case 7: return "sz";
        	        	case 8: return "recordId";
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
    	    	__fieldMap.put("ownerId", 1);
    	    	__fieldMap.put("px", 2);
    	    	__fieldMap.put("py", 3);
    	    	__fieldMap.put("pz", 4);
    	    	__fieldMap.put("sx", 5);
    	    	__fieldMap.put("sy", 6);
    	    	__fieldMap.put("sz", 7);
    	    	__fieldMap.put("recordId", 8);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildableArea.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildableArea parseFrom(byte[] bytes) {
	BuildableArea message = new BuildableArea();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildableArea.getSchema());
	return message;
}

public static BuildableArea parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildableArea message = new BuildableArea();
	JsonIOUtil.mergeFrom(bytes, message, BuildableArea.getSchema(), false);
	return message;
}

public BuildableArea clone() {
	byte[] bytes = this.toByteArray();
	BuildableArea buildableArea = BuildableArea.parseFrom(bytes);
	return buildableArea;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildableArea.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildableArea> schema = BuildableArea.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildableArea.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, BuildableArea.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
