package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class TurnWheelsToDegrees extends CommandBase {
    Drivetrain drivetrain;

    public TurnWheelsToDegrees(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.turnWheelsToDegrees(drivetrain.frontLeft, 45);
        drivetrain.turnWheelsToDegrees(drivetrain.frontRight, 45);
        drivetrain.turnWheelsToDegrees(drivetrain.backLeft, 45);
        drivetrain.turnWheelsToDegrees(drivetrain.backRight, 45);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
