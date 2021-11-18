// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.subsystems.Drivetrain.MAX_ANGULAR_SPEED;
import static frc.robot.subsystems.Drivetrain.MAX_SPEED;

public class Robot extends TimedRobot
{
    private final XboxController controller = new XboxController(0);
    private final Drivetrain swerve = new Drivetrain();

    RobotContainer robotContainer = new RobotContainer();
}
