package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Drive;
import frc.robot.subsystems.Drivetrain;

public class RobotContainer {
    public static JoystickButton xboxA;
    public static JoystickButton xboxB;
    public static JoystickButton xboxX;
    public static JoystickButton xboxY;
    public static JoystickButton xboxLB;
    public static JoystickButton xboxRB;
    public static JoystickButton xboxSquares;
    public static JoystickButton xboxBurger;
    public static Trigger xboxLS;
    public static XboxController.Axis xboxRS;
    // Define all OI devices here
    public static XboxController xboxController = new XboxController(Constants.OIConstants.xboxControllerPort);
    public static Spark statusLEDs = new Spark(0);
    private final Drivetrain drivetrain = new Drivetrain();
    SendableChooser<SequentialCommandGroup> chooser = new SendableChooser<>();

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        buttonSetup();
        configureButtonBindings();
        SmartDashboard.putData("AutoChooser", chooser);
//        chooser.setDefaultOption("0: CrossLine", new AutoRoutine0(drivetrain));
//        chooser.addOption("1: ShootPreloadsStraight", new AutoRoutine1(drivetrain, shooterSubsystem, intakeSubsystem));
//        chooser.addOption("2: ShootPreloadsTurn", new AutoRoutine2(drivetrain, shooterSubsystem, intakeSubsystem));
//        chooser.addOption("3: GrabTwoStraightShoot", new AutoRoutine3(drivetrain, shooterSubsystem, intakeSubsystem));
//        chooser.addOption("4: GrabTwoTrenchShoot", new AutoRoutine4(drivetrain, shooterSubsystem, intakeSubsystem));
//        chooser.addOption("5: DriveForwardTrench", new AutoRoutine5(drivetrain, intakeSubsystem));

        drivetrain.setDefaultCommand(new Drive(drivetrain, xboxController));
//        ledSubsystem.setDefaultCommand(new LEDCommand(ledSubsystem));
    }

    public static void disableAll(RobotContainer robotContainer) {

    }

    /**
     * Use this method to define your button->command mappings.  Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void buttonSetup() {
        xboxA = new JoystickButton(xboxController, 1);
        xboxB = new JoystickButton(xboxController, 2);
        xboxX = new JoystickButton(xboxController, 3);
        xboxY = new JoystickButton(xboxController, 4);
        xboxLB = new JoystickButton(xboxController, 5);
        xboxRB = new JoystickButton(xboxController, 6);
        xboxSquares = new JoystickButton(xboxController, 7);
        xboxBurger = new JoystickButton(xboxController, 8);
        xboxLS = new Trigger();
    }

    private void configureButtonBindings() {

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return chooser.getSelected();
    }
}