package org.livoniawarriors.Robot2019;

import edu.wpi.first.hal.can.CANJNI;
import edu.wpi.first.hal.can.CANMessageNotFoundException;
import edu.wpi.first.hal.util.UncleanStatusException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightDrive {
    private ByteBuffer sendBuffer;
    private byte[] recieveArray;
    private ByteBuffer recieveID;
    private ByteBuffer timestampBuffer;

    private final int CAN_ID = 33882112; //given by lib.
    private final int MSG_ID_MASK = 536870911; //given by lib.

    public LightDrive() {
        sendBuffer = ByteBuffer.allocate(16);
        recieveID = ByteBuffer.allocateDirect(4);
        recieveID.order(ByteOrder.LITTLE_ENDIAN);
        timestampBuffer = ByteBuffer.allocateDirect(4);
        timestampBuffer.order(ByteOrder.LITTLE_ENDIAN);
        //buffer values are written in order from least to most significant figures
    }

    public void update() {
        //attempts to send message
        byte[] tempSend = new byte[8];
        try {
            //sends first 8 bytes
            sendBuffer.get(tempSend, 0, 8);
            CANJNI.FRCNetCommCANSessionMuxSendMessage(CAN_ID, tempSend, 100); //library used 100ms
            //sends second 8 bytes
            sendBuffer.get(tempSend, 0, 8);
            CANJNI.FRCNetCommCANSessionMuxSendMessage(CAN_ID + 1, tempSend, 100);
        }
        catch(UncleanStatusException e) {
            System.err.println(e.getMessage());
        }

        //attempts to recieve message
        recieveID.rewind();
        recieveID.putInt(CAN_ID + 4);
        try {
            recieveArray = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(recieveID.asIntBuffer(), 
                MSG_ID_MASK, timestampBuffer);
        }
        catch (CANMessageNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setColor(int channel, Color color) {
        if(channel > 1 && channel < 4) {
            channel = (channel - 1) * 3;
            sendBuffer.putShort(channel, color.getGreen());
            sendBuffer.putShort(channel + 1, color.getRed());
            sendBuffer.putShort(channel + 2, color.getBlue());
        }
    }

    public void setColor(int channel, Color color, double brightness) {
        if((channel > 1 && channel < 4) && (brightness <= 1 && brightness > 0)) {
            channel = (channel - 1) * 3;
            sendBuffer.putShort(channel, (short)(color.getGreen() * brightness));
            sendBuffer.putShort(channel + 1, (short)(color.getRed() * brightness));
            sendBuffer.putShort(channel + 2, (short)(color.getBlue() * brightness));
        }
    }
}