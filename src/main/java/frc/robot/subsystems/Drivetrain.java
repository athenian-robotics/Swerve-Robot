// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.DriveConstants.*;


public class Drivetrain extends SubsystemBase {
    public static final double MAX_SPEED = speedScale; // 1 meters per second max
    public static final double MAX_ANGULAR_SPEED = Math.PI * speedScale / 2; // 1/4 rotation per second

    private final SwerveModule frontLeft;
    private final SwerveModule frontRight;
    private final SwerveModule backLeft;
    private final SwerveModule backRight;

    private final ADXRS450_Gyro gyro;

    private final SwerveDriveKinematics kinematics;
    private final SwerveDriveOdometry odometry;

    public Drivetrain() {
        frontLeft = new SwerveModule(frontLeftDrivePort, frontLeftTurnPort, frontLeftTurnEncoderPort);
        frontRight = new SwerveModule(frontRightDrivePort, frontRightTurnPort, frontRightTurnEncoderPort);
        backLeft = new SwerveModule(backLeftDrivePort, backLeftTurnPort, backLeftTurnEncoderPort);
        backRight = new SwerveModule(backRightDrivePort, backRightTurnPort, backRightTurnEncoderPort);
        gyro = new ADXRS450_Gyro();

        Translation2d backRightLocation = new Translation2d(0.2925, 0.2925);
        Translation2d frontRightLocation = new Translation2d(0.2925, -0.2925);
        Translation2d backLeftLocation = new Translation2d(-0.2925, -0.2925);
        Translation2d frontLeftLocation = new Translation2d(-0.2925, 0.2925);

        kinematics = new SwerveDriveKinematics(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
        odometry = new SwerveDriveOdometry(kinematics, getAngle());
    }

    /**
     * Returns the angle of the robot as a Rotation2d.
     *
     * @return The angle of the robot.
     */
    public Rotation2d getAngle() {return Rotation2d.fromDegrees(-gyro.getAngle());} //Negating the angle because WPILib gyros are CCW positive.

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed        Speed of the robot in the x direction (forward).
     * @param ySpeed        Speed of the robot in the y direction (sideways).
     * @param rot           Angular rate of the robot.
     */
    public void drive(double xSpeed, double ySpeed, double rot) {
        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, getAngle()));
        SwerveDriveKinematics.normalizeWheelSpeeds(swerveModuleStates, MAX_SPEED); //Make sure output values aren't above our max speed

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
}
