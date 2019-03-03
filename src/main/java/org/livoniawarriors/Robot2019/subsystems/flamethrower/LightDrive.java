package org.livoniawarriors.Robot2019.subsystems.flamethrower;

import edu.wpi.first.hal.can.CANJNI;
import edu.wpi.first.hal.can.CANMessageNotFoundException;
import edu.wpi.first.hal.util.UncleanStatusException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.livoniawarriors.Robot2019.Robot;

public class LightDrive {
    private ByteBuffer sendBuffer;
    private byte[] receiveArray;
    private ByteBuffer receiveID;
    private ByteBuffer timestampBuffer;

    private int CAN_ID; //given by lib.
    private static int NUM_DRIVES = 0;
    private final int MSG_ID_MASK = 536870911; //given by lib.

    public LightDrive() {
        CAN_ID = 33882112 + (NUM_DRIVES);
        NUM_DRIVES++;
        sendBuffer = ByteBuffer.allocate(16);
        receiveID = ByteBuffer.allocateDirect(4);
        receiveID.order(ByteOrder.LITTLE_ENDIAN);
        timestampBuffer = ByteBuffer.allocateDirect(4);
        timestampBuffer.order(ByteOrder.LITTLE_ENDIAN);
        //buffer values are written in order from least to most significant figures
        }

    public void update() {
        //attempts to send message
        byte[] tempSend1 = new byte[8];
        byte[]tempSend2 = new byte[8];

        try {
            //sends first 8 bytes
            sendBuffer.get(tempSend1, 0, 6);

            CANJNI.FRCNetCommCANSessionMuxSendMessage(CAN_ID, tempSend1, 100); //library used 100ms
            //sends second 8 bytes
            sendBuffer.get(tempSend2, 0, 6);

            CANJNI.FRCNetCommCANSessionMuxSendMessage(CAN_ID + 1, tempSend2, 100);
        }
        catch(UncleanStatusException e) {
            Robot.logger.error("Couldn't send LightDrive message");
        }
        sendBuffer.rewind();
        
        //attempts to recieve message
        receiveID.putInt(CAN_ID + 4);
        receiveID.rewind();
        try {
            receiveArray = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(receiveID.asIntBuffer(), 
                MSG_ID_MASK, timestampBuffer);
        }
        catch (CANMessageNotFoundException e) {
            //Robot.logger.error("Couldn't receive LightDrive message", e);
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