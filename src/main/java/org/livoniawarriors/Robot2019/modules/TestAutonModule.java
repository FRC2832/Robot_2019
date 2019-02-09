package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import jaci.pathfinder.Waypoint;

public class TestAutonModule implements IControlModule {
	private static final int k_ticks_per_rev = 1024;
	private static final double k_wheel_diameter = 4.0 / 12.0;
	private static final double k_max_velocity = 4.0;
  
	private static final int k_left_channel = 0;
	private static final int k_right_channel = 1;
  
	private static final int k_left_encoder_port_a = 0;
	private static final int k_left_encoder_port_b = 1;
	private static final int k_right_encoder_port_a = 2;
	private static final int k_right_encoder_port_b = 3;
  
	private static final int k_gyro_port = 0;
  
	private static final String k_path_name = "Back&Forth";

	private SpeedController m_left_motor;
	private SpeedController m_right_motor;

	private Encoder m_left_encoder;
	private Encoder m_right_encoder;

	private AnalogGyro m_gyro;

	private EncoderFollower m_left_follower;
	private EncoderFollower m_right_follower;
		
	private Notifier m_follower_notifier;

	/*k_ticks_per_rev - number of encoder counts per wheel revolution

	k_wheel_diameter - diameter of the wheels

	k_max_velocity - maximum velocity of the robot

	k_left_channel, k_right_channel - the port numbers for the left and right speed controllers

	k_left_encoder_port_a, k_left_encoder_port_b, k_right_encoder_port_a, k_right_encoder_port_b - the port numbers for the encoders connected to the left and right side of the drivetrain

	k_gyro_port - the analog input for the gyro (other gyros might be connected differently)

	k_path_name - name of this path*/

	
	public void init(){

	}

	public void robotinit() {
		m_left_motor = new Spark(k_left_channel);
		m_right_motor = new Spark(k_right_channel);
		m_left_encoder = new Encoder(k_left_encoder_port_a, k_left_encoder_port_b);
		m_right_encoder = new Encoder(k_right_encoder_port_a, k_right_encoder_port_b);
		m_gyro = new AnalogGyro(k_gyro_port);
	}

	public void autonomousInit() {
	  Trajectory left_trajectory = PathfinderFRC.getTrajectory(k_path_name + ".right");
	  Trajectory right_trajectory = PathfinderFRC.getTrajectory(k_path_name + ".left");
  
	  m_left_follower = new EncoderFollower(left_trajectory);
	  m_right_follower = new EncoderFollower(right_trajectory);
  
	  m_left_follower.configureEncoder(m_left_encoder.get(), k_ticks_per_rev, k_wheel_diameter);
	  // You must tune the PID values on the following line!
	  m_left_follower.configurePIDVA(1.0, 0.0, 0.0, 1 / k_max_velocity, 0);
  
	  m_right_follower.configureEncoder(m_right_encoder.get(), k_ticks_per_rev, k_wheel_diameter);
	  // You must tune the PID values on the following line!
	  m_right_follower.configurePIDVA(1.0, 0.0, 0.0, 1 / k_max_velocity, 0);
	  
	  m_follower_notifier = new Notifier(this::followPath);
	  m_follower_notifier.startPeriodic(left_trajectory.get(0).dt);
	}

	@Override
	public void start() {

	}
	@Override
	public void update() {
	}
	private void followPath() {
		if (m_left_follower.isFinished() || m_right_follower.isFinished()) {
			m_follower_notifier.stop();
		} else {
			double left_speed = m_left_follower.calculate(m_left_encoder.get());
			double right_speed = m_right_follower.calculate(m_right_encoder.get());
			double heading = m_gyro.getAngle();
			double desired_heading = Pathfinder.r2d(m_left_follower.getHeading());
			double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
			double turn =  0.8 * (-1.0/80.0) * heading_difference;
			m_left_motor.set(left_speed + turn);
			m_right_motor.set(right_speed - turn);
			Waypoint[] points = new Waypoint[]{
				new Waypoint(68.76, 118.4, Pathfinder.d2r(60)),
				new Waypoint(144.218, 134.435, 0),
				new Waypoint(210.243, 151.413, Pathfinder.d2r(-60)),
				new Waypoint(70.647, 151.413, 0)

			};
			Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
			Trajectory trajectory = Pathfinder.generate(points, config);
		}
	}

	/**
	* This function is called periodically during autonomous.
	*/

	public void autonomousPeriodic() {
		
	}
	@Override
	public void stop(){

	}
	@Override
	public boolean isFinished() {
		return false;
	}
	public void teleopInit() {
		m_follower_notifier.stop();
		m_left_motor.set(0);
		m_right_motor.set(0);
	}
}
