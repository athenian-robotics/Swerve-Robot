package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class Drive extends CommandBase {
    Drivetrain drivetrain;
    XboxController controller;

    public Drive(Drivetrain drivetrain, XboxController controller) { this.drivetrain = drivetrain; this.controller = controller; addRequirements(drivetrain); }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double x = controller.getX(GenericHID.Hand.kLeft);
        double y = controller.getY(GenericHID.Hand.kLeft);
        drivetrain.drive(x, y, 0.1, true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
