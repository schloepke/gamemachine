
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
public final class BuildObjects implements Externalizable, Message<BuildObjects>, Schema<BuildObjects>{



    public static Schema<BuildObjects> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BuildObjects getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BuildObjects DEFAULT_INSTANCE = new BuildObjects();

        public List<BuildObject> buildObject;
	    			public Integer action;
	    
        			public Integer requestedUpdateId;
	    
        			public Integer currentUpdate;
	    
      
    public BuildObjects()
    {
        
    }


	

	    
    public Boolean hasBuildObject()  {
        return buildObject == null ? false : true;
    }
        
		public List<BuildObject> getBuildObjectList() {
		if(this.buildObject == null)
            this.buildObject = new ArrayList<BuildObject>();
		return buildObject;
	}

	public BuildObjects setBuildObjectList(List<BuildObject> buildObject) {
		this.buildObject = buildObject;
		return this;
	}

	public BuildObject getBuildObject(int index)  {
        return buildObject == null ? null : buildObject.get(index);
    }

    public int getBuildObjectCount()  {
        return buildObject == null ? 0 : buildObject.size();
    }

    public BuildObjects addBuildObject(BuildObject buildObject)  {
        if(this.buildObject == null)
            this.buildObject = new ArrayList<BuildObject>();
        this.buildObject.add(buildObject);
        return this;
    }
            	    	    	    	
    public BuildObjects removeBuildObjectByPlayerItemId(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.playerItemId.equals(obj.playerItemId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByAction(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.action.equals(obj.action)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectById(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByRecordId(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.recordId.equals(obj.recordId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByOwnerId(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.ownerId.equals(obj.ownerId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByX(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByY(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByZ(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByRx(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.rx.equals(obj.rx)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByRy(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.ry.equals(obj.ry)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByRz(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.rz.equals(obj.rz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByRw(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.rw.equals(obj.rw)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByHealth(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.health.equals(obj.health)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByTemplateId(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.templateId.equals(obj.templateId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByGrid(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.grid.equals(obj.grid)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByUpdatedAt(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.updatedAt.equals(obj.updatedAt)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByState(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.state.equals(obj.state)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByUpdateId(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.updateId.equals(obj.updateId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByIsFloor(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.isFloor.equals(obj.isFloor)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByIsDestructable(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.isDestructable.equals(obj.isDestructable)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByHasDoor(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.hasDoor.equals(obj.hasDoor)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByDoorStatus(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.doorStatus.equals(obj.doorStatus)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByGroundBlockObject(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.groundBlockObject.equals(obj.groundBlockObject)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByIsGroundBlock(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.isGroundBlock.equals(obj.isGroundBlock)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByChunk(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.chunk.equals(obj.chunk)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByTerrainEdit(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.terrainEdit.equals(obj.terrainEdit)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByIsTerrainEdit(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.isTerrainEdit.equals(obj.isTerrainEdit)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByCustomBytes(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.customBytes.equals(obj.customBytes)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByCustomString(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.customString.equals(obj.customString)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByTextureId(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.textureId.equals(obj.textureId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectBySlots(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.slots.equals(obj.slots)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public BuildObjects removeBuildObjectByPlacedAt(BuildObject buildObject)  {
    	if(this.buildObject == null)
           return this;
            
       	Iterator<BuildObject> itr = this.buildObject.iterator();
       	while (itr.hasNext()) {
    	BuildObject obj = itr.next();
    	
    	    		if (buildObject.placedAt.equals(obj.placedAt)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	        	
    
    
    
		    
    public Boolean hasAction()  {
        return action == null ? false : true;
    }
        
		public Integer getAction() {
		return action;
	}
	
	public BuildObjects setAction(Integer action) {
		this.action = action;
		return this;	}
	
		    
    public Boolean hasRequestedUpdateId()  {
        return requestedUpdateId == null ? false : true;
    }
        
		public Integer getRequestedUpdateId() {
		return requestedUpdateId;
	}
	
	public BuildObjects setRequestedUpdateId(Integer requestedUpdateId) {
		this.requestedUpdateId = requestedUpdateId;
		return this;	}
	
		    
    public Boolean hasCurrentUpdate()  {
        return currentUpdate == null ? false : true;
    }
        
		public Integer getCurrentUpdate() {
		return currentUpdate;
	}
	
	public BuildObjects setCurrentUpdate(Integer currentUpdate) {
		this.currentUpdate = currentUpdate;
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

    public Schema<BuildObjects> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BuildObjects newMessage()
    {
        return new BuildObjects();
    }

    public Class<BuildObjects> typeClass()
    {
        return BuildObjects.class;
    }

    public String messageName()
    {
        return BuildObjects.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BuildObjects.class.getName();
    }

    public boolean isInitialized(BuildObjects message)
    {
        return true;
    }

    public void mergeFrom(Input input, BuildObjects message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.buildObject == null)
                        message.buildObject = new ArrayList<BuildObject>();
                                        message.buildObject.add(input.mergeObject(null, BuildObject.getSchema()));
                                        break;
                            	            	case 2:
            	                	                	message.action = input.readInt32();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.requestedUpdateId = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.currentUpdate = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BuildObjects message) throws IOException
    {
    	    	
    	    	
    	    	if(message.buildObject != null)
        {
            for(BuildObject buildObject : message.buildObject)
            {
                if(buildObject != null) {
                   	    				output.writeObject(1, buildObject, BuildObject.getSchema(), true);
    				    			}
            }
        }
    	            	
    	    	
    	    	    	if(message.action != null)
            output.writeInt32(2, message.action, false);
    	    	
    	            	
    	    	
    	    	    	if(message.requestedUpdateId != null)
            output.writeInt32(3, message.requestedUpdateId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.currentUpdate != null)
            output.writeInt32(4, message.currentUpdate, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "buildObject";
        	        	case 2: return "action";
        	        	case 3: return "requestedUpdateId";
        	        	case 4: return "currentUpdate";
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
    	    	__fieldMap.put("buildObject", 1);
    	    	__fieldMap.put("action", 2);
    	    	__fieldMap.put("requestedUpdateId", 3);
    	    	__fieldMap.put("currentUpdate", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = BuildObjects.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static BuildObjects parseFrom(byte[] bytes) {
	BuildObjects message = new BuildObjects();
	ProtobufIOUtil.mergeFrom(bytes, message, BuildObjects.getSchema());
	return message;
}

public static BuildObjects parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	BuildObjects message = new BuildObjects();
	JsonIOUtil.mergeFrom(bytes, message, BuildObjects.getSchema(), false);
	return message;
}

public BuildObjects clone() {
	byte[] bytes = this.toByteArray();
	BuildObjects buildObjects = BuildObjects.parseFrom(bytes);
	return buildObjects;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, BuildObjects.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<BuildObjects> schema = BuildObjects.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, BuildObjects.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
