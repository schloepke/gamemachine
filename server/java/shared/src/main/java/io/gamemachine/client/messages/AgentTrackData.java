
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
public final class AgentTrackData implements Externalizable, Message<AgentTrackData>, Schema<AgentTrackData>{



    public static Schema<AgentTrackData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AgentTrackData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AgentTrackData DEFAULT_INSTANCE = new AgentTrackData();

        public List<TrackData> trackData;
	  
    public AgentTrackData()
    {
        
    }


	

	    
    public Boolean hasTrackData()  {
        return trackData == null ? false : true;
    }
        
		public List<TrackData> getTrackDataList() {
		if(this.trackData == null)
            this.trackData = new ArrayList<TrackData>();
		return trackData;
	}

	public AgentTrackData setTrackDataList(List<TrackData> trackData) {
		this.trackData = trackData;
		return this;
	}

	public TrackData getTrackData(int index)  {
        return trackData == null ? null : trackData.get(index);
    }

    public int getTrackDataCount()  {
        return trackData == null ? 0 : trackData.size();
    }

    public AgentTrackData addTrackData(TrackData trackData)  {
        if(this.trackData == null)
            this.trackData = new ArrayList<TrackData>();
        this.trackData.add(trackData);
        return this;
    }
            	    	    	    	
    public AgentTrackData removeTrackDataByIx(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.ix.equals(obj.ix)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByIy(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.iy.equals(obj.iy)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByIz(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.iz.equals(obj.iz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataById(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.id.equals(obj.id)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByX(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.x.equals(obj.x)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByY(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.y.equals(obj.y)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByZ(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.z.equals(obj.z)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public AgentTrackData removeTrackDataByGridName(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.gridName.equals(obj.gridName)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByGetNeighbors(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.getNeighbors.equals(obj.getNeighbors)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	
    public AgentTrackData removeTrackDataByShortId(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.shortId.equals(obj.shortId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public AgentTrackData removeTrackDataByBroadcast(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.broadcast.equals(obj.broadcast)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByCharacterId(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.characterId.equals(obj.characterId)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByRx(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.rx.equals(obj.rx)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByRy(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.ry.equals(obj.ry)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByRz(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.rz.equals(obj.rz)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByRw(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.rw.equals(obj.rw)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByVaxis(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.vaxis.equals(obj.vaxis)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByHaxis(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.haxis.equals(obj.haxis)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataBySpeed(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.speed.equals(obj.speed)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByVelX(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.velX.equals(obj.velX)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByVelZ(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.velZ.equals(obj.velZ)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByZone(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.zone.equals(obj.zone)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByHidden(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.hidden.equals(obj.hidden)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public AgentTrackData removeTrackDataByYaxis(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.yaxis.equals(obj.yaxis)) {
    	      			itr.remove();
    		}
		}
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

    public Schema<AgentTrackData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AgentTrackData newMessage()
    {
        return new AgentTrackData();
    }

    public Class<AgentTrackData> typeClass()
    {
        return AgentTrackData.class;
    }

    public String messageName()
    {
        return AgentTrackData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AgentTrackData.class.getName();
    }

    public boolean isInitialized(AgentTrackData message)
    {
        return true;
    }

    public void mergeFrom(Input input, AgentTrackData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	            		if(message.trackData == null)
                        message.trackData = new ArrayList<TrackData>();
                                        message.trackData.add(input.mergeObject(null, TrackData.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, AgentTrackData message) throws IOException
    {
    	    	
    	    	
    	    	if(message.trackData != null)
        {
            for(TrackData trackData : message.trackData)
            {
                if(trackData != null) {
                   	    				output.writeObject(1, trackData, TrackData.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "trackData";
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
    	    	__fieldMap.put("trackData", 1);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = AgentTrackData.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static AgentTrackData parseFrom(byte[] bytes) {
	AgentTrackData message = new AgentTrackData();
	ProtobufIOUtil.mergeFrom(bytes, message, AgentTrackData.getSchema());
	return message;
}

public static AgentTrackData parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	AgentTrackData message = new AgentTrackData();
	JsonIOUtil.mergeFrom(bytes, message, AgentTrackData.getSchema(), false);
	return message;
}

public AgentTrackData clone() {
	byte[] bytes = this.toByteArray();
	AgentTrackData agentTrackData = AgentTrackData.parseFrom(bytes);
	return agentTrackData;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, AgentTrackData.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<AgentTrackData> schema = AgentTrackData.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, AgentTrackData.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
