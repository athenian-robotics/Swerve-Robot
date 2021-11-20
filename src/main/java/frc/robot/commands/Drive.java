package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class Drive extends CommandBase {
    Drivetrain drivetrain;
    XboxController controller;

    public Drive(Drivetrain drivetrain, XboxController controller) {
        this.drivetrain = drivetrain;
        this.controller = controller;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        double x = controller.getX(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double y = controller.getY(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double r = controller.getX(GenericHID.Hand.kRight) * Drivetrain.MAX_ANGULAR_SPEED; // rad/s

        drivetrain.updateOdometry();
        drivetrain.drive(x, y, r);
    }

    @Override
    public void end(boolean interrupted) {drivetrain.drive(0, 0, 0);}
}
