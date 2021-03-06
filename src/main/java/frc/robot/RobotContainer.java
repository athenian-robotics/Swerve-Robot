package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Drive;
import frc.robot.commands.ResetEncoderZeroes;
import frc.robot.commands.ResetWheelPositions;
import frc.robot.subsystems.Drivetrain;


public class RobotContainer {
    public static XboxController xboxController;
    public static JoystickButton xboxA;
    public static JoystickButton xboxB;
    public static JoystickButton xboxX;
    public static JoystickButton xboxY;
    public static JoystickButton xboxLB;
    public static JoystickButton xboxRB;
    public static JoystickButton xboxSquares;
    public static JoystickButton xboxBurger;
    public static Drivetrain drivetrain;

    SendableChooser<SequentialCommandGroup> chooser = new SendableChooser<>();

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        SmartDashboard.putData("AutoChooser", chooser);

        xboxController = new XboxController(Constants.OIConstants.xboxControllerPort);
        drivetrain = new Drivetrain();
        drivetrain.setDefaultCommand(new Drive(drivetrain, xboxController));

        buttonSetup();
        configureButtonBindings();
    }

    public static void disableAll() {
        drivetrain.drive(0, 0, 0);
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
    }

    private void configureButtonBindings() {
        xboxA.whenPressed(new ResetWheelPositions(drivetrain));
        xboxB.whenPressed(new ResetEncoderZeroes(drivetrain));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {return chooser.getSelected();}
}