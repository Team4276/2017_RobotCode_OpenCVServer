package org.usfirst.frc.team4276.robot;

import java.io.IOException;

import org.opencv.core.*;
import org.opencv.videoio.*;
import org.opencv.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraThread extends Thread implements Runnable {

	public static Boolean isCameraThreadShuttingDown = false;
	private static VideoCapture camera;

	public int cameraFrame = 0;


	// Known problem (OpenCV Bug #3460) with symbols for camera property ID in Java, 
	// so define them using numbers from C++ header:
	static int CAP_PROP_POS_MSEC = 0;
	static int CAP_PROP_POS_FRAMES = 1;
	static int CAP_PROP_POS_AVI_RATIO = 2;
	static int CAP_PROP_FRAME_WIDTH = 3;
	static int CAP_PROP_FRAME_HEIGHT = 4;
	static int CAP_PROP_FPS = 5;
	static int CAP_PROP_FOURCC = 6;
	static int CAP_PROP_FRAME_COUNT = 7;
	static int CAP_PROP_FORMAT = 8;
	static int CAP_PROP_MODE = 9;
	static int CAP_PROP_BRIGHTNESS = 10;
	static int CAP_PROP_CONTRAST = 11;
	static int CAP_PROP_SATURATION = 12;
	static int CAP_PROP_HUE = 13;
	static int CAP_PROP_GAIN = 14;
	static int CAP_PROP_EXPOSURE = 15;
	static int CAP_PROP_CONVERT_RGB = 16;
	static int CAP_PROP_WHITE_BALANCE_BLUE_U = 17;
	static int CAP_PROP_RECTIFICATION = 18;
	static int CAP_PROP_MONOCROME = 19;
	static int CAP_PROP_SHARPNESS = 20;
	static int CAP_PROP_AUTO_EXPOSURE = 21; // DC1394: exposure control done by
											// camera, user can adjust reference
											// level using this feature
	static int CAP_PROP_GAMMA = 22;
	static int CAP_PROP_TEMPERATURE = 23;
	static int CAP_PROP_TRIGGER = 24;
	static int CAP_PROP_TRIGGER_DELAY = 25;
	static int CAP_PROP_WHITE_BALANCE_RED_V = 26;
	static int CAP_PROP_ZOOM = 27;
	static int CAP_PROP_FOCUS = 28;
	static int CAP_PROP_GUID = 29;
	static int CAP_PROP_ISO_SPEED = 30;
	static int CAP_PROP_BACKLIGHT = 32;
	static int CAP_PROP_PAN = 33;
	static int CAP_PROP_TILT = 34;
	static int CAP_PROP_ROLL = 35;
	static int CAP_PROP_IRIS = 36;
	static int CAP_PROP_SETTINGS = 37;

	public void run() {

		camera = new VideoCapture(0);
		
		//if (!camera.set(CAP_PROP_FRAME_WIDTH, 1920.0)) {

		Mat frame = new Mat();
		
		camera.read(frame);

		if (!camera.isOpened()) {
			SmartDashboard.putNumber("CameraFrame", -1);
			System.out.println("Camera not opened");
		} else {
			while (!isCameraThreadShuttingDown) {
				if (camera.read(frame)) {
					SmartDashboard.putNumber("CameraFrame", cameraFrame++);
					try {
						Robot.visionStreamServer.sendImage(frame);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		camera.release();

		System.out.println("VideoServer test stopped");

	}

}
