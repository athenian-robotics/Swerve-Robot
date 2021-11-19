// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.DriveConstants.*;

/**
 * Represents a swerve drive style drivetrain.
 */
public class Drivetrain extends SubsystemBase {
    public static final double MAX_SPEED = 3.0*Constants.DriveConstants.speedScale; // 3 meters per second
    public static final double MAX_ANGULAR_SPEED = Math.PI*Constants.DriveConstants.speedScale; // 1/2 rotation per second

    private final Translation2d frontLeftLocation = new Translation2d(-0.2925, 0.2925);
    private final Translation2d frontRightLocation = new Translation2d(0.2925, -0.2925);
    private final Translation2d backLeftLocation = new Translation2d(-0.2925, -0.2925);
    private final Translation2d backRightLocation = new Translation2d(0.2925, 0.2925);

    private final SwerveModule frontLeft = new SwerveModule(frontLeftDrivePort, frontLeftTurnPort, frontLeftTurnEncoderPort);
    private final SwerveModule frontRight = new SwerveModule(frontRightDrivePort, frontRightTurnPort, frontRightTurnEncoderPort);
    private final SwerveModule backLeft = new SwerveModule(backLeftDrivePort, backLeftTurnPort, backLeftTurnEncoderPort);
    private final SwerveModule backRight = new SwerveModule(backRightDrivePort, backRightTurnPort, backRightTurnEncoderPort);

    private final AnalogGyro gyro = new AnalogGyro(SPI.Port.kOnboardCS0.value);

    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);

    private final SwerveDriveOdometry odometry = new SwerveDriveOdometry(kinematics, getAngle());

    public Drivetrain()
    {
        gyro.reset();
    }

    /**
     * Returns the angle of the robot as a Rotation2d.
     *
     * @return The angle of the robot.
     */
    public Rotation2d getAngle() {
        // Negating the angle because WPILib gyros are CW positive.
        return Rotation2d.fromDegrees(-gyro.getAngle());
    }

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed        Speed of the robot in the x direction (forward).
     * @param ySpeed        Speed of the robot in the y direction (sideways).
     * @param rot           Angular rate of the robot.
     */
    public void drive(double xSpeed, double ySpeed, double rot) {
        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, getAngle()));
        SwerveDriveKinematics.normalizeWheelSpeeds(swerveModuleStates, MAX_SPEED);
        frontLeft.setDesiredState(swerveModuleStates[0]);
        frontRight.setDesiredState(swerveModuleStates[1]);
        backLeft.setDesiredState(swerveModuleStates[2]);
        backRight.setDesiredState(swerveModuleStates[3]);
    }

    /**
     * Updates the field relative position of the robot.
     */
    public void updateOdometry() {
        odometry.update(
                getAngle(),
                frontLeft.getState(),
                frontRight.getState(),
                backLeft.getState(),
                backRight.getState()
        );
    }

    public SwerveDriveOdometry getOdometry() { return odometry; }
}
