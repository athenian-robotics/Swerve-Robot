// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Drivetrain;

public class SwerveModule extends SubsystemBase {
    private static final double WHEEL_RADIUS = 0.0508;
    private static final int ENCODER_RESOLUTION = 4096;

    private static final double MODULE_MAX_ANGULAR_VELOCITY = Drivetrain.MAX_ANGULAR_SPEED;
    private static final double MODULE_MAX_ANGULAR_ACCELERATION = 2 * Math.PI; // radians per second squared

    private final SpeedController driveMotor;
    private final SpeedController turningMotor;

    private final Encoder driveEncoder = new Encoder(0, 1);
    private final Encoder turningEncoder = new Encoder(2, 3);

    private final PIDController drivePIDController = new PIDController(1, 0, 0);

    private final ProfiledPIDController turningPIDController
            = new ProfiledPIDController(1, 0, 0,
            new TrapezoidProfile.Constraints(MODULE_MAX_ANGULAR_VELOCITY, MODULE_MAX_ANGULAR_ACCELERATION));

    /**
     * Constructs a SwerveModule.
     *
     * @param driveMotorChannel   ID for the drive motor.
     * @param turningMotorChannel ID for the turning motor.
     */
    public SwerveModule(int driveMotorChannel, int turningMotorChannel)
    {
        driveMotor = new PWMVictorSPX(driveMotorChannel);
        turningMotor = new PWMVictorSPX(turningMotorChannel);

        // Set the distance per pulse for the drive encoder. We can simply use the
        // distance traveled for one rotation of the wheel divided by the encoder
        // resolution.
        driveEncoder.setDistancePerPulse(2 * Math.PI * WHEEL_RADIUS / ENCODER_RESOLUTION);

        // Set the distance (in this case, angle) per pulse for the turning encoder.
        // This is the the angle through an entire rotation (2 * wpi::math::pi)
        // divided by the encoder resolution.
        turningEncoder.setDistancePerPulse(2 * Math.PI / ENCODER_RESOLUTION);

        // Limit the PID Controller's input range between -pi and pi and set the input
        // to be continuous.
        turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
    }

    /**
     * Returns the current state of the module.
     *
     * @return The current state of the module.
     */
    public SwerveModuleState getState()
    {
        return new SwerveModuleState(driveEncoder.getRate(), new Rotation2d(turningEncoder.get()));
    }

    /**
     * Sets the desired state for the module.
     *
     * @param state Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState state)
    {
        // Calculate the drive output from the drive PID controller.
        final var driveOutput = drivePIDController.calculate(driveEncoder.getRate(), state.speedMetersPerSecond);

        // Calculate the turning motor output from the turning PID controller.
        final var turnOutput = turningPIDController.calculate(turningEncoder.get(), state.angle.getRadians());

        // Calculate the turning motor output from the turning PID controller.
        driveMotor.set(driveOutput);
        turningMotor.set(turnOutput);
    }
}
