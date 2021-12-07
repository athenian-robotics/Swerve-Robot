package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.SwerveModule;

import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.DriveConstants.backRightTurnOffset;


public class Drive extends CommandBase {
    Drivetrain drivetrain;
    XboxController controller;

    SwerveModule frontLeft;
    SwerveModule frontRight;
    SwerveModule backLeft;
    SwerveModule backRight;

    public Drive(Drivetrain drivetrain, XboxController controller) {
        this.drivetrain = drivetrain;
        this.controller = controller;

        this.frontLeft = drivetrain.frontLeft;
        this.frontRight = drivetrain.frontRight;
        this.backLeft = drivetrain.backLeft;
        this.backRight = drivetrain.backRight;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.zeroWheels();
    }

    @Override
    public void execute() {
        double x = controller.getX(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double y = controller.getY(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double r = controller.getX(GenericHID.Hand.kRight) * Drivetrain.MAX_ANGULAR_SPEED; // rad/s
        x = x < 0.1 ? x >- 0.1 ? 0 : x : x;
        y = y < 0.1 ? y >- 0.1 ? 0 : y : y; //Manual dead zone; if any value is below 0.1 (10% movement) make it 0
        r = r < 0.1 ? r >- 0.1 ? 0 : r : r;

        r = Math.min(r, 0.5); // TODO THIS IS SOLELY SO DRIVETRAIN.ROTATE DOESN'T GO TOO HARD WHEN TESTING

        drivetrain.drive(-y, x); // A new, raw input & zero pid loops drive function.
        //drivetrain.rotate(r); // TODO PLEASE TEST AND PROOF THIS FOR THE LOVE OF GOD DON'T BREAK ANOTHER CHAIN

        // drivetrain.drive(x, y, -r); // OLD DRIVE FUNCTION (PID CONTROLLED) r is CW and needs to be CCW
        // drivetrain.updateOdometry(); // PID CONTROLLED
    }

    @Override
    public void end(boolean interrupted) {drivetrain.drive(0, 0, 0);}

}
