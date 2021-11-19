// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Drivetrain;


public class Robot extends TimedRobot {
    private final XboxController controller = new XboxController(0);
    private final Drivetrain swerve = new Drivetrain();

    RobotContainer robotContainer;

    @Override
    public void robotInit() {robotContainer = new RobotContainer();}

    @Override
    public void robotPeriodic() {}

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
        RobotContainer.disableAll(robotContainer);
    }

    @Override
    public void disabledPeriodic() {}

    /**
     * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {}

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {}

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit() {CommandScheduler.getInstance().cancelAll();}

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {}
}
