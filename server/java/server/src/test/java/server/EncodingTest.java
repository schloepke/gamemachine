package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import io.gamemachine.messages.TrackData;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufOutput;

@Test
public class EncodingTest {

    private static final Logger logger = LoggerFactory.getLogger(EncodingTest.class);

    public void trackDataTest() {
        TrackData trackData = new TrackData();
        trackData.x = 555;
        trackData.y = 555;
        trackData.z = 555;

        byte[] data = trackData.toByteArray();
        //logger.warn("TrackData length " + data.length);

        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ProtobufOutput output = new ProtobufOutput(buffer);
        try {
            output.writeInt32(1, 555, false);
            output.writeInt32(1, 555, false);
            output.writeInt32(1, 555, false);
            data = output.toByteArray();
            //logger.warn("Raw length " + data.length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    // public void parseProcess() {
    // ExternalProcess process;
    // process = new ExternalProcess("blah","notrunning");
    // assertThat(process.isRunning()).isFalse();
    //
    // process = new ExternalProcess("blah","update-notifier");
    // //assertThat(process.isRunning()).isTrue();
    // }
}
