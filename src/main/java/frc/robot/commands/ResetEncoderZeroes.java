package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.SwerveModule;


public class ResetEncoderZeroes extends CommandBase {
    Drivetrain drivetrain;

    public ResetEncoderZeroes(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements();
    }

    @Override
    public void initialize() {
        SwerveModule[] modules = {drivetrain.frontLeft, drivetrain.frontRight, drivetrain.backLeft, drivetrain.backRight};
        for (SwerveModule module: modules) { module.turningEncoder.reset(); }
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
