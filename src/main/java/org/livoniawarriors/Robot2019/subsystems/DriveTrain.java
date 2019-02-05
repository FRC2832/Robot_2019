package org.livoniawarriors.Robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;

public class DriveTrain implements ISubsystem {

    public final static int DRIVE_MOTER_FL = 10;
    public final static int DRIVE_MOTER_FR = 24;
    public final static int DRIVE_MOTER_BL = 11;
    public final static int DRIVE_MOTER_BR = 25;

    private DifferentialDrive drive;

    @Override
    public void init() {
        WPI_TalonSRX talonFL = new WPI_TalonSRX(DRIVE_MOTER_FL);
        WPI_TalonSRX talonFR = new WPI_TalonSRX(DRIVE_MOTER_FR);
        WPI_TalonSRX talonBL = new WPI_TalonSRX(DRIVE_MOTER_BL);
        WPI_TalonSRX talonBR = new WPI_TalonSRX(DRIVE_MOTER_BR);
        talonBL.follow(talonFL);
        //talonBL.setInverted(true);
        talonBR.follow(talonFR);
        drive = new DifferentialDrive(talonFL, talonFR);
    }

    public void tankDrive(double left, double right) {
        tankDrive(left, right, true);
    }

    public void tankDrive(double left, double right, boolean squaredInputs) {
        drive.tankDrive(left, right, squaredInputs);
    }

    @Override
    public void update(boolean enabled) {

    }

    @Override
    public void dispose() throws Exception {

    }
}
