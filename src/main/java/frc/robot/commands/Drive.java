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

    public Drive(Drivetrain drivetrain, XboxController controller) {
        this.drivetrain = drivetrain;
        this.controller = controller;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        double FLangle = drivetrain.frontLeft.turningEncoder.getDistance() / (2 * Math.PI) * 360 - frontLeftTurnOffset;
        double FRangle = drivetrain.frontRight.turningEncoder.getDistance() / (2 * Math.PI) * 360 - frontRightTurnOffset; //TURN ENCODER IN RADIANS
        double BLangle = drivetrain.backLeft.turningEncoder.getDistance() / (2 * Math.PI) * 360 - backLeftTurnOffset;
        double BRangle = drivetrain.backRight.turningEncoder.getDistance() / (2 * Math.PI) * 360 - backRightTurnOffset;

        while (Math.abs(FLangle) > 2) {drivetrain.frontLeft.setTurnMotor(0.05);}
        drivetrain.frontLeft.setTurnMotor(0);
        while (Math.abs(FRangle) > 2) {drivetrain.frontRight.setTurnMotor(0.05);}
        drivetrain.frontRight.setTurnMotor(0);
        while (Math.abs(BLangle) > 2) {drivetrain.backLeft.setTurnMotor(0.05);}
        drivetrain.backLeft.setTurnMotor(0);
        while (Math.abs(BRangle) > 2) {drivetrain.backRight.setTurnMotor(0.05);}
        drivetrain.backRight.setTurnMotor(0);
    }

    @Override
    public void execute() {
        double x = controller.getX(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double y = controller.getY(GenericHID.Hand.kLeft) * Drivetrain.MAX_SPEED; // m/s
        double r = controller.getX(GenericHID.Hand.kRight) * Drivetrain.MAX_ANGULAR_SPEED; // rad/s
        x=x<0.1?x>-0.1?0:x:x;
        y=y<0.1?y>-0.1?0:y:y; //Manual dead zone; if any value is below 0.02 (2% movement) make it 0
        r=r<0.1?r>-0.1?0:r:r;

        //drivetrain.updateOdometry();
        drivetrain.drive(y, x);
        //drivetrain.drive(x, y, -r); //r is CW and needs to be CCW
    }

    @Override
    public void end(boolean interrupted) {drivetrain.drive(0, 0, 0);}
}
