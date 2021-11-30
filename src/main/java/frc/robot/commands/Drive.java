package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
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
        double x = MathUtil.clamp(controller.getX(GenericHID.Hand.kLeft), 0.1, 1) * Drivetrain.MAX_SPEED; // m/s
        double y = MathUtil.clamp(controller.getY(GenericHID.Hand.kLeft), 0.1, 1) * Drivetrain.MAX_SPEED; // m/s
        double r = MathUtil.clamp(controller.getX(GenericHID.Hand.kRight), 0.1, 1) * Drivetrain.MAX_ANGULAR_SPEED; // rad/s

        drivetrain.updateOdometry();
        drivetrain.drive(x, y, -r); //r is CW and needs to be CCW
    }

    @Override
    public void end(boolean interrupted) {drivetrain.drive(0, 0, 0);}
}
