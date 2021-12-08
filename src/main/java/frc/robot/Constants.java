package frc.robot;


public final class Constants {
    public static final class DriveConstants {
        public static final int frontLeftDrivePort = 8;
        public static final int frontLeftTurnPort = 11;
        public static final int frontRightDrivePort = 2;
        public static final int frontRightTurnPort = 6;
        public static final int backLeftDrivePort = 5;
        public static final int backLeftTurnPort = 3;
        public static final int backRightDrivePort = 4;
        public static final int backRightTurnPort = 7;

        // The degrees in which the physical position of the motors are zeroed out.
        public static final double frontLeftTurnOffset = 81.19; // ZERO: 86.19
        public static final double frontRightTurnOffset = 69.62; // ZERO: 74.62
        public static final double backLeftTurnOffset = 55.76; // ZERO: 60.76
        public static final double backRightTurnOffset = 339.64; // ZERO: 339.64

        public static final int frontLeftTurnEncoderPort = 3;
        public static final int frontRightTurnEncoderPort = 4;
        public static final int backLeftTurnEncoderPort = 1;
        public static final int backRightTurnEncoderPort = 2;

        public static final double speedScale = .8;
    }

    public static final class AutonomousConstants {

    }

    public static final class OIConstants {
        public static final int xboxControllerPort = 0;
        public static final int fightStickPort = 1;
    }

    public static final class MechanismConstants {

    }

    public static final class PneumaticsConstants {

    }
}
