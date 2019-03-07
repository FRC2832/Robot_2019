package org.livoniawarriors.Robot2019.subsystems.peripherals;

import edu.wpi.first.cameraserver.CameraServer;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;
import java.io.IOException;
import java.util.Random;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import com.ctre.phoenix.sensors.PigeonIMU;
import org.apache.logging.log4j.Level;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;
    private PigeonIMU pigeon;
    private double[] yawPitchRoll = new double[3];

    private static final int PRESSURE_SENSOR_PORT = 1;
    private static final int PIGEON_PORT = 24;
    private JeVois jeVois;
    private AnalogInput pressureSensor;
    private double startingYaw;
    private final static int BOTTOM_OUT_PIN = 0;

    private Compressor compressor;

    private Notifier notifier;
    private final static double REV_ROBOTICS_DIGIT_MXP_DISPLAY_UPDATE_PERIOD = .25;

    private REVDigitBoard digitBoard;
    private CasseroleRIOLoadMonitor loadMonitor;
    private DigitalInput bottomOut;


    @Override
    public void init() {
        lidar = new Lidar();
        loadMonitor = new CasseroleRIOLoadMonitor();
        pigeon = new PigeonIMU(PIGEON_PORT);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_PORT);
        compressor = new Compressor();
        compressor.start();
        digitBoard = new REVDigitBoard();
        jeVois = new JeVois();
        //CameraServer.getInstance().startAutomaticCapture();
        notifier = new Notifier(() -> {
            var value = new StringBuilder(Integer.toString((int)Math.abs(getPressure())));
            value.reverse();
            while (value.length() < 4)
                value.append("0");
            value.reverse();
            digitBoard.display(value.toString());
        });
        notifier.startPeriodic(REV_ROBOTICS_DIGIT_MXP_DISPLAY_UPDATE_PERIOD);
        startingYaw = getYaw();
        bottomOut = new DigitalInput(BOTTOM_OUT_PIN);
    }

    @Override
    public void update(boolean enabled) {
        //digitBoard.display(Double.toString(getPressure()));
        /*System.out.println("==============+++==============");
        System.out.print("Vision Online: ");
        System.out.println(jeVois.isVisionOnline());
        System.out.print("Target Visible: ");
        System.out.println(jeVois.isTgtVisible());
        System.out.print("Target Angle: ");
        System.out.println(jeVois.getTgtAngle_Deg());
        System.out.print("Target Range:");
        System.out.println(jeVois.getTgtRange_in());
        System.out.print("Serial Packet RX Rate: ");
        System.out.println(jeVois.getPacketRxRate_PPS());
        System.out.print("JeVois Framerate: ");
        System.out.println(jeVois.getJeVoisFramerate_FPS());
        System.out.print("JeVois CPU Load: ");
        System.out.println(jeVois.getJeVoisCpuLoad_pct());
        System.out.println("===============================\n\n\n");*/

    }

    @Override
    public void dispose() throws IOException {
        lidar.dispose();
    }
    @Override
    public void csv(ICsvLogger csv) {
        csv.log("Memory Usage", loadMonitor.getMemLoadPct());
        csv.log("Cpu Usage", loadMonitor.getCPULoadPct());
    }

    @Override
    public void diagnose() {

    }

    public double getYaw() {
        //returns the yaw; copy method and change array element to get pitch or roll
        pigeon.getYawPitchRoll(yawPitchRoll);
        return yawPitchRoll[0] - startingYaw;
    }

    public double getPressure() {
        return 250d * pressureSensor.getAverageVoltage() / 5d - 25d;
    }

    public boolean getBottomOut() {
        return bottomOut.get();
    }
}
