
package org.usfirst.frc.team4276.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {

	TankDrive drive;
	Arm arm;
	// LidarSpin spinny;
	LIDAR mylid;
	Shooter shoot;
	LEDOut led;
	Timer autotimer;
	DashboardOutput dash;
	IMU myIMU;
	AutoModeSwitch automodeswitch;

	public static VideoServer visionStreamServer;
	private Thread m_cameraThread;

	// ADIS16448_IMU imu;
	public static int g_nSequenceLidar = 0;
	public static double g_lidarDistanceCentimeters = 0.0;

	public static int g_nSequenceVisionSystem = 0;
	public static boolean g_isVisionSystemGoalDetected = false;
	public static double g_visionSystemAngleRobotToGoal = -181.0;
	public static double g_visionSystemPixelX = -181.0;

	public static boolean g_isImuDataValid = false;
	public static double g_imuYawDegrees = -181.00;

	public Robot() {
		System.load("/usr/local/share/OpenCV/java/libopencv_java310.so");

		drive = new TankDrive(3, 4, 5, 0, 1, 2, 6, 7); // RF,RM,RB,LF,LM,LB,DriveEnc1,DriveEnc2
		arm = new Arm(6, 7, 3, 0, 1, 2); // PowerMotor,IntakeMotor,BallStopLimit,ArmEnc,ArmEnc,HallSwitch
		autotimer = new Timer();
		// imu = new ADIS16448_IMU();
		// spinny = new LidarSpin(9);
		mylid = new LIDAR(Port.kMXP);
		mylid.start(20);
		shoot = new Shooter(8, 9, 4, 5); // ShooterLeft,ShooterRight,enc1,enc2
		// led = new LEDOut(20,19,18,27);
		// dash = new DashboardOutput();
		myIMU = new IMU();
		automodeswitch = new AutoModeSwitch();
		dash = new DashboardOutput();

		try {
			visionStreamServer = new VideoServer(5814, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			visionStreamServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_cameraThread = new Thread(new CameraThread());

	}

	public void robotInit() {

		dash.start();

		m_cameraThread.start();
	}

	public void autonomous() {
		drive.driveenc.reset();
		while (true) {
			if (drive.autodrive(3000, .8, 180.0))
				break;
			SmartDashboard.putString("Auto: ", "Running!");
		}
		SmartDashboard.putString("Auto: ", "NOT!");
		/*
		 * String autoSelected = (String) chooser.getSelected(); // String
		 * autoSelected = SmartDashboard.getString("Auto Selector",
		 * defaultAuto); System.out.println("Auto selected: " + autoSelected);
		 * 
		 * switch(autoSelected) { case customAuto:
		 * myRobot.setSafetyEnabled(false); myRobot.drive(-0.5, 1.0); // spin at
		 * half speed Timer.delay(2.0); // for 2 seconds myRobot.drive(0.0,
		 * 0.0); // stop robot break; case defaultAuto: default:
		 * myRobot.setSafetyEnabled(false); myRobot.drive(-0.5, 0.0); // drive
		 * forwards half speed Timer.delay(2.0); // for 2 seconds
		 * myRobot.drive(0.0, 0.0); // stop robot break; }
		 */
	}

	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {

			arm.collector();
			// spinny.spinnerex();
			drive.run();
			shoot.run();
			SmartDashboard.putNumber("Shooter speed: ", Shooter.shooterenc.getRate());

			// lead.output();

			Timer.delay(0.005); // wait for a motor update time
		}
	}

	public void test() {
	}
}
