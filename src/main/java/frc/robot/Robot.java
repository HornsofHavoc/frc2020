/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {

  private Timer autoTimer;
  private Timer tubeTimer;
  private CameraServer camera;
  private WPI_TalonSRX leftFront;
  private WPI_TalonSRX leftBack;
  private WPI_TalonSRX rightFront;
  private WPI_TalonSRX rightBack;
  private WPI_TalonSRX tube1;
  private WPI_TalonSRX tube2;
  private WPI_TalonSRX intake;
  private WPI_TalonSRX controlPanel;
  private Solenoid controlUp;
  private Solenoid controlDown;
  private SpeedControllerGroup leftDrive;
  private SpeedControllerGroup rightDrive;
  private SpeedControllerGroup tube;
  private DifferentialDrive drive;
  private Joystick leftStick;
  private Joystick rightStick;
  private XboxController controller;
  private double timeNow;
  @Override
  public void robotInit() {
    autoTimer = new Timer();
    tubeTimer = new Timer();
    CameraServer.getInstance().startAutomaticCapture();
    leftFront = new WPI_TalonSRX(0);
    leftBack = new WPI_TalonSRX(1);
    rightFront = new WPI_TalonSRX(2);
    rightBack = new WPI_TalonSRX(3);
    tube1 = new WPI_TalonSRX(4);
    tube2 = new WPI_TalonSRX(5);
    intake = new WPI_TalonSRX(6);
    controlPanel = new WPI_TalonSRX(7);
    controlUp = new Solenoid(0);
    controlDown = new Solenoid(1);
    leftDrive = new SpeedControllerGroup(leftFront, leftBack);
    rightDrive = new SpeedControllerGroup(rightFront, rightBack);
    tube = new SpeedControllerGroup(tube1, tube2);
    drive = new DifferentialDrive(leftDrive, rightDrive);
    leftStick = new Joystick(0);
    rightStick = new Joystick(1);
    controller = new XboxController(2);
  }

  @Override
  public void autonomousInit() {
    autoTimer.stop();
    autoTimer.reset();
    autoTimer.start();
    // TODO Auto-generated method stub
    super.autonomousInit();
  }
  @Override
  public void autonomousPeriodic() {
    if (autoTimer.get() <= 3.0) {
      leftDrive.set(0.75);
      rightDrive.set(0.75);
    } else {
      leftDrive.set(0);
      rightDrive.set(0);
    }
    // TODO Auto-generated method stub
    super.autonomousPeriodic();
  }
  @Override
  public void teleopPeriodic() {
    drive.setSafetyEnabled(false);
    drive.tankDrive(leftStick.getRawAxis(1)*-1, rightStick.getRawAxis(1)*-1);
    while (rightStick.getRawButton(1)) {
      drive.tankDrive(rightStick.getRawAxis(1), leftStick.getRawAxis(1));
    }
    while (leftStick.getRawButton(1)) {
      drive.tankDrive(leftStick.getRawAxis(1)*-0.75, rightStick.getRawAxis(1)*-0.75);
    }
    if (controller.getRawAxis(3) >= 0) {
      timeNow = tubeTimer.get();
    }
    if (tubeTimer.get() - timeNow >= 0.5) {
      intake.set(0);
      System.out.println(timeNow);
    }
    tube.set(controller.getRawAxis(2) * -0.6);
    intake.set(controller.getRawAxis(3) * -0.75);
    while (controller.getRawButton(5)) {
      tube.set(controller.getRawAxis(3) * -1);
      intake.set(controller.getRawAxis(3) * -1);
    }
    if (controller.getRawButtonPressed(3)) {
      controlPanel.set(0.5);
    } else if (controller.getRawButtonPressed(4)) {
      controlPanel.set(0);
    }
    if (controller.getRawButtonPressed(1)) {
      controlUp.set(true);
      controlDown.set(false);
    } else if (controller.getRawButton(2)) {
      controlUp.set(false);
      controlDown.set(true);
    } else {

    }
  }
}
  


