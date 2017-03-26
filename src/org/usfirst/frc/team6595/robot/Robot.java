package org.usfirst.frc.team6595.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	
	public RobotDrive terra;
	public Joystick xbox, xbox2;
	public Spark hopperIntake, lift, gearArm;
	public VictorSP leftFront, leftBack, rightFront, rightBack;
	public UsbCamera cam;
	
	public SendableChooser<String> chooser;
	public PowerDistributionPanel pdp;

	@Override
	public void robotInit() {
		
		leftFront = new VictorSP(RobotMap.DRIVE_LEFT_FRONT);
		leftBack = new VictorSP(RobotMap.DRIVE_LEFT_BACK);
		rightFront = new VictorSP(RobotMap.DRIVE_RIGHT_FRONT);
		rightBack = new VictorSP(RobotMap.DRIVE_RIGHT_BACK);
		
		terra = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		terra.setExpiration(0.1);
		
		hopperIntake = new Spark(RobotMap.HOPPER_INTAKE);
		gearArm = new Spark(RobotMap.GEAR_ARM);
		lift = new Spark(RobotMap.LIFT);
		
		cam = CameraServer.getInstance().startAutomaticCapture(1);
		cam.setResolution(RobotMap.CAM_WIDTH, RobotMap.CAM_HEIGHT);
		cam.setFPS(RobotMap.CAM_FPS);
		
		chooser = new SendableChooser<>();
		chooser.addDefault("Baseline Auto", RobotMap.BASELINE_AUTO);
		chooser.addObject("Center Peg Auto", RobotMap.CNTR_PEG_AUTO);
		SmartDashboard.putData("Auto modes", chooser);
		
		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();
		
		xbox = new Joystick(0);
		xbox2 = new Joystick(1);

	}

	@Override
	public void autonomous() {
		String autoSelected = (String) chooser.getSelected();
		System.out.println("Autonomous Mode selected: " + autoSelected);

		switch (autoSelected) {
		case RobotMap.CNTR_PEG_AUTO:
			terra.setSafetyEnabled(false);
			terra.drive(0.3, 0.0);
			Timer.delay(2.3);
			terra.stopMotor();
			break;
		case RobotMap.BASELINE_AUTO:
		default:
			terra.setSafetyEnabled(false);
			terra.drive(0.4, 0.0);
			Timer.delay(3);
			terra.stopMotor();
			break;
		}
	}

	@Override
	public void operatorControl() {
		
		terra.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {

			this.updateDashboard();

			terra.tankDrive(-xbox.getRawAxis(RobotMap.XBOX_LEFTSTICK_Y), -xbox.getRawAxis(RobotMap.XBOX_RIGHTSTICK_Y));

			// Hopper
			if (xbox2.getRawButton(RobotMap.XBOX_X)) {
				hopperIntake.set(-0.8);
			} else if (xbox2.getRawButton(RobotMap.XBOX_Y)) {
				hopperIntake.set(0.8);
			} else {
				hopperIntake.stopMotor();
			}

			// Lift Mechanism
			if (xbox2.getRawButton(RobotMap.XBOX_BACK)) {
				lift.set(1);
			} else if (xbox2.getRawButton(RobotMap.XBOX_LEFTSTICK_BUTTON)) {
				lift.set(-1);
			} else {
				lift.stopMotor();
			}
			
			/*Gear Arm
			if (xbox2.getRawButton(RobotMap.XBOX_LEFTBUMPER)) {
			    gearArm.set(0.4);
			} else if (xbox2.getRawButton(RobotMap.XBOX_RIGHTBUMPER)) {
				gearArm.set(-0.4);
			} else {
				gearArm.stopMotor();
			}*/
			
			Timer.delay(0.005);
		}
	}

	@Override
	public void test() {
		
	}

	public void updateDashboard() {
		
		SmartDashboard.putNumber("Voltage of PDP: ", pdp.getVoltage());
	}
}