package org.livoniawarriors.Robot2019.subsystems.flamethrower;

import edu.wpi.first.hal.can.CANJNI;
import edu.wpi.first.hal.util.UncleanStatusException;
import java.nio.ByteBuffer;
import org.livoniawarriors.Robot2019.Robot;
import edu.wpi.first.wpilibj.Notifier;

public class LightDrive {
    private Notifier lightsNotifier;

    private ByteBuffer sendBuffer;
    private byte[] tempSend1;
    private byte[] tempSend2;

    private int CAN_ID; //given by lib.
    private static int NUM_DRIVES = 0;
    private static final float PERIOD_SEC = 1;

    public LightDrive() {
        CAN_ID = 33882112 + (NUM_DRIVES);
        NUM_DRIVES++;

        tempSend1 = new byte[8];
        tempSend2 = new byte[8];
        sendBuffer = ByteBuffer.allocate(16);

        lightsNotifier = new Notifier(this::update);
        lightsNotifier.startPeriodic(PERIOD_SEC);
    }

    public void closeNotifier() {
        lightsNotifier.close();
    }

    public void update() {
        //attempts to send message
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