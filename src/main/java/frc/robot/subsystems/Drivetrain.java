// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.DriveConstants.*;

import com.kauailabs.navx.frc.AHRS;


public class Drivetrain extends SubsystemBase {
    public static final double MAX_SPEED = speedScale; // 1 meters per second max
    public static final double MAX_ANGULAR_SPEED = Math.PI * speedScale / 2; // 1/4 rotation per second

    public final SwerveModule frontLeft;
    public final SwerveModule frontRight;
    public final SwerveModule backLeft;
    public final SwerveModule backRight;

    private final AHRS gyro;

    private final SwerveDriveKinematics kinematics;
    private final SwerveDriveOdometry odometry;

    public Drivetrain() {
        frontLeft = new SwerveModule(frontLeftDrivePort, frontLeftTurnPort, frontLeftTurnEncoderPort, frontLeftTurnOffset);
        frontRight = new SwerveModule(frontRightDrivePort, frontRightTurnPort, frontRightTurnEncoderPort, frontRightTurnOffset);
        backLeft = new SwerveModule(backLeftDrivePort, backLeftTurnPort, backLeftTurnEncoderPort, backLeftTurnOffset);
        backRight = new SwerveModule(backRightDrivePort, backRightTurnPort, backRightTurnEncoderPort, backRightTurnOffset);
        gyro = new AHRS(SerialPort.Port.kUSB); //If not plugged into the USB (SerialPort.Port.kUSB, and is instead i2c, use I2C.Port.kMXP

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

    public void drive(double driveMotor, double turnMotor) {
        backRight.setDriveMotor(driveMotor);
        backLeft.setDriveMotor(driveMotor); // DRIVE MOTOR
        frontRight.setDriveMotor(driveMotor);
        frontLeft.setDriveMotor(driveMotor);

        backRight.setTurnMotor(-turnMotor);
        backLeft.setTurnMotor(-turnMotor); // TURN MOTORS
        frontRight.setTurnMotor(-turnMotor);
        frontLeft.setTurnMotor(-turnMotor);
    }

    public void rotate(double rot) {
        int rightSign = frontRight.getDriveMotorOutput() < 0 ? 1 : -1;
        int leftSign = frontLeft.getDriveMotorOutput() < 0 ? 1 : -1;

        if (rot > 0) { // Rotate Right
            frontRight.setDriveMotor(frontRight.getDriveMotorOutput() * rightSign + 0.05);
            backRight.setDriveMotor(backRight.getDriveMotorOutput() * rightSign + 0.05);
        }
        if (rot < 0 ) { // Rotate Left
            frontLeft.setDriveMotor(frontLeft.getDriveMotorOutput() * leftSign + 0.05);
            backLeft.setDriveMotor(backLeft.getDriveMotorOutput() * leftSign + 0.05);
        }
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
        SmartDashboard.putBoolean("Gyro Connection", gyro.isConnected());
        SmartDashboard.putNumber("Front Left Drive Encoder: ", frontLeft.driveEncoder.getPosition());
        SmartDashboard.putNumber("Front Left Turning Encoder: ", frontLeft.turningEncoder.getDistance() / (2 * Math.PI) * 360);
        SmartDashboard.putNumber("Front Right Drive Encoder: ", frontRight.driveEncoder.getPosition());
        SmartDashboard.putNumber("Front Right Turning Encoder: ", frontRight.turningEncoder.getDistance() / (2 * Math.PI) * 360); //Radians to degrees
        SmartDashboard.putNumber("Back Left Drive Encoder: ", backLeft.driveEncoder.getPosition());
        SmartDashboard.putNumber("Back Left Turning Encoder: ", backLeft.turningEncoder.getDistance() / (2 * Math.PI) * 360);
        SmartDashboard.putNumber("Back Right Drive Encoder: ", backRight.driveEncoder.getPosition());
        SmartDashboard.putNumber("Back Right Turning Encoder: ", backRight.turningEncoder.getDistance() / (2 * Math.PI) * 360);
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
