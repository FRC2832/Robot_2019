package org.livoniawarriors.Robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;

public class DriveTrain implements ISubsystem {

    private final static int DRIVE_MOTER_FL = 11;
    private final static int DRIVE_MOTER_FR = 13;
    private final static int DRIVE_MOTER_BL = 23;
    private final static int DRIVE_MOTER_BR = 24;

    private DifferentialDrive drive;

    @Override
    public void csv(ICsvLogger csv) {
        csv.log("Voltage", new PowerDistributionPanel().getVoltage());
    }

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
