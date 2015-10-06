
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
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class Neighbors implements Externalizable, Message<Neighbors>, Schema<Neighbors>{

private static final Logger logger = LoggerFactory.getLogger(Neighbors.class);



    public static Schema<Neighbors> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Neighbors getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Neighbors DEFAULT_INSTANCE = new Neighbors();
    static final String defaultScope = Neighbors.class.getSimpleName();

        public List<TrackData> trackData;
	    


    public Neighbors()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    }
    
	public void toModel(Model model) {
    	    	    }
    
	public static Neighbors fromModel(Model model) {
		boolean hasFields = false;
    	Neighbors message = new Neighbors();
    	    			if (hasFields) {
			return message;
		} else {
			return null;
		}
    }


	            
		public List<TrackData> getTrackDataList() {
		if(this.trackData == null)
            this.trackData = new ArrayList<TrackData>();
		return trackData;
	}

	public Neighbors setTrackDataList(List<TrackData> trackData) {
		this.trackData = trackData;
		return this;
	}

	public TrackData getTrackData(int index)  {
        return trackData == null ? null : trackData.get(index);
    }

    public int getTrackDataCount()  {
        return trackData == null ? 0 : trackData.size();
    }

    public Neighbors addTrackData(TrackData trackData)  {
        if(this.trackData == null)
            this.trackData = new ArrayList<TrackData>();
        this.trackData.add(trackData);
        return this;
    }
            	    	    	    	
    public Neighbors removeTrackDataByIx(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.ix == obj.ix) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByIy(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.iy == obj.iy) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByIz(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.iz == obj.iz) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataById(TrackData trackData)  {
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
    
        	    	    	    	
    public Neighbors removeTrackDataByX(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.x == obj.x) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByY(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.y == obj.y) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByZ(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.z == obj.z) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public Neighbors removeTrackDataByGridName(TrackData trackData)  {
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
    
        	    	    	    	
    public Neighbors removeTrackDataByGetNeighbors(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.getNeighbors == obj.getNeighbors) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	    	
    public Neighbors removeTrackDataByShortId(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.shortId == obj.shortId) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	    	
    public Neighbors removeTrackDataByBroadcast(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.broadcast == obj.broadcast) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByCharacterId(TrackData trackData)  {
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
    
        	    	    	    	
    public Neighbors removeTrackDataByRx(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.rx == obj.rx) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByRy(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.ry == obj.ry) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByRz(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.rz == obj.rz) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByRw(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.rw == obj.rw) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByVaxis(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.vaxis == obj.vaxis) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByHaxis(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.haxis == obj.haxis) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataBySpeed(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.speed == obj.speed) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByVelX(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.velX == obj.velX) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByVelZ(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.velZ == obj.velZ) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByZone(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.zone == obj.zone) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByHidden(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.hidden == obj.hidden) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
        	    	    	    	
    public Neighbors removeTrackDataByYaxis(TrackData trackData)  {
    	if(this.trackData == null)
           return this;
            
       	Iterator<TrackData> itr = this.trackData.iterator();
       	while (itr.hasNext()) {
    	TrackData obj = itr.next();
    	
    	    		if (trackData.yaxis == obj.yaxis) {
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

    public Schema<Neighbors> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Neighbors newMessage()
    {
        return new Neighbors();
    }

    public Class<Neighbors> typeClass()
    {
        return Neighbors.class;
    }

    public String messageName()
    {
        return Neighbors.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Neighbors.class.getName();
    }

    public boolean isInitialized(Neighbors message)
    {
        return true;
    }

    public void mergeFrom(Input input, Neighbors message) throws IOException
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


    public void writeTo(Output output, Neighbors message) throws IOException
    {
    	    	
    	    	
    	    	if(message.trackData != null)
        {
            for(TrackData trackData : message.trackData)
            {
                if( (TrackData) trackData != null) {
                   	    				output.writeObject(1, trackData, TrackData.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Neighbors");
    	    	//if(this.trackData != null) {
    		System.out.println("trackData="+this.trackData);
    	//}
    	    	System.out.println("END Neighbors");
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
		fieldName = Neighbors.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Neighbors parseFrom(byte[] bytes) {
	Neighbors message = new Neighbors();
	ProtobufIOUtil.mergeFrom(bytes, message, Neighbors.getSchema());
	return message;
}

public static Neighbors parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Neighbors message = new Neighbors();
	JsonIOUtil.mergeFrom(bytes, message, Neighbors.getSchema(), false);
	return message;
}

public Neighbors clone() {
	byte[] bytes = this.toByteArray();
	Neighbors neighbors = Neighbors.parseFrom(bytes);
	return neighbors;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Neighbors.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Neighbors> schema = Neighbors.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Neighbors.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Neighbors.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
