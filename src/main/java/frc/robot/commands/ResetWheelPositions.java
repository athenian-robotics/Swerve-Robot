package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.SwerveModule;


public class ResetWheelPositions extends CommandBase {
    private final Drivetrain drivetrain;

    public ResetWheelPositions(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        // each subsystem used by the command must be passed into the addRequirements() method (which takes a vararg of Subsystem)
        addRequirements(this.drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.turnWheelsToDegrees(drivetrain.frontLeft, 0);
        drivetrain.turnWheelsToDegrees(drivetrain.frontRight, 0);
        drivetrain.turnWheelsToDegrees(drivetrain.backLeft, 0);
        drivetrain.turnWheelsToDegrees(drivetrain.backRight, 0);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }

}
