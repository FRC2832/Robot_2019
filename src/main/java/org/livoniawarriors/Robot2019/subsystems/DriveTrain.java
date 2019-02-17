package org.livoniawarriors.Robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;

public class DriveTrain implements ISubsystem {

    public final static int DRIVE_MOTER_FL = 25;
    public final static int DRIVE_MOTER_FR = 10;
    public final static int DRIVE_MOTER_BL = 24;
    public final static int DRIVE_MOTER_BR = 11;

    private DifferentialDrive drive;
    private PowerDistributionPanel pdp;

    @Override
    public void csv(ICsvLogger csv) {
        csv.log("Voltage", pdp.getVoltage());
    }

    @Override
    public void diagnose() {

    }

    @Override
    public void init() {
        WPI_TalonSRX talonFrontLeft = new WPI_TalonSRX(DRIVE_MOTER_FL);
        WPI_TalonSRX talonFrontRight = new WPI_TalonSRX(DRIVE_MOTER_FR);
        WPI_TalonSRX talonBackLeft = new WPI_TalonSRX(DRIVE_MOTER_BL);
        WPI_TalonSRX talonBackRight = new WPI_TalonSRX(DRIVE_MOTER_BR);
        talonBackLeft.follow(talonFrontLeft);
        //talonBL.setInverted(true);
        talonBackRight.follow(talonFrontRight);
        drive = new DifferentialDrive(talonFrontLeft, talonFrontRight);
        pdp = new PowerDistributionPanel();
    }

    public void tankDrive(double left, double right) {
        tankDrive(left, right, true);
    }

    public void tankDrive(double left, double right, boolean squaredInputs) {
        drive.tankDrive(-left, -right, squaredInputs);
    }

    @Override
    public void update(boolean enabled) {

    }

    @Override
    public void dispose() throws Exception {

    }
}
