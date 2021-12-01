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
        double x = controller.getX(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double y = controller.getY(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double r = controller.getX(GenericHID.Hand.kRight) * Drivetrain.MAX_ANGULAR_SPEED; // rad/s
        x=x<0.02?x>-0.02?0:x:x; y=y<0.02?y>-0.02?0:y:y; r=r<0.02?r>-0.02?0:r:r; //Manual dead zone; if any value is below 0.02 (2% movement) make it 0

        drivetrain.updateOdometry();
        drivetrain.drive(x, y, -r); //r is CW and needs to be CCW
    }

    @Override
    public void end(boolean interrupted) {drivetrain.drive(0, 0, 0);}
}
