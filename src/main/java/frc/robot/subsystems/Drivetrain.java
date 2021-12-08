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
     * Method to drive the robot using joystick info (left stick)
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
     * Method to drive the robot using joystick info without pid or field relative
     *
     * @param driveMotorInput Drive Motor Input, scaled from raw left joystick xbox controller values. Drive via y-axis
     * @param turnMotorInput Turn Motor Input, scaled from raw left joystick xbox controller values. TUrn via x-axis
     */
    public void drive(double driveMotorInput, double turnMotorInput) {
        backRight.setDriveMotor(driveMotorInput);
        backLeft.setDriveMotor(driveMotorInput); // DRIVE MOTOR
        frontRight.setDriveMotor(driveMotorInput);
        frontLeft.setDriveMotor(driveMotorInput);

        backRight.setTurnMotor(-turnMotorInput);
        backLeft.setTurnMotor(-turnMotorInput); // TURN MOTORS
        frontRight.setTurnMotor(-turnMotorInput); // If not negated, they should be. These were tested and negating all of them allows for proper rotation
        frontLeft.setTurnMotor(-turnMotorInput);
    }

    /**
     * Method to rotate the robot chassis using joystick info (right stick)
     *
     * @param rot Rotation Input, scaled from raw right joystick xbox controller data.
     */
    public void rotate(double rot) {
        int rightSign;
        int leftSign;

        if (rot > 0) { // Rotate Right (Right side should be going backwards, while left side should move forwards)
            rightSign = frontRight.getDriveMotorOutput() < 0 ? 1 : -1; // If the right is already going backwards, leave it.
            leftSign = frontLeft.getDriveMotorOutput() > 0 ? 1 : -1; // If the left is already going forwards, leave it.

            // Reverse right side
            frontRight.setDriveMotor(frontRight.getDriveMotorOutput() * rightSign - rot);
            backRight.setDriveMotor(backRight.getDriveMotorOutput() * rightSign - rot);

            // Forward left side
            frontLeft.setDriveMotor(frontLeft.getDriveMotorOutput() * leftSign + rot); // Sets the front left to what it already was and just adds rot xbox data
            backLeft.setDriveMotor(backLeft.getDriveMotorOutput() * leftSign + rot);
        }
        if (rot < 0 ) { // Rotate Left (Left side should be going backwards, while right side should move forwards)
            rightSign = frontRight.getDriveMotorOutput() > 0 ? 1 : -1; // If the right side is already going forwards, leave it.
            leftSign = frontLeft.getDriveMotorOutput() < 0 ? 1 : -1; // If the left side is already going backwards, leave it.

            // Reverse left side
            frontLeft.setDriveMotor(frontLeft.getDriveMotorOutput() * leftSign + rot); // Add the rotation since turning left yields negative xbox data
            backLeft.setDriveMotor(backLeft.getDriveMotorOutput() * leftSign + rot);

            // Forward right side
            frontRight.setDriveMotor(frontRight.getDriveMotorOutput() * rightSign - rot);
            backRight.setDriveMotor(backRight.getDriveMotorOutput() * rightSign - rot);
        }
    }

    /**
     * This method, utilizing offsets and the current wheel position in degrees, turns all wheels to a specified angle. 0 for reset.
     * @param module The swerve module to adjust
     * @param deg The target in degrees
     */
    public void turnWheelsToDegrees(SwerveModule module, double deg) {
        int sign = Math.abs(360 - deg) > 180 ? -1 : 1; // Optimize motor direction

        double moduleAngle = (360 * (module.getTurnEncoderAngleDegrees())) % 360 - module.getTurningOffset() + deg;
        while (Math.abs(moduleAngle) > 0.2) { // While it's outside 0.2 degree of error . . .
            module.setTurnMotor(0.5 * sign); // Turn the motor on
            moduleAngle = (360 * (module.getTurnEncoderAngleDegrees())) % 360 - module.getTurningOffset() + deg; // Reset the angle reading
        }
        module.setTurnMotor(0); // Disable the motor once finished
    }

    /**
     * This method is repeatedly called, and is native to SubsystemBase. See parent class for more information.
     */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
        SmartDashboard.putBoolean("Gyro Connection", gyro.isConnected());
        SmartDashboard.putNumber("Front Left Drive Encoder: ", frontLeft.driveEncoder.getPosition());
        SmartDashboard.putNumber("Front Left Turning Encoder: ", (360 * (frontLeft.turningEncoder.getDistance() / (2 * Math.PI))));
        SmartDashboard.putNumber("Front Right Drive Encoder: ", frontRight.driveEncoder.getPosition());
        SmartDashboard.putNumber("Front Right Turning Encoder: ", (360 * (frontRight.turningEncoder.getDistance() / (2 * Math.PI)))); //Radians to degrees
        SmartDashboard.putNumber("Back Left Drive Encoder: ", backLeft.driveEncoder.getPosition());
        SmartDashboard.putNumber("Back Left Turning Encoder: ", (360 * (backLeft.turningEncoder.getDistance() / (2 * Math.PI))));
        SmartDashboard.putNumber("Back Right Drive Encoder: ", backRight.driveEncoder.getPosition());
        SmartDashboard.putNumber("Back Right Turning Encoder: ", (360 * (backRight.turningEncoder.getDistance() / (2 * Math.PI))));
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
