// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class SwerveModule extends SubsystemBase {
    private static final double WHEEL_RADIUS = 0.041275;

    private static final double MODULE_MAX_ANGULAR_VELOCITY = Drivetrain.MAX_ANGULAR_SPEED;
    private static final double MODULE_MAX_ANGULAR_ACCELERATION = Math.PI / 4; // radians per second squared

    private final CANSparkMax driveMotor;
    private final CANSparkMax turningMotor;

    public final CANEncoder driveEncoder;
    public final DutyCycleEncoder turningEncoder;

    private final PIDController drivePIDController = new PIDController(0.003, 0, 0);
    private final ProfiledPIDController turningPIDController
            = new ProfiledPIDController(0.5, 0, 0,
            new TrapezoidProfile.Constraints(MODULE_MAX_ANGULAR_VELOCITY, MODULE_MAX_ANGULAR_ACCELERATION));

    private final double turningOffset;

    /**
     * Constructs a SwerveModule.
     *
     * @param driveMotorChannel   ID for the drive motor.
     * @param turningMotorChannel ID for the turning motor.
     */
    public SwerveModule(int driveMotorChannel, int turningMotorChannel, int turningEncoderChannel, double turningOffset) {
        driveMotor = new CANSparkMax(driveMotorChannel, MotorType.kBrushless);
        turningMotor = new CANSparkMax(turningMotorChannel, MotorType.kBrushless);

        this.driveEncoder = driveMotor.getEncoder();
        this.turningEncoder = new DutyCycleEncoder(turningEncoderChannel);
        this.turningOffset = turningOffset;

        // Set the distance per pulse for the drive encoder. We can simply use the
        // distance traveled for one rotation of the wheel divided by the encoder
        // resolution.
        driveEncoder.setPositionConversionFactor(2 * Math.PI * WHEEL_RADIUS / 7.2);

        // Set the distance (in this case, angle) per pulse for the turning encoder.
        // This is the the angle through an entire rotation (2 * wpi::math::pi)
        // divided by the encoder resolution.
        turningEncoder.setDistancePerRotation(2 * Math.PI / 2);

        // Limit the PID Controller's input range between -pi and pi and set the input
        // to be continuous.
        turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
    }

    /**
     * Returns the current state of the module.
     *
     * @return The current state of the module.
     */
    public SwerveModuleState getState() { return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(turningEncoder.getDistance() + turningOffset)); }

    /**
     * Sets the desired state for the module.
     *
     * @param state Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState state) {
        state = SwerveModuleState.optimize(state, new Rotation2d(turningEncoder.getDistance() + turningOffset)); //never rotate more than 90 degrees :)

        // Calculate the motor outputs from the PID controllers.
        final var driveOutput = drivePIDController.calculate(driveEncoder.getVelocity(), state.speedMetersPerSecond);
        final var turnOutput = turningPIDController.calculate(turningEncoder.getDistance() + turningOffset, state.angle.getRadians());

        driveMotor.set(driveOutput);
        turningMotor.set(turnOutput);
    }

}
