package org.livoniawarriors.Robot2019.subsystems.flamethrower;

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

    private int CAN_ID; //given by lib.
    private static int NUM_DRIVES = 0;
    private final int MSG_ID_MASK = 536870911; //given by lib.

    public LightDrive() {
        CAN_ID = 33882112 + (NUM_DRIVES);
        NUM_DRIVES++;
        System.out.println(CAN_ID + " " + NUM_DRIVES);
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
            System.out.println(e.getMessage());
        }
        
        //attempts to recieve message
        recieveID.putInt(CAN_ID + 4);
        recieveID.rewind();
        try {
            recieveArray = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(recieveID.asIntBuffer(), 
                MSG_ID_MASK, timestampBuffer);
        }
        catch (CANMessageNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setColor(int channel, Color color) {
        if(channel >= 1 && channel <= 4) {
            channel = ((channel - 1) * 3);
            sendBuffer.array()[channel] = (byte)color.getGreen();
            sendBuffer.array()[channel + 1] = (byte)color.getRed();
            sendBuffer.array()[channel + 2] = (byte)color.getBlue();
        }
    }

    public void setColor(int channel, Color color, double brightness) {
        if((channel >= 1 && channel <= 4) && (brightness <= 1 && brightness > 0)) {
            channel = (channel - 1) * 3;
            sendBuffer.array()[channel] = (byte)(color.getGreen()*brightness);
            sendBuffer.array()[channel + 1] = (byte)(color.getRed()*brightness);
            sendBuffer.array()[channel + 2] = (byte)(color.getBlue()*brightness);
        }
    }
}