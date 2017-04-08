package org.usfirst.frc.team6595.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	public RobotDrive terra;
	public Joystick xbox, xbox2;
	public Joystick ps4;
	public Spark lift;
	public VictorSP leftFront, leftBack, rightFront, rightBack;
	public UsbCamera cam;
	
	private SendableChooser<String> chooser = new SendableChooser<>();
	public String autoSelected;
	
	public PowerDistributionPanel pdp;

	public ADXRS450_Gyro gyro;
	public double angle;
	public int reset = 0;
	final String defaultAuto = "Baseline Auton";
	final String centerpegAuto = "Center Peg Auton";
	public Timer autoTimer = new Timer();
	public double autoTime;
	

	@Override
	public void robotInit() {
		
		leftFront = new VictorSP(RobotMap.DRIVE_LEFT_FRONT);
		leftBack = new VictorSP(RobotMap.DRIVE_LEFT_BACK);
		rightFront = new VictorSP(RobotMap.DRIVE_RIGHT_FRONT);
		rightBack = new VictorSP(RobotMap.DRIVE_RIGHT_BACK);
		
		terra = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		terra.setExpiration(0.1);
		
		lift = new Spark(RobotMap.HOPPER_INTAKE);
		lift = new Spark(RobotMap.LIFT);
		
		cam = CameraServer.getInstance().startAutomaticCapture(0);
		cam.setResolution(RobotMap.CAM_WIDTH, RobotMap.CAM_HEIGHT);
		cam.setFPS(RobotMap.CAM_FPS);
		
		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();
		
		//xbox = new Joystick(0);
		xbox2 = new Joystick(1);
		
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		
		ps4 = new Joystick(0);
		
	    chooser.addDefault("Baseline Auton", defaultAuto);
		chooser.addObject("Center Peg Auton", centerpegAuto);
		SmartDashboard.putData("Autonomous Choices", chooser);
		
	}


	@Override
	public void autonomousInit() {
		
		terra.setSafetyEnabled(false);
		reset = 0;
		autoSelected = chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector",
		defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		
		gyro.reset();
		
		autoTimer.start();
	}

	@Override
	public void autonomousPeriodic() {
		
		this.updateDashboard();
		
		autoTime = autoTimer.get();
		
		if (autoSelected == centerpegAuto) {
			
			if (autoTime <= 1.5) {
				terra.drive(0.3, -angle);
			}
			
			if (autoTime <= 2.1) {
				terra.drive(-0.2, -angle);
			}
			
			else {
				terra.stopMotor();
			}
		}
		
		else if (autoSelected == defaultAuto) {
			
			if (autoTime <= 2) {
				terra.drive(0.3, -angle);
			}
			
			else {
				terra.stopMotor();
			}
			
		}
		
		/*switch //(autoSelected) {
		case centerpegAuto:
			/*terra.setSafetyEnabled(false);
			terra.drive(0.3, 0.0);
			Timer.delay(2.3);
			terra.stopMotor();*/
		//	break;
		//case defaultAuto:
		//default:
			/*terra.setSafetyEnabled(false);
			terra.drive(0.3, 0.0);
			Timer.delay(1.5);
			terra.stopMotor();
			Timer.delay(10);*/
			//break;
	}
	
	@Override
	public void teleopInit() {
		
		gyro.reset();
		terra.setSafetyEnabled(true);
	
	}

	@Override
	public void teleopPeriodic() {
		
		this.updateDashboard();
	
		
		if (xbox.getRawButton(1)) {
			
			if (reset < 1) {
				gyro.reset();
				reset++;
			}
			
		   terra.drive(-ps4.getRawAxis(RobotMap.XBOX_LEFTSTICK_Y), -angle);
		} 
		else {
			
		   reset = 0;
		   terra.tankDrive(-ps4.getRawAxis(RobotMap.PS4_LEFTSTICK_Y), -ps4.getRawAxis(RobotMap.PS4_RIGHTSTICK_Y));
		}
		    
        // terra.arcadeDrive(-ps4.getRawAxis(RobotMap.PS4_LEFTSTICK_Y), -ps4.getRawAxis(RobotMap.PS$_RIGHTSTICK_X);
		// terra.arcadeDrive(-xbox.getRawAxis(RobotMap.XBOX_LEFTSTICK_Y), -xbox.getRawAxis(RobotMap.XBOX_RIGHTSTICK_X));

		// Lift
		if (xbox2.getRawButton(RobotMap.XBOX_LEFTBUMPER)) {
			lift.set(-0.8);
		} else if (xbox2.getRawButton(RobotMap.XBOX_RIGHTBUMPER)) {
			lift.set(0.8);
		} else {
			lift.stopMotor();
		}
		
		/*if (Math.abs(xbox2.getRawAxis(RobotMap.XBOX_LEFTSTICK_Y)) > 0.2) {
			lift.set(xbox2.getRawAxis(RobotMap.XBOX_LEFTSTICK_Y) * 0.8);
		}*/

		// Lift Mechanism
		/*if (xbox2.getRawButton(RobotMap.XBOX_X)) {
			lift.set(1);
		} 
		else if (xbox2.getRawButton(RobotMap.XBOX_Y)) {
			lift.set(-1);
		} 
		else {
			lift.stopMotor();
		}*/
		
		/*Gear Arm
		if (xbox2.getRawButton(RobotMap.XBOX_LEFTBUMPER)) {
		    gearArm.set(0.4);
		} else if (xbox2.getRawButton(RobotMap.XBOX_RIGHTBUMPER)) {
			gearArm.set(-0.4);
		} else {
			gearArm.stopMotor();
		}*/
		
	}


	@Override
	public void testPeriodic() {
		
	}
	
	public void updateDashboard() {
		
		angle = gyro.getAngle() * 0.04;
		
		SmartDashboard.putBoolean("Spark Lift Connection (PWM 5): ", lift.isAlive());		
		SmartDashboard.putNumber("Voltage of PDP: ", pdp.getVoltage());
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		
	}
	
}

